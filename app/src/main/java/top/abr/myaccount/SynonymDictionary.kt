package top.abr.myaccount

open class SynonymDictionary {
    private val CanonicalWord: MutableMap<String, String> = HashMap()
    private val NonCanonicalWords: MutableMap<String, HashSet<String>> = HashMap()

    fun Insert(Word: String, Synonym: String) {
        val CWord = CanonicalWord[Word]
        if (CWord == null) { // The param Word is not in the dict yet
            CanonicalWord[Word] = Word                                          // Use the param Word itself as the canonical word
            if (Word != Synonym) NonCanonicalWords[Word] = hashSetOf(Synonym)   // The other word Synonym as the non-canonical synonym
            else NonCanonicalWords[Word] = HashSet()                            // Elide the repeated element Word
        }
        else if (CWord == Word) {
            if (CWord != Synonym) NonCanonicalWords[CWord]!!.add(Synonym)       // Elide the repeated element Word
            else return                                                         // No synonyms to add. Note: Canonical word should NOT appear in the value of K-V pairs of NonCanonicalWords
        }
        else if (CWord == Synonym) { // Given CWord != Word
            NonCanonicalWords[CWord]!!.add(Word)                                // Elide the repeated element Word
        }
        else { // CWord != Word and CWord != Synonym
            NonCanonicalWords[CWord]!!.add(Synonym)                             // Regardless of whether Word == Synonym
        }
    }

    fun Insert(Word: String, Synonyms: Iterable<String>) {
        val CWord = CanonicalWord[Word]
        if (CWord == null) {
            CanonicalWord[Word] = Word                                          // Use the param Word itself as the canonical word
            val NCWords = HashSet<String>()                                     // Synonyms as the non-canonical synonyms
            for (Synonym in Synonyms) {
                if (Word != Synonym) NCWords.add(Synonym)                       // Elide the repeated element Word
            }
            NonCanonicalWords[Word] = NCWords
        }
        else if (CWord == Word) {
            val NCWords = NonCanonicalWords[CWord]!!                            // Get the existed set of non-canonical words
            for (Synonym in Synonyms) {                                         // Synonyms as the non-canonical synonyms
                if (CWord != Synonym) NCWords.add(Synonym)                      // Elide the repeated element Word
            }
        }
        else { // CWord != Word
            val NCWords = NonCanonicalWords[CWord]!!
            NCWords.add(Word)
            for (Synonym in Synonyms) {                                         // Synonyms as the non-canonical synonyms
                if (CWord != Synonym) NCWords.add(Synonym)                      // Elide the repeated element Word
            }
        }
    }

    fun Delete(Word: String, Synonym: String) {

    }

    fun Delete(Word: String, Synonyms: Iterable<String>) {

    }

    fun Modify(Word: String, OldSynonym: String, NewSynonym: String) {

    }

    fun Modify(Word: String, OldSynonyms: Iterable<String>, NewSynonyms: Iterable<String>) {

    }

    fun Find(Word: String): Map.Entry<String, String>? {
        return null
    }
}