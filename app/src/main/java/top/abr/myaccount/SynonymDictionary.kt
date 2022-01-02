package top.abr.myaccount

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

open class SynonymDictionary {
    companion object {
        const val NO_ID = 0L
    }

    private val CanonicalID: MutableMap<String, Long> = HashMap()
    private val Synonyms: MutableMap<Long, HashSet<String>> = TreeMap()

    /**
     * Add a synonym for the specified word.
     * @param Word The word you want to add a new synonym for.
     * @param Synonym The synonym you want to add for <code>Word</code>.
     * You may swap <code>Word</code> and <code>Synonym</code>. This function will recognize that.
     * @param MergeEnabled <code>True</code>: If <code>Word</code> and <code>Synonym</code> respectively exist in different synonym groups of this dictionary,
     * then these 2 groups will be merged into 1 group which includes every synonym from the original 2 groups.
     * <code>False</code>: Does nothing in this situation.
     */
    fun Insert(Word: String, Synonym: String, MergeEnabled: Boolean = false) {
        var IW = CanonicalID[Word]
        val IS = CanonicalID[Synonym]
        when (((if (IW != null) 1 else 0) shl 1) + (if (IS != null) 1 else 0)) {
            0b00 -> {
                IW = GenerateCanonicalID()
                CanonicalID[Word] = IW
                CanonicalID[Synonym] = IW               // The canonical ID of the synonym is identical to the canonical ID of the specified word
                Synonyms[IW] = hashSetOf(Word, Synonym)
            }
            0b01 -> {
                CanonicalID[Word] = IS!!
                Synonyms[IS]!!.add(Word)
            }
            0b10 -> {
                CanonicalID[Synonym] = IW!!
                Synonyms[IW]!!.add(Synonym)
            }
            0b11 -> {
                if (IW == IS) return    // Word and Synonym are both existed in the same synonym group
                else {
                    when (MergeEnabled) {
                        true -> {
                            val SW = Synonyms[IW]!!
                            val SS = Synonyms[IS]!!
                            if (SW.size >= SS.size) {
                                for (CurrentSynonym in SS) {
                                    CanonicalID[CurrentSynonym] = IW!!
                                    SW.add(CurrentSynonym)
                                }
                                Synonyms.remove(IS)
                            }
                            else {
                                for (CurrentSynonym in SW) {
                                    CanonicalID[CurrentSynonym] = IS!!
                                    SS.add(CurrentSynonym)
                                }
                                Synonyms.remove(IW)
                            }
                        }
                        false -> {}
                    }
                }
            }
        }
    }

    /**
     * Add a group of synonyms for the specified word.
     * @param Word The word you want to add a new synonym for.
     * @param Synonyms The group of synonyms you want to add for <code>Word</code>.
     * If any one synonym in <code>Synonyms</code> has been added as a synonym of <code>Word</code>, then the insertion of this synonym is skipped.
     */
    fun Insert(Word: String, Synonyms: Iterable<String>, MergeEnabled: Boolean = false) {
        var IW = CanonicalID[Word]
        if (IW == null) { // Word doesn't exist. Add it first.
            IW = GenerateCanonicalID()
            CanonicalID[Word] = IW
            this.Synonyms[IW] = hashSetOf(Word)
        }
        val TargetedSynonyms = this.Synonyms[IW]!!
        for (Synonym in Synonyms) {
            when (val IS = CanonicalID[Synonym]) {
                null -> { // Add Synonym as a new synonym of Word directly
                    CanonicalID[Synonym] = IW
                    TargetedSynonyms.add(Synonym)
                }
                IW -> {} // Synonym already exists as a synonym of Word. Skip this insertion.
                else -> {
                    when (MergeEnabled) {
                        true -> {
                            val SynonymsToBeMerged = this.Synonyms[IS]!!
                            for (SynonymToBeMerged in SynonymsToBeMerged) {
                                CanonicalID[SynonymToBeMerged] = IW
                                TargetedSynonyms.add(SynonymToBeMerged)
                            }
                            this.Synonyms.remove(IS)
                        }
                        false -> {} // Merge is disabled. Skip this insertion.
                    }
                }
            }
        }
    }

    /**
     * Add a group of synonyms.
     */
    fun Insert(Synonyms: Iterable<String>, MergeEnabled: Boolean = false) {
        val SynonymGroupSet = HashSet<Pair<Long, HashSet<String>?>>()
        val NonexistentSynonyms = HashSet<String>()
        for (Synonym in Synonyms) {
            when (val CID = CanonicalID[Synonym]) {
                null -> NonexistentSynonyms.add(Synonym)
                else -> SynonymGroupSet.add(Pair(CID, this.Synonyms[CID]))
            }
        }
        val SynonymGroup = SynonymGroupSet.toTypedArray()
        if (SynonymGroup.isNotEmpty()) {
            if (!MergeEnabled and (SynonymGroup.size > 1)) return // Merge disabled and found 2 or more existed synonym groups
            SynonymGroup.sortByDescending { it.second!!.size }
            val BiggestSynonymGroup = SynonymGroup[0]
            val CID = CanonicalID[SynonymGroup[0].second!!.iterator().next()]!!   // Canonical ID of any one synonym in this synonym group (Their canonical IDs are all identical)
            for (i in 1 until SynonymGroup.size) { // Merge into a single synonym group
                for (Synonym in SynonymGroup[i].second!!) {
                    CanonicalID[Synonym] = CID
                    BiggestSynonymGroup.second!!.add(Synonym)
                }
                this.Synonyms.remove(SynonymGroup[i].first)
            }
            for (Synonym in NonexistentSynonyms) { // Add nonexistent synonyms (i.e. new synonyms)
                CanonicalID[Synonym] = CID
                BiggestSynonymGroup.second!!.add(Synonym)
            }
        }
        else { // No existent synonyms. Directly add new synonyms.
            val CID = GenerateCanonicalID()
            for (Synonym in NonexistentSynonyms) CanonicalID[Synonym] = CID
            this.Synonyms[CID] = NonexistentSynonyms
        }
    }

    /**
     * Delete a synonym for the specified word.
     * @param Word The word you want to delete a synonym for.
     * @param Synonym The synonym you want to add for <code>Word</code>.
     * <code>Word</code> and <code>Synonym</code> can be the same.
     * And if <code>Word</code> or <code>Synonym</code> doesn't exist in this dictionary, or <code>Synonym</code> is not a synonym of <code>Word</code>, this function does nothing.
     * @return The original canonical ID of <code>Word</code> if all of its synonyms are deleted from this dictionary.
     * This ID won't exist in this dictionary any more.
     * You may use this return value to modify other data indexed by the original canonical ID, e.g., use a new ID to index them instead.
     * Otherwise return NO_ID.
     */
    fun Delete(Word: String, Synonym: String): Long {
        val IW = CanonicalID[Word] ?: return NO_ID
        val IS = CanonicalID[Synonym]
        if (IW != IS) return NO_ID                  // Reject to delete when Synonym is not a synonym of Word.
        CanonicalID.remove(Synonym)
        val ExistedSynonyms = Synonyms[IW]!!
        ExistedSynonyms.remove(Synonym)
        if (ExistedSynonyms.size < 1) {
            Synonyms.remove(IW)
            return IW                               // This ID won't exist in this dictionary any more.
        }
        return NO_ID
    }

    /**
     * Delete a group of synonyms for the specified word.
     * @param Word The word you want to delete synonyms for.
     * @param Synonyms The group of synonyms you want to delete for <code>Word</code>.
     * @return The original canonical ID of <code>Word</code> if all of its synonyms are deleted from this dictionary.
     * This ID won't exist in this dictionary any more.
     * You may use this return value to modify other data indexed by the original canonical ID, e.g., use a new ID to index them instead.
     * Otherwise return NO_ID.
     */
    fun Delete(Word: String, Synonyms: Iterable<String>): Long {
        val CID = CanonicalID[Word] ?: return NO_ID
        val ExistedSynonyms = this.Synonyms[CID]!!
        for (Synonym in Synonyms) {
            CanonicalID.remove(Synonym)
            ExistedSynonyms.remove(Synonym)
            if (ExistedSynonyms.size < 1) {
                this.Synonyms.remove(CID)           // Remove the only one existed element
                return CID
            }
        }
        return NO_ID
    }

    /**
     * Delete a group of synonyms.
     * @param Synonyms The group of synonyms you want to delete.
     * @return The original canonical IDs of each group which contains no ID after the deletion.
     */
//    fun Delete(Synonyms: Iterable<String>): Long {
//        // TODO
//    }

    /**
     * **`<DESTRUCTIVE`>** Delete all entries in the dictionary.
     */
    fun DeleteAll() {
        CanonicalID.clear()
        Synonyms.clear()
    }

    /**
     * @return All synonyms of <code>Word</code>
     */
    fun GetSynonyms(Word: String): HashSet<String>? {
        val CID = CanonicalID[Word] ?: return null
        return Synonyms[CID]
    }

    /**
     * Return all synonyms of the word with the specified <code>CanonicalID</code>.
     */
    fun GetSynonyms(CanonicalID: Long): Set<String>? = Synonyms[CanonicalID]

    /**
     * Return the number of synonyms of <code>Word</code>. If the word doesn't exist in this dictionary, return -1.
     */
    fun SynonymsCount(Word: String): Int {
        val CID = CanonicalID[Word] ?: return 0
        return Synonyms[CID]!!.size - 1         // exclude Word itself
    }

    /**
     * Get the canonical ID of <code>Word</code> in this dictionary.
     */
    fun GetCanonicalID(Word: String) = CanonicalID[Word]

    /**
     * Set the canonical ID of <code>Word</code> in this dictionary. All of the synonyms of <code>Word</code> will also have this ID.
     * @param Word If the word doesn't exist in the dictionary, does nothing.
     * @param ID If this ID has been already acquired by other synonym(s), does nothing.
     * @return Succeeded or failed.
     */
    fun SetCanonicalID(Word: String, ID: Long): Boolean {
        val CID = CanonicalID[Word] ?: return false             // If param Word doesn't exist, does nothing.
        if (CID == ID) return false                             // The new ID is identical to the old one. No need to modify.
        if (this.Synonyms[ID] != null) return false             // This ID is already used
        val Synonyms = this.Synonyms[CID]!!
        for (Synonym in Synonyms) CanonicalID[Synonym] = ID     // Replace their canonical ID with this new ID
        this.Synonyms[ID] = Synonyms                            // Add the new mappings
        this.Synonyms.remove(CID)                               // Remove the old mappings
        return true
    }

    fun GetTotalWordCount() = CanonicalID.size

    fun GetSynonymGroupCount() = Synonyms.size

    private fun GenerateCanonicalID(): Long = System.nanoTime()

    fun AllSynonyms(): Map<Long, Set<String>> = Synonyms
}