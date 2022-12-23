package com.marquistech.quickautomationlite


import android.util.Log
import com.marquistech.quickautomationlite.testcases.Messages.*
import org.junit.Test


class QuickInstrumentTest {
/*    @Rule
    var collector = ErrorCollector()*/

    @Test
    fun testShow() {

        try{
            val test = MmsSendAudioTest()
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