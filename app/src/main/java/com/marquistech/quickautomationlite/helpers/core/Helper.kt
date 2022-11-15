package com.marquistech.quickautomationlite.helpers.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.marquistech.quickautomationlite.core.AppSelector
import com.marquistech.quickautomationlite.core.Coordinate
import com.marquistech.quickautomationlite.core.EventType
import com.marquistech.quickautomationlite.core.Selector
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
                is AppSelector.ByUri->
                {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.apply {
                       // `package` = appSelector.pkgName

                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse(appSelector.uriName)
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

    open fun performClick(selector: Selector, position: Int, lgClick: Boolean): Boolean {
        return false
    }

    open fun closeApp(packageName: String): Boolean {
        return false
    }

    open fun drag(coordinate: Coordinate): Boolean {
        return false
    }

    open fun performGetText(selector: Selector, position: Int): String {
        return ""
    }

    open fun performSetText(selector: Selector, text: String): Boolean {
        return false
    }

    fun performSendEvent(type: EventType): Boolean {
        return when (type) {
            EventType.HOME ->
                uiDevice.pressHome()
            EventType.BACK -> uiDevice.pressBack()
            EventType.RECENT_APP -> uiDevice.pressRecentApps()
            EventType.ENTER -> uiDevice.pressEnter()
            EventType.RECEIVE_CALL -> uiDevice.pressKeyCode(KeyEvent.KEYCODE_CALL)
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
    open fun performListItemClick(selector: Selector, position: Int,itemClassname:String,itemSearch:String): Boolean {
        return false
    }
    open fun performListItemClickByIndex(selector: Selector, position: Int,itemClassname:String,itemSearchIndex:Int): Boolean {
        return false
    }
    open fun dateFormate(dateStr:String): Date? {
        val knownPatterns: MutableList<SimpleDateFormat> = ArrayList<SimpleDateFormat>()
        knownPatterns.add(SimpleDateFormat("MM/d/yy, HH:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/dd/yy, HH:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/d/yyyy, HH:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/d/yy, hh:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/d/yy, HH:mm "))
        knownPatterns.add(SimpleDateFormat("d/MM/yyyy, HH:mm"))
        knownPatterns.add(SimpleDateFormat("d/MM/yyyy, HH:mm a"))

        knownPatterns.add(SimpleDateFormat("d/MM/yyyy, hh:mm"))
        knownPatterns.add(SimpleDateFormat("dd/MM/yyyy, hh:mm"))
        knownPatterns.add(SimpleDateFormat("dd/MM/yyyy, hh:mm a"))
        knownPatterns.add(SimpleDateFormat("dd/MM/yyyy, HH:mm"))
        knownPatterns.add(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))
        knownPatterns.add(SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"))
        knownPatterns.add(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"))

        for (pattern in knownPatterns) {
            try {
                // Take a try
                return Date(pattern.parse(dateStr).getTime())
            } catch (pe: ParseException) {
                // Loop on
            }
        }
        System.err.println("No known Date format found: $dateStr")

        return null
    }

}

