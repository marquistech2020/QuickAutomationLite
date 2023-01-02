package com.marquistech.quickautomationlite


import android.util.Log
import com.marquistech.quickautomationlite.testcases.Messages.*
import com.marquistech.quickautomationlite.testcases.WifiOnOff
import org.junit.Test


class QuickInstrumentTest {
/*    @Rule
    var collector = ErrorCollector()*/

    @Test
    fun testShow() {

        try {
            val test = MmsSendLargeTextTest()
            test.mainTest()

            Log.e("TestCaseCount", "Error Catch ")
        } catch (e: Exception) {
            Log.e("TestCaseCount", "Error Catch ")
        }

    }

}