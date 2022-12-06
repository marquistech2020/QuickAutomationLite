package com.marquistech.quickautomationlite


import com.marquistech.quickautomationlite.testcases.MainScrenAppTest
import com.marquistech.quickautomationlite.testcases.Messages.*
import com.marquistech.quickautomationlite.testcases.OpenStoreAppDownload
import com.marquistech.quickautomationlite.testcases.OpenStoreFrontAppInUnTest
import com.marquistech.quickautomationlite.testcases.SendEmailAttachment
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
       // val test = OpenSmsTest()
       // val test = OpenStoreFrontAppInUnTest()
       // val test =  OpenStoreAppDownload()
       // val test = SendEmailAttachment()
        val test = MainScrenAppTest()
        test.mainTest()
       /* val test2 = MmsSendvideoTest()
        test2.mainTest()
        val test3 = MmsSendLargeTextTest()
        test3.mainTest()*/
    }
}