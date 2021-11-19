package top.abr.myaccount

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

open class SynonymDictionary {
    companion object {
        val NO_ID = 0L
    }

    private val CanonicalID: MutableMap<String, Long> = HashMap()       // These IDs are for internal usage ONLY
    private val Synonyms: MutableMap<Long, HashSet<String>> = TreeMap()

    fun Insert(Word: String, Synonym: String) {
        var CID = CanonicalID[Word]
        if (CID == null) CID = GenerateCanonicalID()    // Allocate a new canonical ID for the specified word if it doesn't appear in this dict
        CanonicalID[Synonym] = CID                      // The canonical ID of the synonym is identical to the canonical ID of the specified word
        Synonyms[CID]!!.add(Synonym)
    }

    fun Insert(Word: String, Synonyms: Iterable<String>) {
        var CID = CanonicalID[Word]
        if (CID == null) CID = GenerateCanonicalID()    // Allocate a new canonical ID for the specified word if it doesn't appear in this dict
        for (Synonym in Synonyms) {
            
        }
    }

    fun Delete(Word: String, Synonym: String) {

    }

    fun Delete(Word: String, Synonyms: Iterable<String>) {

    }

    fun Find(Word: String): Iterable<String>? = Synonyms[CanonicalID[Word]]

    fun GetCanonicalID(Word: String) = CanonicalID[Word]

    private fun GenerateCanonicalID(): Long = System.nanoTime()
}