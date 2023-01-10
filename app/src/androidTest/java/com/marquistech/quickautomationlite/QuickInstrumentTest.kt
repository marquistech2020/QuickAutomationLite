package com.marquistech.quickautomationlite


import androidx.test.platform.app.InstrumentationRegistry
import com.marquistech.quickautomationlite.core.TestFactory
import org.junit.Test


class QuickInstrumentTest {

    @Test
    fun testShow() {

        val code = InstrumentationRegistry.getArguments().getString("testCode", "103").toInt()

        val factory = TestFactory()

        val test = factory.getTest(code)
        test?.mainTest()

    }

}