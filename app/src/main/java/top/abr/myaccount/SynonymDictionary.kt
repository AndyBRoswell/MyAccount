package top.abr.myaccount

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

open class SynonymDictionary {
    companion object {
        val NO_ID = 0L
    }

    private val CanonicalID: MutableMap<String, Long> = HashMap()
    private val Synonyms: MutableMap<Long, HashSet<String>> = TreeMap()

    fun Insert(Word: String, Synonym: String) {
        
    }

    fun Insert(Word: String, Synonyms: Iterable<String>) {

    }

    fun Delete(Word: String, Synonym: String) {

    }

    fun Delete(Word: String, Synonyms: Iterable<String>) {

    }

    fun Find(Word: String): HashSet<String>? = Synonyms[CanonicalID[Word]]
}