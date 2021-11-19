package top.abr.myaccount

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class SynonymsDictionaryUnitTest {
    val RandomSource = Random()

    @Test fun InsertTest() {
        val SDict = SynonymsDictionary()
        assertEquals(0, SDict.GetSize())
        val InsertSingleSynonymTestCount = 1
        val InsertGroupOfSynonymsTestCount = 1
        
    }
}