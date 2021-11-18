package top.abr.myaccount

class SynonymDictionary {
    private val CanonicalWord: MutableMap<String, String> = HashMap()
    private val NonCanonicalWords: MutableMap<String, HashSet<String>> = HashMap()

    fun Insert(Word: String, Synonym: String) {
        val CWord = CanonicalWord[Word]
        if (CWord == null) {                                                        // The param Word is not in the dict yet
            CanonicalWord[Word] = Word                                              // Use the param Word itself as the canonical word
            if (Word != Synonym) NonCanonicalWords[Word] = hashSetOf(Synonym)       // The other word Synonym as the non-canonical aliases
            else NonCanonicalWords[Word] = HashSet()                                // Elide the repeated element Word
        }
        else if (CWord == Word) {
            if (Word != Synonym) NonCanonicalWords[CWord]!!.add(Synonym)            // Elide the repeated element Word
            else return                                                             // No synonyms to add. Note: Canonical word should NOT appear in the value of K-V pairs of NonCanonicalWords
        }
        else if (CWord == Synonym) {
            if (Word != Synonym) NonCanonicalWords[CWord]!!.add(Word)               // Elide the repeated element Word
            else return                                                             // No synonyms to add
        }
        else { // CWord != Word and CWord != Synonym
            NonCanonicalWords[CWord]!!.add(Synonym)                                 // Regardless of whether Word == Synonym
        }
    }

    fun Insert(Word: String, Synonyms: Iterable<String>) {
        val CWord = CanonicalWord[Word]
        if (CWord == null) {
            CanonicalWord[Word] = Word
            val NCWords = HashSet<String>()
            for (Synonym in Synonyms) {
                if (Word != Synonym) NCWords.add(Synonym)                           // Elide the repeated element Word
            }
            NonCanonicalWords[Word] = NCWords
        }
        else if (CWord == Word) {
            val NCWords = NonCanonicalWords[CWord]
            for (Synonym in Synonyms) {

            }
        }
        else {

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