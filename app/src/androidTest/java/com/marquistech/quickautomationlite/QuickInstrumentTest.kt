package com.marquistech.quickautomationlite


import com.marquistech.quickautomationlite.testcases.VoiceCallTestReceive
import com.marquistech.quickautomationlite.testcases.VoiceCallTestUsingDialer
import com.marquistech.quickautomationlite.testcases.VtCallTestUsingDialer
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
        val test = VoiceCallTestUsingDialer()
        test.mainTest()
    }
}