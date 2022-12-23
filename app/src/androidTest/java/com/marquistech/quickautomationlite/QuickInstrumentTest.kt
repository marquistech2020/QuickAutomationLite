package com.marquistech.quickautomationlite


import com.marquistech.quickautomationlite.testcases.call.VtCallTestUsingDialer
import com.marquistech.quickautomationlite.testcases.stability.BrowserStability
import com.marquistech.quickautomationlite.testcases.stability.MultiTaskingStability
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {

        val test = BrowserStability()
        test.mainTest()

    }
}