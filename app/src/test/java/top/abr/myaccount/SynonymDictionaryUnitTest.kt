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
    private fun RandomAlphabeticStringRClosed(Lmin: Int, Lmax: Int) = RandomStringUtils.randomAlphabetic(Lmin, Lmax + 1)
    private fun RandomASCIIRClosed(Lmin: Int, Lmax: Int) = RandomStringUtils.randomAscii(Lmin, Lmax + 1)
    private fun RandomIntArray(Length: Long, Min: Int, Max: Int) = RandomSource.ints(Length, Min, Max).toArray()
    private fun RandomIntArrayRClosed(Length: Long, Min: Int, Max: Int) = RandomSource.ints(Length, Min, Max + 1).toArray()
    private fun NextIntInterval(Min: Int, Max: Int): Pair<Int, Int> {
        val End = RandomIntArray(2, Min, Max)
        if (End[0] > End[1]) End[0] = End[1].also { End[1] = End[0] }   // swap and ensure End[0] <= End[1]
        return Pair(End[0], End[1])
    }

    // Range parameters of random data
    val REPETITION_COUNT = 10
    val MAX_SYNONYM_GROUP_COUNT = 1024
    val MIN_SYNONYM_GROUP_COUNT = 1024
    val MAX_SYNONYM_GROUP_SIZE = 1024
    val MIN_SYNONYM_GROUP_SIZE = 1024
    val MAX_WORD_LENGTH = 256
    val MIN_WORD_LENGTH = 256
    // MAX_SYNONYM_GROUP_SIZE * MAX_SYNONYM_GROUP_COUNT must be less than pow(C, MAX_WORD_LENGTH), should be MUCH LESS THAN pow(C, MAX_WORD_LENGTH)
    // C = 10 when randomNumeric() is used; C = 52 when randomAlphabetic() is used
    // C = 62 when randomAlphanumeric() is used; C = 95 when randomAscii() is used
    val MAX_ALL_SYNONYMS_COUNT = 95.0.pow(MAX_WORD_LENGTH.toDouble()).toInt()

    // Shared test data
    val SDict = SynonymDictionary()
    val SynonymsForTest = HashSet<String>()
    val SList = ArrayList<ArrayList<String>>()

    fun PrepareTestData() {
        SDict.DeleteAll()
        SynonymsForTest.clear()
        SList.clear()
        val SynonymGroupCount = NextIntRClosed(MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT)
        val SynonymCount = RandomIntArrayRClosed(SynonymGroupCount.toLong(), MIN_SYNONYM_GROUP_SIZE, MAX_SYNONYM_GROUP_SIZE)
        for (i in SynonymCount.indices) {
            SList.add(ArrayList())
            while (SList[i].size < SynonymCount[i]) {
                val Synonym = RandomASCIIRClosed(MIN_WORD_LENGTH, MAX_WORD_LENGTH)
                if (SynonymsForTest.add(Synonym)) SList[i].add(Synonym)             // Each synonym which will be added for tests is different from others
            }
        }
    }

    fun VerifyInsertion() {
        for (i in SList.indices) {
            for (QuerySynonym in SList[i]) {
                val QueryResult = SDict.GetSynonyms(QuerySynonym)
                for (ExpectedSynonym in SList[i]) {
//                        assertTrue(QueryResult!!.contains(ExpectedSynonym))
                    if (!QueryResult!!.contains(ExpectedSynonym)) {
                        println("ExpectedSynonym = $ExpectedSynonym")
                        println("================================")
                        println("SList[$i] (size = ${SList[i].size}) = ${SList[i]}")
                        println("================================")
                        println("CIDs = " + SList[i].map { SDict.GetCanonicalID(it) })
                        println("================================")
                        println("QueryResult (size = ${QueryResult.size}) = $QueryResult")
                        println("================================")
                        println("CIDs = " + QueryResult.map { SDict.GetCanonicalID(it) })
                        throw AssertionError()
                    }
                }
                assertEquals(QueryResult!!.size, SList[i].size)
            }
        }
    }
    //  ================================================================
    @Test fun InsertionAndDeletion() {
        assertTrue(MAX_SYNONYM_GROUP_SIZE * MAX_SYNONYM_GROUP_COUNT < MAX_ALL_SYNONYMS_COUNT)
        for (CurrentCount in 1..REPETITION_COUNT) { // Repeat Count of this test
            PrepareTestData()
            // Start to insert
            for (i in SList.indices) { // Every synonym in SList is guaranteed to be the arg of SynonymDictionary.insert() at least once.
                if (SList[i].size > 1) {
                    val K = max((0.1 * SList[i].size).toInt(), 2)   // K >= 2
                    val Interval = ArrayList<Pair<Int, Int>>()
                    val REndSet = TreeSet<Int>()                        // Each right end should be unique
                    while (REndSet.size < K - 1) REndSet.add(NextInt(0, SList[i].size - 1))
                    val REnd = REndSet.toIntArray()
                    Interval.apply { // Generate k closed intervals, K >= 2
                        add(Pair(0, REnd[0]))
                        for (j in 0..(K - 3)) add(Pair(REnd[j] + 1, REnd[j + 1]))
                        add(Pair(REnd[(K - 2)] + 1, SList[i].size - 1))
                    }
                    for (CurrentInterval in Interval) { // Usual insertion test
                        for (j in 0 until CurrentInterval.second) SDict.Insert(SList[i][j], SList[i][j + 1], true)
                    }
                    for (j in 0 until Interval.size - 1) { // Merge test
                        val WordIndex = NextIntRClosed(Interval[j].first, Interval[j].second)
                        val SynonymIndex = NextIntRClosed(Interval[j + 1].first, Interval[j + 1].second)
                        SDict.Insert(SList[i][WordIndex], SList[i][SynonymIndex], true)
                    }
                }
                else SDict.Insert(SList[i][0], SList[i][0], true)
            }
            VerifyInsertion()
            // Start to delete and verify
            for (i in SList.indices) {
                val CID = SDict.GetCanonicalID(SList[i][0])
                for (j in SList[i].size - 1 downTo 1) {
                    assertEquals(SDict.Delete(SList[i][0], SList[i][j]), 0)
                    SList[i].removeAt(j)
                    val QueryResult = SDict.GetSynonyms(SList[i][0])!!
                    for (ExpectedSynonym in SList[i]) assertTrue(QueryResult.contains(ExpectedSynonym))
                    assertEquals(QueryResult.size, SList[i].size)
                }
                assertEquals(SDict.Delete(SList[i][0], SList[i][0]), CID)
                assertNull(SDict.GetSynonyms(SList[i][0]))
                SList[i].removeAt(0)
            }
        }
    }

    @Test fun GroupInsertionAndDeletion() {
        assertTrue(MAX_SYNONYM_GROUP_SIZE * MAX_SYNONYM_GROUP_COUNT < MAX_ALL_SYNONYMS_COUNT)
        for (CurrentCount in 1..REPETITION_COUNT) {
            PrepareTestData()
            // Start to insert and verify
            for (i in SList.indices) {
                if (SList[i].size > 1) {
                    val K = max((0.1 * SList[i].size).toInt(), 2)   // K >= 2
                    val Interval = ArrayList<Pair<Int, Int>>()
                    val REndSet = TreeSet<Int>()                        // Each right end should be unique
                    while (REndSet.size < K - 1) REndSet.add(NextInt(0, SList[i].size - 1))
                    val REnd = REndSet.toIntArray()
                    Interval.apply { // Generate K closed intervals, K >= 2
                        add(Pair(0, REnd[0]))
                        for (j in 0..K - 3) add(Pair(REnd[j] + 1, REnd[j + 1]))
                        add(Pair(REnd[(K - 2)] + 1, SList[i].size - 1))
                    }
                    for (CurrentInterval in Interval) { // Usual insertion test
                        SDict.Insert(SList[i][CurrentInterval.first], SList[i].subList(CurrentInterval.first, CurrentInterval.second + 1), true)
                    }
                    val K0 = NextInt(0, K)
                    val CID_K0 = SDict.GetCanonicalID(SList[i][Interval[K0].second])
                    for (j in Interval[K0].first until Interval[K0].second) assertEquals(SDict.GetCanonicalID(SList[i][j]), CID_K0)
                    for (j in Interval.indices) { // Merge test
                        if (j == K0) { // Should not perform any addition cause every synonym in this interval are already in this dict
                            SDict.Insert(SList[i][Interval[j].first], SList[i].subList(Interval[j].first, Interval[j].second + 1), true)
                        }
                        else { // Check situations where merge is disabled or enabled respectively
                            SDict.Insert(SList[i][Interval[K0].first], SList[i].subList(Interval[j].first, Interval[j].second + 1), false)
                            val CID_j = SDict.GetCanonicalID(SList[i][Interval[j].second])
                            for (k in Interval[j].first until Interval[j].second) assertEquals(SDict.GetCanonicalID(SList[i][k]), CID_j)
                            assertNotEquals(CID_j, CID_K0) // When merge is disabled, all the insertions above should be skipped.
                            SDict.Insert(SList[i][Interval[K0].first], SList[i].subList(Interval[j].first, Interval[j].second + 1), true)
                            // When merge is enabled, all the synonyms in Interval[i] should be in the same synonym group with all the synonyms in Interval[K0] after the insertions above.
                            for (k in Interval[j].first until Interval[j].second) assertEquals(SDict.GetCanonicalID(SList[i][k]), CID_K0)
                        }
                    }
                }
                else { // SList[i].size == 1
                    SDict.Insert(SList[i][0], SList[i][0], true)
                    val QueryResult = SDict.GetSynonyms(SList[i][0])
                    assertEquals(QueryResult!!.size, 1)
                    assertEquals(SList[i][0], QueryResult.iterator().next())
                }
                // Start to delete and verify
                for (i in SList.indices) {
                    val CID = SDict.GetCanonicalID(SList[i][0])!!
                    while (true) {
                        val LIndex = NextInt(0, SList[i].size)
                        val Ret = SDict.Delete(SList[i][0], SList[i].subList(LIndex, SList[i].size))    // Delete several synonyms of the word SList[i][0]
                        while (SList[i].size > LIndex) SList[i].removeLast()                            // Sync the deletion to the test data
                        // SynonymDictionary.Delete() should return the original canonical ID of this synonym group for other possible essential usages if all synonyms in this group are deleted.
                        if (SList[i].size > 0) {
                            assertEquals(Ret, SynonymDictionary.NO_ID)
                            val QueryResult = SDict.GetSynonyms(SList[i][0])!!
                            for (ExpectedSynonym in SList[i]) assertTrue(QueryResult.contains(ExpectedSynonym))
                            assertEquals(SList[i].size, QueryResult.size)
                        }
                        else {
                            assertEquals(Ret, CID)
                            assertEquals(SDict.GetSynonyms(CID), null)
                            break
                        }
                    }
                }
            }
        }
    }

    @Test fun GroupInsertionAndGroupDeletion2() {
        assertTrue(MAX_SYNONYM_GROUP_SIZE * MAX_SYNONYM_GROUP_COUNT < MAX_ALL_SYNONYMS_COUNT)
        for (CurrentCount in 1..REPETITION_COUNT) {
            PrepareTestData()
            // Start to insert
            for (i in SList.indices) {
                if (SList[i].size > 3) { // SList[i].size >= 4
                    val K = max((0.1 * SList[i].size).toInt(), 2)           // K >= 2
                    val L = NextIntRClosed(1, SList[i].size - K)    // 1 <= L <= SList[i].size - K
                    val Interval = ArrayList<Pair<Int, Int>>()
                    val REndSet = TreeSet<Int>()                                // Each right end should be unique
                    while (REndSet.size < K - 1) REndSet.add(NextInt(0, SList[i].size - L))
                    val REnd = REndSet.toIntArray()
                    Interval.apply { // Generate K closed intervals, K >= 2
                        add(Pair(0, REnd[0]))
                        for (j in 0..K - 3) add(Pair(REnd[j] + 1, REnd[j + 1]))
                        add(Pair(REnd[(K - 2)] + 1, SList[i].size - L))
                    }
                    for (CurrentInterval in Interval) { // Usual insertion test
                        SDict.Insert(SList[i].subList(CurrentInterval.first, CurrentInterval.second + 1))
//                        print("<${CurrentInterval.first}, ${CurrentInterval.second}> ")
                    }
//                    println()
                    for (j in 0 until Interval.size - 1) { // Merge test
                        val Left = NextIntRClosed(Interval[j].first, Interval[j].second)
                        val Right = NextIntRClosed(Interval[j + 1].first, Interval[j + 1].second)
                        SDict.Insert(SList[i].subList(Left, Right + 1), true)
//                        println("<$Left, $Right>")
                    }
                    SDict.Insert(SList[i].subList(SList[i].size - L, SList[i].size), true)
//                    println("<${SList[i].size - l}, ${SList[i].size - 1}>")
//                    println("K = $K, L = $L")
                }
                else SDict.Insert(SList[i], true)
            }
            VerifyInsertion()
            // Start to delete and verify
//            for (i in SList.indices) {
//                val CID = SDict.GetCanonicalID(SList[i][0])
//                while (SList[i].size > 0) {
//                    val Interval = NextIntInterval(0, SList[i].size)
//                    // TODO
//                }
//            }
        }
    }
}