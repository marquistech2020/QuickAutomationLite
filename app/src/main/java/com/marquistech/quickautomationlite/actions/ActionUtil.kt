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
        action: Action.SwitchOn
    ) {


        var uiObject: UiObject? = null
        var uiObject2: List<UiObject2>? = null
        var uiSelector: UiSelector? = null

        if (action.byClass.isNotEmpty()) {
            uiSelector = UiSelector().className(action.byClass)
        } else if (action.byPackage.isNotEmpty()) {
            uiSelector = UiSelector().packageName(action.byPackage)
        } else if (action.byText.isNotEmpty()) {
            uiSelector = UiSelector().text(action.byText)
        }

        uiSelector?.let {
            val uo = uiDevice.findObject(it)
            if (uo.exists()){
                uiObject = uo
            }
        }

        uiObject?.let { rootObject ->


            var requiredObject:UiObject? = null
            val len = rootObject.childCount - 1

            for (i in 0..len){
                uiSelector?.let {
                    if (i == action.position) {
                        requiredObject = rootObject.getChild(uiSelector)
                    }
                }
            }

            Log.e(TAG, " Switch button found  $requiredObject")
            requiredObject?.click()
            /*if (!requiredObject?.isChecked) {
                requiredObject?.click()
                Log.e(TAG, " Switching on ${requiredObject?.isChecked}")
            }*/

            wifiOnOffReport?.onTime = System.currentTimeMillis()
            wifiOnOffReport?.onStatus = requiredObject?.isChecked.toString()

        }


        if (uiObject == null) {

            if (action.byClass.isNotEmpty()) {
                uiObject2 = uiDevice.findObjects(By.clazz(action.byClass))
            } else if (action.byPackage.isNotEmpty()) {
                uiObject2 = uiDevice.findObjects(By.pkg(action.byPackage))
            } else if (action.byText.isNotEmpty()) {
                uiObject2 = uiDevice.findObjects(By.text(action.byText))
            }

        }


        uiObject2?.let { elementList ->

            var requiredObject: UiObject2? = null

            for (element in elementList) {

                if (elementList.indexOf(element) == action.position) {
                    requiredObject = element
                    break
                }
            }

            Log.e(TAG, " Switch button found  $requiredObject")

            if (requiredObject?.isChecked == false) {
                requiredObject?.click()
                Log.e(TAG, " Switching on ${requiredObject?.isChecked}")
            }

            wifiOnOffReport?.onTime = System.currentTimeMillis()
            wifiOnOffReport?.onStatus = requiredObject?.isChecked.toString()
        }

    }

    /**
     * perform switch OFF operation of objects
     */
    fun performSwitchOff(
        action: Action.SwitchOFF
    ) {

        var uiObject: UiObject? = null
        var uiObject2: List<UiObject2>? = null

        if (action.byClass.isNotEmpty()) {
            uiObject = uiDevice.findObject(UiSelector().className(action.byClass))
        } else if (action.byPackage.isNotEmpty()) {
            uiObject = uiDevice.findObject(UiSelector().packageName(action.byPackage))
        } else if (action.byText.isNotEmpty()) {
            uiObject = uiDevice.findObject(UiSelector().text(action.byText))
        }


        uiObject?.let { rootObject ->

            Log.e(TAG, " Using UiSelector")
            var requiredObject = rootObject
            /*val len = rootObject.childCount - 1

            for (i in 0..len){
                if (i == action.position) {
                    requiredObject = rootObject.getChild(i)
                    break
                }
            }*/

            Log.e(TAG, " Switch button found  $requiredObject")

            if (requiredObject.isChecked) {
                requiredObject.click()
                Log.e(TAG, " Switching on ${requiredObject.isChecked}")
            }

            wifiOnOffReport?.onTime = System.currentTimeMillis()
            wifiOnOffReport?.onStatus = requiredObject.isChecked.toString()

        }


        if (uiObject == null) {

            if (action.byClass.isNotEmpty()) {
                uiObject2 = uiDevice.findObjects(By.clazz(action.byClass))
            } else if (action.byPackage.isNotEmpty()) {
                uiObject2 = uiDevice.findObjects(By.pkg(action.byPackage))
            } else if (action.byText.isNotEmpty()) {
                uiObject2 = uiDevice.findObjects(By.text(action.byText))
            }

        }


        uiObject2?.let { elementList ->

            Log.e(TAG, " Using BySelector")

            var requiredObject: UiObject2? = null

            for (element in elementList) {

                if (elementList.indexOf(element) == action.position) {
                    requiredObject = element
                    break
                }
            }

            Log.e(TAG, " Switch button found  $requiredObject")

            if (requiredObject?.isChecked == false) {
                requiredObject.click()
                Log.e(TAG, " Switching off ${requiredObject.isChecked}")
            }

            wifiOnOffReport?.offTime = System.currentTimeMillis()
            wifiOnOffReport?.offStatus = if (requiredObject?.isChecked == false) "true" else "false"


    }
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