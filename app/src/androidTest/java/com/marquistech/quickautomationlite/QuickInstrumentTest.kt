package com.marquistech.quickautomationlite

import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.testcases.VtCallTestUsingDialer
import com.marquistech.quickautomationlite.testcases.WifiEnbDsb
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