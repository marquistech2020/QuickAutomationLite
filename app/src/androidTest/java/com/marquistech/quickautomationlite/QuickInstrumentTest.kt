package com.marquistech.quickautomationlite


import android.util.Log
import com.marquistech.quickautomationlite.testcases.Messages.DeleteMmSTest
import com.marquistech.quickautomationlite.testcases.Messages.MmsSendLargeTextTest
import com.marquistech.quickautomationlite.testcases.Messages.SendSegmentSmsTest
import org.junit.Test


class QuickInstrumentTest {
/*    @Rule
    var collector = ErrorCollector()*/

    @Test
    fun testShow() {

        try{
            val test = SendSegmentSmsTest()
            test.mainTest()

            Log.e("TestCaseCount","Error Catch ")
        }catch (e:Exception){
        Log.e("TestCaseCount","Error Catch ")
    }

       /* val test2 = MmsSendvideoTest()
        test2.mainTest()
        val test3 = MmsSendLargeTextTest()
        test3.mainTest()*/
    }

}