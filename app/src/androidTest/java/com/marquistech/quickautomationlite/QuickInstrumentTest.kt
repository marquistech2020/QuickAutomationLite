package com.marquistech.quickautomationlite

import com.marquistech.quickautomationlite.testcases.WifiOnOff
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
        // val test = VtCallTestUsingDialer()
        //val test = WifiEnbDsb()
        val test = WifiOnOff()
        test.mainTest()
    }
}