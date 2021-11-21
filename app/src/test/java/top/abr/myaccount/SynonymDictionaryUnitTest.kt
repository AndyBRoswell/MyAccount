package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Assert.*
import org.junit.Test
import java.lang.AssertionError
import java.security.SecureRandom
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.pow

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
        val MAX_SYNONYM_GROUP_COUNT = 20
        val MIN_SYNONYM_GROUP_COUNT = 10
        val MAX_SYNONYM_GROUP_SIZE = 20
        val MIN_SYNONYM_GROUP_SIZE = 10
        val MAX_WORD_LENGTH = 2
        val MIN_WORD_LENGTH = 1
        // MAX_SYNONYM_GROUP_SIZE must be less than pow(C, MAX_WORD_LENGTH), should be MUCH LESS THAN pow(C, MAX_WORD_LENGTH), C = 52 when randomAlphabetic() is used
        val MAX_ALL_SYNONYMS_COUNT = 52.0.pow(MAX_WORD_LENGTH.toDouble()).toInt()
        assertTrue(MAX_SYNONYM_GROUP_SIZE < MAX_ALL_SYNONYMS_COUNT)
        val SynonymGroupCount = NextIntRInclusive(MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT)
        val SynonymCount = RandomSource.ints(SynonymGroupCount.toLong(), MIN_SYNONYM_GROUP_SIZE, MAX_SYNONYM_GROUP_SIZE).toArray()

        // Round 1: Insert(Word: String, Synonym: String)
        val SDict = SynonymDictionary()
        val SynonymsForTest = HashSet<String>()
        val SList = ArrayList<ArrayList<String>>()
        val TestCount = ArrayList<ArrayList<Int>>()
        val UncoveredSynonymCount = ArrayList<Int>()
        // Prepare test data
        for (i in SynonymCount.indices) {
            SList.add(ArrayList())
            while (SList[i].size < SynonymCount[i]) {
                val Synonym = RandomStringRInclusive(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
                // SynonymsForTest.size must be less than pow(C, MAX_WORD_LENGTH), should be MUCH LESS THAN pow(C, MAX_WORD_LENGTH), C = 52 when randomAlphabetic() is used
                if (SynonymsForTest.add(Synonym)) SList[i].add(Synonym)                 // Each synonym which will be added is different from others
                else assertTrue(SynonymsForTest.size < MAX_ALL_SYNONYMS_COUNT)
            }
            UncoveredSynonymCount.add(SynonymCount[i])
            TestCount.add(ArrayList<Int>().apply { for (j in 1..SynonymCount[i]) add(0) })
//            val CurrentTestCount = ArrayList<Int>()
//            for (j in 1..SynonymCount[i]) CurrentTestCount.add(0)
//            TestCount.add(CurrentTestCount)
        }
        // Start to insert
        for (i in SList.indices) { // Try as many as it can. Every synonym in SList is guaranteed to be the arg of SynonymDictionary.insert() at least once.
            while (UncoveredSynonymCount[i] > 0) {
                val WordIndex = NextInt(0, SList[i].size)
                val SynonymIndex = NextInt(0, SList[i].size)
                if (TestCount[i][WordIndex] == 0) --UncoveredSynonymCount[i]
                if (TestCount[i][SynonymIndex] == 0) --UncoveredSynonymCount[i]
                SDict.Insert(SList[i][WordIndex], SList[i][SynonymIndex])
                ++TestCount[i][WordIndex]
                ++TestCount[i][SynonymIndex]
            }
        }
        // Start to verify
        for (i in SList.indices) {
            for (QuerySynonym in SList[i]) {
                val QueryResult = SDict.GetSynonyms(QuerySynonym)
                for (ExpectedSynonym in SList[i]) {
//                    assertTrue(QueryResult!!.contains(ExpectedSynonym))
                    if (!QueryResult!!.contains(ExpectedSynonym)) {
                        println("ExpectedSynonym = $ExpectedSynonym")
                        println("CID = " + SDict.GetCanonicalID(ExpectedSynonym))
                        println("SList[$i] = " + SList[i])
                        var CIDs = SList[i].map { SDict.GetCanonicalID(it) }
                        println("CIDs = $CIDs")
                        println("QueryResult = $QueryResult")
                        CIDs = QueryResult.map { SDict.GetCanonicalID(it) }
                        println("CIDs = $CIDs")
                        throw AssertionError()
                    }
                }
                assertEquals(QueryResult!!.size, SList[i].size)
            }
        }
    }
}