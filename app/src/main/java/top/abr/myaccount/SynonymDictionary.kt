package top.abr.myaccount

class SynonymDictionary {
	private val CanonicalWord: MutableMap<String, String> = HashMap()
	private val NonCanonicalWords: MutableMap<String, HashSet<String>> = HashMap()

	fun Insert(Word: String, Synonym: String) {
		val CWord = CanonicalWord[Word]
		if (CWord == null) {														// The param Word is not in the dict yet.
			CanonicalWord[Word] = Word												// Use this word itself as the canonical word
			if (Word != Synonym) NonCanonicalWords[Word] = hashSetOf(Synonym)
			else NonCanonicalWords[Word] = HashSet()								// Elide the repeated word
		}
		else {
			
			NonCanonicalWords[Word]!!.add(Synonym)
		}
	}

	fun Insert(Word: String, Synonyms: Iterable<String>) {
		val CWord = CanonicalWord[Word]
		if (CWord == null) {
			CanonicalWord[Word] = Word
			val NCWords = HashSet<String>()
			for (Synonym in Synonyms) NCWords.add(Synonym)
			NonCanonicalWords[Word] = NCWords
		}
		else {
			val NCWords = NonCanonicalWords[CWord]
			for (Synonym in Synonyms) NCWords!!.add(Synonym)
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