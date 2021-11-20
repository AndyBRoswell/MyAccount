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
     * <code>Word</code> must be different from <code>Synonym</code>
     */
    fun Insert(Word: String, Synonym: String) {
        if (Word == Synonym) return
        var CID = CanonicalID[Word]
        if (CID == null) { // Allocate a new canonical ID for the specified word if it doesn't appear in this dict
            CID = GenerateCanonicalID()
            CanonicalID[Word] = CID
            CanonicalID[Synonym] = CID                      // The canonical ID of the synonym is identical to the canonical ID of the specified word
            this.Synonyms[CID] = hashSetOf(Word, Synonym)
        }
        else {
            CanonicalID[Synonym] = CID                      // The canonical ID of the synonym is identical to the canonical ID of the specified word
            Synonyms[CID]!!.add(Synonym)
        }
    }

    /**
     * Add a group of synonyms for the specified word.
     * @param Word The word you want to add a new synonym for.
     * @param Synonyms The group of synonyms you want to add for <code>Word</code>.
     * If <code>Synonyms.size == 1</code> and <code>Word</code> is identical to this only synonym, then the insertion is skipped.
     */
    fun Insert(Word: String, Synonyms: Iterable<String>) {
        var CID = CanonicalID[Word]
        if (CID == null) { // Allocate a new canonical ID for the specified word if it doesn't appear in this dict
            CID = GenerateCanonicalID()
            CanonicalID[Word] = CID
            this.Synonyms[CID] = hashSetOf(Word)
            this.Synonyms[CID]!!.addAll(Synonyms)
        }
        else {
            val ExistedSynonyms = this.Synonyms[CID]!!
            for (Synonym in Synonyms) {
                CanonicalID[Synonym] = CID          // The canonical ID of the synonym is identical to the canonical ID of the specified word
                ExistedSynonyms.add(Synonym)
            }
        }
    }

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
        if (ExistedSynonyms.size < 2) {
            CanonicalID.remove(ExistedSynonyms.iterator().next())   // Remove the only one existed element
            Synonyms.remove(CID)                                    // Remove the set which has only one existed element
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
    fun Delete(Word: String, Synonyms: Iterable<String>): Long {
        val CID = CanonicalID[Word] ?: return NO_ID
        val ExistedSynonyms = this.Synonyms[CID]!!
        for (Synonym in Synonyms) {
            CanonicalID.remove(Synonym)
            ExistedSynonyms.remove(Synonym)
            if (ExistedSynonyms.size < 2) {
                CanonicalID.remove(ExistedSynonyms.iterator().next())   // Remove the only one existed element
                this.Synonyms.remove(CID)                               // Remove the set which has only one existed element
                return CID
            }
        }
        return NO_ID
    }

    fun GetSynonyms(Word: String): Iterable<String>? = Synonyms[CanonicalID[Word]]

    fun SynonymsCount(Word: String): Int {
        val CID = CanonicalID[Word] ?: return 0
        return Synonyms[CID]!!.size - 1         // exclude Word itself
    }

    fun GetCanonicalID(Word: String) = CanonicalID[Word]

    fun GetTotalWordCount() = CanonicalID.size

    fun GetSynonymGroupCount() = Synonyms.size

    private fun GenerateCanonicalID(): Long = System.nanoTime()
}