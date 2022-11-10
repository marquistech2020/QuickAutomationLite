package com.marquistech.quickautomationlite

import com.marquistech.quickautomationlite.testcases.MmsReceivedImageTest
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
        val test = MmsReceivedImageTest()
        test.mainTest()
    }
}