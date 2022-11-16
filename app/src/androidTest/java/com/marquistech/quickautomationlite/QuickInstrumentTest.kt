package com.marquistech.quickautomationlite

import com.marquistech.quickautomationlite.testcases.VtCallTestReceive
import com.marquistech.quickautomationlite.testcases.VtCallTestReceiveWIFI
import com.marquistech.quickautomationlite.testcases.VtCallTestUsingPhonebook
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
        val test = VtCallTestUsingPhonebook()
        test.mainTest()
    }
}