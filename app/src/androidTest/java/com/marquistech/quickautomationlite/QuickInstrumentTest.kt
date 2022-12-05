package com.marquistech.quickautomationlite


import com.marquistech.quickautomationlite.testcases.Messages.*
import com.marquistech.quickautomationlite.testcases.OpenStoreFrontAppInUnTest
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
       // val test = OpenSmsTest()
        val test = MmsSendvideoTest()
        test.mainTest()
       /* val test2 = MmsSendvideoTest()
        test2.mainTest()
        val test3 = MmsSendLargeTextTest()
        test3.mainTest()*/
    }
}