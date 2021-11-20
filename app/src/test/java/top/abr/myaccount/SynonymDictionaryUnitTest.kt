package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Assert.*
import org.junit.Test
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
        val MAX_SYNONYM_COUNT = 20
        val MIN_SYNONYM_COUNT = 10
        val MAX_WORD_LENGTH = 1
        val MIN_WORD_LENGTH = 1
        // MAX_SYNONYM_COUNT must be less than (C ^ MIN_WORD_LENGTH), should be MUCH LESS THAN (C ^ MIN_WORD_LENGTH), C = 52 when randomAlphabetic() is used
        assertTrue(MAX_SYNONYM_COUNT < 52.0.pow(MIN_WORD_LENGTH.toDouble()))
        val SynonymGroupCount = NextIntRInclusive(MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT)
        val SynonymCounts = RandomSource.ints(SynonymGroupCount.toLong(), MIN_SYNONYM_COUNT, MAX_SYNONYM_COUNT).toArray()

        // Round 1: Insert(Word: String, Synonym: String)
        val SDict = SynonymDictionary()
        val SSets = ArrayList<HashSet<String>>()
        val SList = ArrayList<ArrayList<String>>()
        val TestCount = ArrayList<ArrayList<Int>>()
        val UncoveredSynonymCount = ArrayList<Int>()
        // Prepare test data
        for (SynonymCount in SynonymCounts) {
            val SSet = HashSet<String>()
            while (SSet.size < SynonymCount) SSet.add(RandomStringRInclusive(MIN_WORD_LENGTH, MAX_WORD_LENGTH))
            SSets.add(SSet)
            val CurrentSList = ArrayList<String>(SSet)
            SList.add(CurrentSList)
            UncoveredSynonymCount.add(CurrentSList.size)
            TestCount.add(ArrayList<Int>().apply { for (i in 1..CurrentSList.size) add(0) })
        }
        // Start to insert
        for (i in SList.indices) {
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
        
    }
}