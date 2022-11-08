package com.marquistech.quickautomationlite

import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.testcases.VtCallTestUsingDialer
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
        val test = VtCallTestUsingDialer()
        test.mainTest()
    }
}