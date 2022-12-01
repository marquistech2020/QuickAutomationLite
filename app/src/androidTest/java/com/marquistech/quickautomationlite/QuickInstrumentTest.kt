package com.marquistech.quickautomationlite


import com.marquistech.quickautomationlite.testcases.Messages.*
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
        val test = OpenReadSentSmsTest()
        test.mainTest()
       /* val test2 = MmsSendvideoTest()
        test2.mainTest()*/
    }
}