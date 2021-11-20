package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import java.security.SecureRandom

class SynonymDictionaryUnitTest {
    val RandomSource = SecureRandom.getInstanceStrong()

    @Test fun InsertTest() {
        // Range parameters of random data
        val MAX_SYNONYM_GROUP_COUNT = 20L
        val MIN_SYNONYM_GROUP_COUNT = 10L
        val MAX_SYNONYM_COUNT = 20
        val MIN_SYNONYM_COUNT = 10
        val MAX_WORD_LENGTH = 32
        val MIN_WORD_LENGTH = 1

        val SynonymGroupCount = RandomSource.longs(1, MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT).iterator().next()
        val SynonymCounts = RandomSource.ints(SynonymGroupCount, MIN_SYNONYM_COUNT, MAX_SYNONYM_COUNT)

        // Round 1
        val SDict1 = SynonymDictionary()
        val SLists = ArrayList<ArrayList<String>>()
        for (SynonymCount in SynonymCounts) {
            val SList = arrayListOf(RandomStringUtils.randomAlphabetic(MIN_WORD_LENGTH, MAX_WORD_LENGTH))
            for (i in 1..SynonymCount) {
                SList.add(RandomStringUtils.randomAlphabetic(MIN_WORD_LENGTH, MAX_WORD_LENGTH))
                SDict1.Insert(SList[0], SList[i])
            }
            SLists.add(SList)
        }
        
    }
}