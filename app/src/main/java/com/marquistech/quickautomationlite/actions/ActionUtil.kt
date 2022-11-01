package com.marquistech.quickautomationlite.actions

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.uiautomator.*
import com.marquistech.quickautomationlite.reports.WifiOnOffReport
import javax.inject.Singleton


@Singleton
class ActionUtil(private val uiDevice: UiDevice) {

    companion object {
        const val TAG = "ActionUtil"
        private const val LAUNCH_TIMEOUT = 500L
    }

    /**
     * Clear all recent apps from history
     */
    fun clearRecent() {

        uiDevice.pressRecentApps()

        uiDevice.wait(
            Until.hasObject(By.pkg("com.android.launcher").depth(0)),
            LAUNCH_TIMEOUT
        )

        val uiSelector = UiSelector().className("android.widget.ListView")

        val lv = uiDevice.findObject(uiSelector)


        lv?.let { list ->


            while (list.getChild(UiSelector().className("android.widget.FrameLayout"))
                    .exists()
            ) {
                val swipe = uiDevice.swipe(542, 1005, 542, 157, 50)
                Log.e(TAG, "Swipe = $swipe ")
            }

        }

    }

    fun hasChildren(childCount: Int): UiObject2Condition<Boolean> {
        return object : UiObject2Condition<Boolean>() {
            fun apply(`object`: UiObject2): Boolean {
                return `object`.childCount == childCount
            }
        }
    }


    /**
     * Launch app using unique package name
     */
    fun performLaunchPackage(context: Context?, packageName: String, launcher: Boolean) {

        if (launcher) {
            val intent = context?.packageManager?.getLaunchIntentForPackage(packageName)
            context?.startActivity(intent)
        } else {
            val intent = Intent(packageName).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            context?.startActivity(intent)
        }


        uiDevice.wait(
            Until.hasObject(By.pkg(packageName).depth(0)),
            LAUNCH_TIMEOUT
        )

    }


    /**
     * perform click operation of objects
     */
    fun performClick(
        bySelector: BySelector
    ) {

        val uiObject2 = uiDevice.findObject(bySelector)

        uiObject2?.click()
        Log.e(TAG, " isClick ")

    }

    /**
     * perform switch ON operation of objects
     */
    fun performSwitchOn(
        bySelector: BySelector,
        position: Int
    ) {

        var uiObject2: UiObject2? = null

        val elementList = uiDevice.findObjects(bySelector)

        for (element in elementList) {

            if (elementList.indexOf(element) == position) {
                uiObject2 = element
            }
        }

        Log.e(TAG, " Switch button found  $uiObject2")

        if (uiObject2?.isChecked == false) {
            uiObject2.click()
            Log.e(TAG, " Switching on ${uiObject2.isChecked}")
        }
        wifiOnOffReport?.onTime = System.currentTimeMillis()
        wifiOnOffReport?.onStatus = uiObject2?.isChecked.toString()
    }

    /**
     * perform switch OFF operation of objects
     */
    fun performSwitchOff(
        bySelector: BySelector,
        position: Int
    ) {

        var uiObject2: UiObject2? = null

        var elementList = uiDevice.findObjects(bySelector)

        for (element in elementList) {

            if (elementList.indexOf(element) == position) {
                uiObject2 = element
            }
        }


        Log.e(TAG, " Switch button found  $uiObject2")

        if (uiObject2?.isChecked == true) {
            uiObject2.click()
            Log.e(TAG, " Switching off ${uiObject2.isChecked}")
        }
        wifiOnOffReport?.offTime = System.currentTimeMillis()
        wifiOnOffReport?.offStatus = if (uiObject2?.isChecked == false) "true" else "false"

    }

    fun pressHomeButton() {
        val isPressed = uiDevice.pressHome()
    }

    var wifiOnOffReport: WifiOnOffReport? = null

    fun setReport(report: WifiOnOffReport) {
        wifiOnOffReport = report
    }

    fun getReport(): WifiOnOffReport? {
        return wifiOnOffReport
    }


}