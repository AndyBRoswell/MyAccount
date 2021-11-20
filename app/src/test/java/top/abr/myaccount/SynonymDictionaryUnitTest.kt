package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Assert.*
import org.junit.Test
import java.lang.AssertionError
import java.security.SecureRandom

class SynonymDictionaryUnitTest {
    private val RandomSource = SecureRandom.getInstanceStrong()

    private fun NextInt(Min: Int, Max: Int) = RandomSource.ints(1, Min, Max).iterator().next()
    private fun NextLong(Min: Long, Max: Long) = RandomSource.longs(1, Min, Max).iterator().next()
    private fun NextIntRInclusive(Min: Int, Max: Int) = RandomSource.ints(1, Min, Max + 1).iterator().next()
    private fun NextLongRInclusive(Min: Long, Max: Long) = RandomSource.longs(1, Min, Max + 1).iterator().next()
    private fun RandomString(Lmin: Int, Lmax: Int) = RandomStringUtils.randomAlphabetic(Lmin, Lmax)
    private fun RandomStringRInclusive(Lmin: Int, Lmax: Int) = RandomStringUtils.randomAlphabetic(Lmin, Lmax + 1)

    @Test fun InsertTest() {
        // Range parameters of random data
        val MAX_SYNONYM_GROUP_COUNT = 20L
        val MIN_SYNONYM_GROUP_COUNT = 10L
        val MAX_SYNONYM_COUNT = 20
        val MIN_SYNONYM_COUNT = 10
        val MAX_WORD_LENGTH = 1
        val MIN_WORD_LENGTH = 1

        val SynonymGroupCount = NextLongRInclusive(MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT)
        val SynonymCounts = RandomSource.ints(SynonymGroupCount, MIN_SYNONYM_COUNT, MAX_SYNONYM_COUNT).toArray()

        // Round 1: Insert(Word: String, Synonym: String)
        val SDict = SynonymDictionary()
        val SSets = ArrayList<HashSet<String>>() // Test data
        // Add test data
        for (SynonymCount in SynonymCounts) {
            val SSet = HashSet<String>()
            val SList = ArrayList<String>()
            val Synonym = RandomStringRInclusive(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
            SDict.Insert(Synonym, Synonym)
            SSet.add(Synonym)
            SList.add(Synonym)
            for (i in 2..SynonymCount) {
                val Synonym = RandomStringRInclusive(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
                val Word = SList[NextInt(0, SList.size)]
                SDict.Insert(Word, Synonym)
                SSet.add(Synonym)
                SList.add(Synonym)
                println(SDict.GetCanonicalID(Word))
                println(SDict.GetCanonicalID(Synonym))
                println("----------------")
            }
            SSets.add(SSet)
            println()
        }
        // Find test
        for (SSet in SSets) {
            val QueryResult = ArrayList<Iterable<String>>()
            for (Synonym in SSet) { // The query result must be the same when finding with synonyms in the identical synonym group
                QueryResult.add(SDict.GetSynonyms(Synonym)!!)
            }
            for (i in 0 until QueryResult.size) { // Verify the consistency of each query result
                val I = QueryResult[i].iterator()
                while (I.hasNext()) {
//                    assertTrue(SSet.contains(I.next()))
                    val FoundedSynonym = I.next()
                    if (!SSet.contains(FoundedSynonym)) {
                        println("================ ERROR OCCURRED ================")
                        println("FoundedSynonym = $FoundedSynonym")
                        println("SSet = $SSet")
                        val CIDs = ArrayList<Long?>()
                        for (Synonym in SSet) CIDs.add(SDict.GetCanonicalID(Synonym))
                        println("Their canonical IDs: $CIDs")
                        println("QueryResult[$i] = " + QueryResult[i])
                        CIDs.clear()
                        for (Synonym in QueryResult[i]) CIDs.add(SDict.GetCanonicalID(Synonym))
                        println("Their canonical IDs: $CIDs")
                        println("================ ERROR OCCURRED ================")
                        throw AssertionError()
                    }
                }
            }
        }
//        // Round 2: Insert(Word: String, Synonyms: Iterable<String>)
//        SDict.DeleteAll()
//        SSets.clear()
//        // Add test data
//        for (SynonymCount in SynonymCounts) {
//            val SSet = HashSet<String>()
//            val SList = ArrayList<String>()
//            for (i in 1..SynonymCount) {
//                val Synonym = RandomStringRInclusive(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
//                SSet.add(Synonym)
//                SList.add(Synonym)
//            }
//            SDict.Insert(SList[NextInt(0, SynonymCount)], SList)
//            SSets.add(SSet)
//        }
//        // Find test
//        for (SSet in SSets) {
//            val QueryResult = ArrayList<Iterable<String>>()
//            for (Synonym in SSet) { // The query result must be the same when finding with synonyms in the identical synonym group
//                QueryResult.add(SDict.GetSynonyms(Synonym)!!)
//            }
//            for (i in 0 until QueryResult.size) { // Verify the consistency of each query result
//                val I = QueryResult[i].iterator()
//                while (I.hasNext()) assertTrue(SSet.contains(I.next()))
//            }
//        }
    }
}