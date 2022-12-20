package com.marquistech.quickautomationlite


import WifiEnbDsb
import com.marquistech.quickautomationlite.testcases.Messages.*
import com.marquistech.quickautomationlite.testcases.OpenCloseStoreFrontTest
import com.marquistech.quickautomationlite.testcases.OpenStoreFrontAppInUnTest
import com.marquistech.quickautomationlite.testcases.SendEmailAttachment
import com.marquistech.quickautomationlite.testcases.WifiOnOff
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {
       // val test = OpenSmsTest()
        //val test = OpenSmsTest()
       // val test = OpenCloseStoreFrontTest()
       // val test = SendEmailAttachment()
        //val test = WifiEnbDsb()
        val  test = WifiOnOff()
      //  val test = WifiEnbDsb()
       //val test = OpenStoreFrontAppInUnTest()

        test.mainTest()
       /* val test2 = MmsSendvideoTest()
        test2.mainTest()
        val test3 = MmsSendLargeTextTest()
        test3.mainTest()*/
    }
}