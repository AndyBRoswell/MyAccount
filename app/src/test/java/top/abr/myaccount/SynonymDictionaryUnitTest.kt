package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Assert.*
import org.junit.Test
import java.security.SecureRandom

class SynonymDictionaryUnitTest {
    private val RandomSource = SecureRandom.getInstanceStrong()

    private fun NextInt(Min: Int, Max: Int) = RandomSource.ints(1, Min, Max).iterator().next()
    private fun NextLong(Min: Long, Max: Long) = RandomSource.longs(1, Min, Max).iterator().next()

    @Test fun InsertTest() {
        // Range parameters of random data
        val MAX_SYNONYM_GROUP_COUNT = 20L
        val MIN_SYNONYM_GROUP_COUNT = 10L
        val MAX_SYNONYM_COUNT = 20
        val MIN_SYNONYM_COUNT = 10
        val MAX_WORD_LENGTH = 32
        val MIN_WORD_LENGTH = 1

        val SynonymGroupCount = NextLong(MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT)
        val SynonymCounts = RandomSource.ints(MIN_SYNONYM_COUNT, MAX_SYNONYM_COUNT)

        // Round 1: Insert(Word: String, Synonym: String)
        val SDict1 = SynonymDictionary()
        val SSets = HashSet<HashSet<String>>()
        // Add test data
        for (SynonymCount in SynonymCounts) {
            val SSet = HashSet<String>()
            val SArray = ArrayList<String>()
            for (i in 1..SynonymCount) {
                val Synonym = RandomStringUtils.randomAlphabetic(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
                SSet.add(Synonym)
                SArray.add(Synonym)
                SDict1.Insert(SArray[NextInt(0, SArray.size)], )
            }
            SSets.add(SSet)
        }
        // Find test
        for (SSet in SSets) {
            val FindResult = ArrayList<Iterable<String>>()
            for (Synonym in SSet) { // The query result must be the same when finding with synonyms in the identical synonym group.
                FindResult.add(SDict1.Find(Synonym)!!)
            }
            for (i in 0 until FindResult.size) {

            }
        }
    }
}