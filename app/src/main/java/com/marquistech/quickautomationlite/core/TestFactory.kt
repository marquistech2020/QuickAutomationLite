package com.marquistech.quickautomationlite.core

import com.marquistech.quickautomationlite.testcases.wifi.WifiEnbDsb
import com.marquistech.quickautomationlite.core.TestFlow
import com.marquistech.quickautomationlite.testcases.email.OpenEmail
import com.marquistech.quickautomationlite.testcases.email.SendEmail
import com.marquistech.quickautomationlite.testcases.email.SendEmailAttachment
import com.marquistech.quickautomationlite.testcases.Messages.*
import com.marquistech.quickautomationlite.testcases.call.*
import com.marquistech.quickautomationlite.testcases.stability.BrowserStability
import com.marquistech.quickautomationlite.testcases.stability.MultiTaskingStability
import com.marquistech.quickautomationlite.testcases.storefront.*
import com.marquistech.quickautomationlite.testcases.wifi.WifiOnOff

class TestFactory {

    fun getTest(code: Int): TestFlow? = when (code) {
        100 -> VoiceCallTestReceive()
        101 -> VoiceCallTestUsingDialer()
        102 -> VoiceCallTestUsingPhonebook()
        103 -> VtCallTestReceive()
        104 -> VtCallTestReceiveWIFI()
        105 -> VtCallTestUsingDialer()
        106 -> VtCallTestUsingDialerWIFI()
        107 -> VtCallTestUsingPhonebook()
        108 -> DeleteMmSTest()
        109 -> DeleteSmsTest()
        110 -> MmsReceivedAudioTest()
        111 -> MmsReceivedImageTest()
        112 -> MmsReceivedVideoTest()
        113 -> MmsSendAudioTest()
        114 -> MmsSendImageTest()
        115 -> MmsSendLargeTextTest()
        116 -> MmsSendvideoTest()
        117 -> OpenReadSentMmsTest()
        118 -> OpenReadSentSmsTest()
        119 -> OpenSmsTest()
        120 -> SendSegmentSmsTest()
        121 -> BrowserStability()
        122 -> MultiTaskingStability()
        123 -> OpenCloseStoreFrontTest()
        124 -> OpenEmail()
        125 -> OpenStoreFrontAppInUnRealmeTest()
        126 -> OpenStoreFrontAppInUnTest()
        127 -> SendEmail()
        128 -> SendEmailAttachment()
        129 -> WifiEnbDsb()
        130 -> WifiOnOff()
        131 -> OpenStoreAppDownload()
        132 -> MainScrenAppTest()
        133 -> VoiceCallTestUsingHistory()

        else -> null
    }
}