package com.marquistech.quickautomationlite.helpers.core

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import com.marquistech.quickautomationlite.core.Action

open class Helper {

    val context: Context
    val uiDevice: UiDevice
    var tag: String

    init {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        uiDevice = UiDevice.getInstance(instrumentation)
        tag = javaClass.simpleName
    }


    fun pressHome(): Boolean {
        return uiDevice.pressHome()
    }

    fun pressRecentApps(): Boolean {
        return uiDevice.pressRecentApps()
    }

    fun pressBack(): Boolean {
        return uiDevice.pressBack()
    }

    open fun performLaunchPackage(
        packageName: String,
        launcher: Boolean
    ): Boolean {
        return true
    }

    open fun performClick(
        bySelector: BySelector
    ): Boolean {
        return true
    }

    open fun performSwitchOn(
        action: Action.SwitchOn
    ): Boolean {
        return true
    }


    open fun performSwitchOff(
        action: Action.SwitchOFF
    ): Boolean {
        return true
    }


}

