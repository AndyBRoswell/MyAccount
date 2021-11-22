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
     * @param MergeEnabled <code>True</code>: If <code>Word</code> and <code>Synonym</code> respectively exist in different synonym groups,
     * then these 2 groups will be merged into 1 group which includes every synonym from the original 2 groups.
     * <code>False</code>: Does nothing in this situation.
     */
    fun Insert(Word: String, Synonym: String, MergeEnabled: Boolean = false) {
        var IW = CanonicalID[Word]
        var IS = CanonicalID[Synonym]
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
                if (IW == IS) return                                    // Word and Synonym are both existed in the same synonym group
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
                    }
                }
            }
        }
    }

    /**
     * Add a group of synonyms for the specified word.
     * @param Word The word you want to add a new synonym for.
     * @param Synonyms The group of synonyms you want to add for <code>Word</code>.
     * If <code>Synonyms.size == 1</code> and <code>Word</code> is identical to this only synonym, then the insertion is skipped.
     */
//    fun Insert(Word: String, Synonyms: Iterable<String>) {
//        var CID = CanonicalID[Word]
//        if (CID == null) { // Allocate a new canonical ID for the specified word if it doesn't appear in this dict
//            CID = GenerateCanonicalID()
//            CanonicalID[Word] = CID
//            this.Synonyms[CID] = hashSetOf(Word)
//            this.Synonyms[CID]!!.addAll(Synonyms)
//        }
//        else {
//            val ExistedSynonyms = this.Synonyms[CID]!!
//            for (Synonym in Synonyms) { // Regardless of Word == Synonym
//                CanonicalID[Synonym] = CID          // The canonical ID of the synonym is identical to the canonical ID of the specified word
//                ExistedSynonyms.add(Synonym)
//            }
//        }
//    }

    /**
     * Delete a synonym for the specified word.
     * @param Word The word you want to delete a synonym for.
     * @param Synonym The synonym you want to add for <code>Word</code>.
     * @return The original canonical ID of <code>Word</code> if all of its synonyms are deleted from this dictionary.
     * This ID won't exist in this dictionary any more.
     * You may use this return value to modify other data indexed by the original canonical ID, e.g., use a new ID to index them instead.
     * Otherwise return NO_ID.
     */
    fun Delete(Word: String, Synonym: String): Long {
        val CID = CanonicalID[Word] ?: return NO_ID
        CanonicalID.remove(Synonym)
        val ExistedSynonyms = Synonyms[CID]!!
        ExistedSynonyms.remove(Synonym)
        if (ExistedSynonyms.size < 1) {
            CanonicalID.remove(ExistedSynonyms.iterator().next())   // Remove the only one existed element
            return CID
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
//    fun Delete(Word: String, Synonyms: Iterable<String>): Long {
//        val CID = CanonicalID[Word] ?: return NO_ID
//        val ExistedSynonyms = this.Synonyms[CID]!!
//        for (Synonym in Synonyms) {
//            CanonicalID.remove(Synonym)
//            ExistedSynonyms.remove(Synonym)
//            if (ExistedSynonyms.size < 1) {
//                CanonicalID.remove(ExistedSynonyms.iterator().next())   // Remove the only one existed element
//                return CID
//            }
//        }
//        return NO_ID
//    }

    /**
     * **`<DESTRUCTIVE`>** Delete all entries in the dictionary.
     */
    fun DeleteAll() {
        CanonicalID.clear()
        Synonyms.clear()
    }

    fun GetSynonyms(Word: String): HashSet<String>? = Synonyms[CanonicalID[Word]]

    fun SynonymsCount(Word: String): Int {
        val CID = CanonicalID[Word] ?: return 0
        return Synonyms[CID]!!.size - 1         // exclude Word itself
    }

    fun GetCanonicalID(Word: String) = CanonicalID[Word]

    fun GetTotalWordCount() = CanonicalID.size

    fun GetSynonymGroupCount() = Synonyms.size

    private fun GenerateCanonicalID(): Long = System.nanoTime()

    fun AllSynonyms() = Synonyms
}