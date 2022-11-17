package com.marquistech.quickautomationlite

import WifiEnbDsb
import com.marquistech.quickautomationlite.testcases.*
import org.junit.Test

class QuickInstrumentTest {

    @Test
    fun testShow() {

      //val test = OpenStoreFrontAppInUnTest()
      // val test =SendEmailAttachment()
        //val test = SendEmail()
       //val test = WifiOnOff()
        val test = WifiEnbDsb()

        //val  test = OpenCloseStoreFrontTest()
        //val test = OpenStoreFrontAppInUnRealmeTest()


        test.mainTest()
    }
}