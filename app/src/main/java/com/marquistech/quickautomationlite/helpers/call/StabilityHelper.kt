package com.marquistech.quickautomationlite.helpers.call

import android.annotation.SuppressLint
import android.content.Context
import android.telecom.TelecomManager
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.marquistech.quickautomationlite.data.AdbCommand
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog

class StabilityHelper : CallHelper() {


    override fun performSwitchApp(loop: Int, endToPackage: String): Boolean {


        val uiSelector = UiSelector().className("android.widget.ListView")

        val uiObject = uiDevice.findObject(uiSelector)


        var isSwitched = false

        if (uiObject.exists()) {

            val childCount = uiObject.childCount

            val noOfCycle = if (loop > childCount) {
                loop / uiObject.childCount
            } else {
                1
            }


            writeLog(tag, "total cycle $noOfCycle")

            (1..noOfCycle).forEach {
                isSwitched = if (it == noOfCycle) {
                    switchAppCycle(childCount, it, endToPackage)
                } else {
                    switchAppCycle(childCount, it, "")
                }
            }

        }

        return isSwitched
    }


    private fun switchAppCycle(childCount: Int, cycle: Int, endToPackage: String): Boolean {

        writeLog(tag, "Cycle $cycle")

        var isSwitched = false

        val uiSelectorFr = UiSelector().className("android.widget.FrameLayout")

        val width = uiDevice.displayWidth
        val height = uiDevice.displayHeight
        var startSwitchPackageName = ""


        for (i in 1..childCount) {

            (1..i).forEach { count ->
                uiDevice.swipe(width / 2, height / 2, width, height / 2, 50)
                waitFor(1)
                //writeLog(tag, "count $count")
            }
            uiDevice.click(width / 2, height / 2)
            waitFor(3)

            val uiObjectPkg = uiDevice.findObject(uiSelectorFr)

            val currentPackage = if (uiObjectPkg.exists()) uiObjectPkg.packageName else ""

            writeLog(tag, "packageName $currentPackage")
            if (currentPackage == endToPackage) {
                break
            }else if (startSwitchPackageName.isEmpty()){
                startSwitchPackageName = currentPackage
            }else if (startSwitchPackageName.isNotEmpty() && startSwitchPackageName != currentPackage){
                isSwitched = true
            }

            uiDevice.pressRecentApps()
            waitFor(2)
        }

        return isSwitched
    }


    override fun closeApp(packageName: String): Boolean {

        val width = uiDevice.displayWidth
        val height = uiDevice.displayHeight

        val appSelectorUI = UiSelector().packageName(packageName)

        val isChrome = uiDevice.findObject(appSelectorUI).exists()

        uiDevice.pressRecentApps()
        waitFor(1)

        val uiSelector = UiSelector().className("android.widget.ListView")


        if (isChrome) {
            val uiObject = uiDevice.findObject(uiSelector)
            if (uiObject.exists()) {
                val childItem = uiObject.getChild(UiSelector().clickable(true))
                if (childItem.exists()) {
                    //uiDevice.swipe(542, 1005, 542, 157, 30)
                    uiDevice.swipe(width / 2, height / 2, width / 2, 0, 50)
                    waitFor(1)
                    uiDevice.click(width / 2, height / 2)
                }

            }

        }


        return uiDevice.findObject(appSelectorUI).exists().not()


    }



}