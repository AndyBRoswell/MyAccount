package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Assert.*
import org.junit.Test
import java.lang.AssertionError
import java.security.SecureRandom
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.max
import kotlin.math.pow

class SynonymDictionaryUnitTest {
    private val RandomSource = SecureRandom.getInstanceStrong()

    private fun NextInt(Min: Int, Max: Int) = RandomSource.ints(1, Min, Max).iterator().next()
    private fun NextIntRClosed(Min: Int, Max: Int) = RandomSource.ints(1, Min, Max + 1).iterator().next()
    private fun RandomStringRClosed(Lmin: Int, Lmax: Int) = RandomStringUtils.randomAscii(Lmin, Lmax + 1)
    private fun RandomIntArrayRClosed(Length: Long, Min: Int, Max: Int) = RandomSource.ints(Length, Min, Max + 1).toArray()

    @Test fun InsertionAndDeletion() {
        // Range parameters of random data
        val MAX_SYNONYM_GROUP_COUNT = 20
        val MIN_SYNONYM_GROUP_COUNT = 10
        val MAX_SYNONYM_GROUP_SIZE = 20
        val MIN_SYNONYM_GROUP_SIZE = 10
        val MAX_WORD_LENGTH = 2
        val MIN_WORD_LENGTH = 1
        // MAX_SYNONYM_GROUP_SIZE * MAX_SYNONYM_GROUP_COUNT must be less than pow(C, MAX_WORD_LENGTH), should be MUCH LESS THAN pow(C, MAX_WORD_LENGTH)
        // C = 10 when randomNumeric() is used; C = 52 when randomAlphabetic() is used
        // C = 62 when randomAlphanumeric() is used; C = 95 when randomAscii() is used
        val MAX_ALL_SYNONYMS_COUNT = 95.0.pow(MAX_WORD_LENGTH.toDouble()).toInt()
        assertTrue(MAX_SYNONYM_GROUP_SIZE * MAX_SYNONYM_GROUP_COUNT < MAX_ALL_SYNONYMS_COUNT)
        for (RepeatCount in 1..10) { // Repeat Count of this test
            val SynonymGroupCount = NextIntRClosed(MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT)
            val SynonymCount = RandomIntArrayRClosed(SynonymGroupCount.toLong(), MIN_SYNONYM_GROUP_SIZE, MAX_SYNONYM_GROUP_SIZE)

            // Round 1: Insert(Word: String, Synonym: String); Delete(Word: String, Synonym: String)
            val SDict = SynonymDictionary()
            val SynonymsForTest = HashSet<String>()
            val SList = ArrayList<ArrayList<String>>()
            // Prepare test data
            for (i in SynonymCount.indices) {
                SList.add(ArrayList())
                while (SList[i].size < SynonymCount[i]) {
                    val Synonym = RandomStringRClosed(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
                    if (SynonymsForTest.add(Synonym)) SList[i].add(Synonym)             // Each synonym which will be added for tests is different from others
                }
            }
            // Start to insert
            for (i in SList.indices) { // Every synonym in SList is guaranteed to be the arg of SynonymDictionary.insert() at least once.
//            println("SList[i].size = ${SList.size}")
                if (SList[i].size > 1) {
                    val k = max((0.1 * SList[i].size).toLong(), 2L)
                    val Interval = ArrayList<Pair<Int, Int>>()
                    val REndTreeSet = TreeSet<Int>()
                    while (REndTreeSet.size < k - 1) REndTreeSet.add(NextInt(0, SList[i].size - 1))
                    val REnd = REndTreeSet.toIntArray()
//                println("REnd = $REnd")
                    Interval.apply { // generate k closed intervals, k >= 2
                        add(Pair(0, REnd[0]))
                        for (j in 0 until (k - 3).toInt()) add(Pair(REnd[j] + 1, REnd[j + 1]))
                        add(Pair(REnd[(k - 2).toInt()] + 1, SList[i].size - 1))
                    }
//                println("Interval = $Interval")
                    for (CurrentInterval in Interval) { // Usual insertion test
                        for (j in 0 until CurrentInterval.second) SDict.Insert(SList[i][j], SList[i][j + 1])
                    }
                    for (j in 0 until Interval.size - 1) { // Merge test
                        val WordIndex = NextIntRClosed(Interval[j].first, Interval[j].second)
                        val SynonymIndex = NextIntRClosed(Interval[j + 1].first, Interval[j + 1].second)
//                    println("[WordIndex, SynonymIndex] = [$WordIndex, $SynonymIndex]")
                        SDict.Insert(SList[i][WordIndex], SList[i][SynonymIndex])
                    }
                }
                else SDict.Insert(SList[i][0], SList[i][0])
            }
            // Start to verify
            for (i in SList.indices) {
                for (QuerySynonym in SList[i]) {
                    val QueryResult = SDict.GetSynonyms(QuerySynonym)
                    for (ExpectedSynonym in SList[i]) {
                        assertTrue(QueryResult!!.contains(ExpectedSynonym))
//                    if (!QueryResult!!.contains(ExpectedSynonym)) {
//                        println("ExpectedSynonym = $ExpectedSynonym")
//                        println("CID = " + SDict.GetCanonicalID(ExpectedSynonym))
//                        println("SList[$i] = ${SList[i]}")
//                        var CIDs = SList[i].map { SDict.GetCanonicalID(it) }
//                        println("CIDs = $CIDs")
//                        println("QueryResult = $QueryResult")
//                        CIDs = QueryResult.map { SDict.GetCanonicalID(it) }
//                        println("CIDs = $CIDs")
//                        println("================================================================")
//                        println("SList = $SList")
//                        println("AllSynonyms() = ${SDict.AllSynonyms()}")
//                        throw AssertionError()
//                    }
                    }
                    assertEquals(QueryResult!!.size, SList[i].size)
                }
            }
            // Start to delete and verify
            for (i in SList.indices) {
                val CID = SDict.GetCanonicalID(SList[i][0])
                for (j in SList[i].size - 1 downTo 1) {
                    assertEquals(SDict.Delete(SList[i][0], SList[i][j]), 0)
                    val QueryResult = SDict.GetSynonyms(SList[i][0])
                    for (ExpectedSynonym in SList[i]) {
                        assertTrue(QueryResult!!.contains(ExpectedSynonym))
                    }
                    assertEquals(QueryResult!!.size, SList[i].size)
                    SList[i].removeAt(j)
                }
                assertEquals(SDict.Delete(SList[i][0], SList[i][0]), CID)
                assertNull(SDict.GetSynonyms(SList[i][0]))
                SList[i].removeAt(0)
            }
        }
    }
}