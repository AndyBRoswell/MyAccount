package top.abr.myaccount

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import java.security.SecureRandom

class SynonymDictionaryUnitTest {
    val RandomSource = SecureRandom.getInstanceStrong()

    @Test fun InsertTest() {
        val MAX_SYNONYM_GROUP_COUNT = 20L
        val MIN_SYNONYM_GROUP_COUNT = 10L
        val MAX_SYNONYM_COUNT = 20L
        val MIN_SYNONYM_COUNT = 10L

        val SynonymGroupCount = RandomSource.longs(1, MIN_SYNONYM_GROUP_COUNT, MAX_SYNONYM_GROUP_COUNT).iterator().next()
        val SynonymCounts= RandomSource.longs(SynonymGroupCount, MIN_SYNONYM_COUNT, MAX_SYNONYM_COUNT)

        val SDict = SynonymDictionary()
        
    }
}