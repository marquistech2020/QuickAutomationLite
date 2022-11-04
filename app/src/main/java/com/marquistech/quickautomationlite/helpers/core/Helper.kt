package com.marquistech.quickautomationlite.helpers.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.marquistech.quickautomationlite.core.AppSelector
import com.marquistech.quickautomationlite.core.Coordinate
import com.marquistech.quickautomationlite.core.EventType
import com.marquistech.quickautomationlite.core.Selector

open class Helper {

    private val context: Context
    val uiDevice: UiDevice
    var tag: String

    init {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        uiDevice = UiDevice.getInstance(instrumentation)
        tag = javaClass.simpleName
    }


    fun launchApp(
        appSelector: AppSelector
    ): Boolean {

        try {

            when (appSelector) {
                is AppSelector.ByAction -> {
                    val intent = Intent(appSelector.actionName).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    context.startActivity(intent)
                }
                is AppSelector.ByPkg -> {
                    val intent = Intent()
                    intent.apply {
                        `package` = appSelector.pkgName
                        action = Intent.ACTION_MAIN
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
            }

            return true

        } catch (e: ActivityNotFoundException) {
            return false
        }
    }


    open fun waitFor(second: Int) {
        Thread.sleep((second * 1000).toLong())
    }

    open fun waitFor(milli: Long) {
        Thread.sleep(milli)
    }

    open fun waitDeviceForIdle(milli: Long) {
        uiDevice.waitForIdle(milli)
    }

    open fun clearRecentApps(): Boolean {
        return false
    }

    open fun performClick(selector: Selector, position: Int): Boolean {
        return false
    }

    open fun closeApp(packageName: String): Boolean {
        return false
    }

    open fun drag(coordinate: Coordinate): Boolean {
        return false
    }

    open fun performGetText(selector: Selector): String {
        return ""
    }

    open fun performSetText(selector: Selector, text: String): Boolean {
        return false
    }

    fun performSendEvent(type: EventType): Boolean {
        return when (type) {
            EventType.HOME -> uiDevice.pressHome()
            EventType.BACK -> uiDevice.pressBack()
            EventType.RECENT_APP -> uiDevice.pressRecentApps()
            EventType.ENTER -> uiDevice.pressEnter()
            EventType.SPACE -> uiDevice.pressKeyCode(KeyEvent.KEYCODE_SPACE)
        }
    }

    open fun performSwipe(coordinate: Coordinate, steps: Int): Boolean {
        return uiDevice.swipe(
            coordinate.startX,
            coordinate.startY,
            coordinate.endX,
            coordinate.endY,
            steps
        )
    }

    open fun performSwitch(selector: Selector): Boolean {
        return false
    }

}

