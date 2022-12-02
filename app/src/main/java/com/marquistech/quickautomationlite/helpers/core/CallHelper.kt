package com.marquistech.quickautomationlite.helpers.core

import android.graphics.Rect
import android.graphics.RectF
import androidx.test.uiautomator.*
import com.marquistech.quickautomationlite.callbacks.ResultCompleteCallback
import com.marquistech.quickautomationlite.core.Selector
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import kotlin.concurrent.thread

class CallHelper : Helper() {

    override fun clearRecentApps(): Boolean {


        val uiSelector = UiSelector().className("android.widget.ListView")

        val uiObject = uiDevice.findObject(uiSelector)

        var isClearAll = false

        if (uiObject.exists()) {
            val childItem = uiObject.getChild(UiSelector().clickable(true))
            while (childItem.exists() && childItem.childCount != 0) {
                uiDevice.swipe(542, 1005, 542, 157, 30)
            }

            isClearAll = childItem.exists().not()
        }

        return isClearAll

    }


    override fun performClick(selector: Selector, position: Int, isLongClick: Boolean): Boolean {
        return try {
            var uiSelector: UiSelector? = null


            when (selector) {
                is Selector.ByCls -> {
                    uiSelector = UiSelector().className(selector.clsName)
                }
                is Selector.ByPkg -> {
                    uiSelector = UiSelector().packageName(selector.pkgName)
                }
                is Selector.ByRes -> {
                    uiSelector = UiSelector().resourceId(selector.resName)
                }
                is Selector.ByText -> {
                    uiSelector = UiSelector().text(selector.text)
                }
                is Selector.ByContentDesc -> {
                    uiSelector = UiSelector().descriptionContains(selector.contentDesc)
                }
            }


            var isClicked = false

            var uiObject = uiDevice.findObject(uiSelector)

            var retryCount = 0

            while (uiObject.exists().not() && retryCount < 3){
                uiObject = uiDevice.findObject(uiSelector)
                retryCount += 1
                writeLog(tag, " retry $retryCount")
                waitDevice(2000)
            }

            writeLog(tag, " button clicked1 $isClicked element exist ${uiObject.exists()}")

            if (uiObject.exists()) {

                writeLog(tag, " button clicked2 $isClicked")

                isClicked =
                    if (uiObject.isClickable.not()) false else if (isLongClick) uiObject.longClick() else uiObject.click()

                writeLog(tag, " button clicked3 $isClicked")

                if (isClicked.not() && uiObject.childCount != 0) {
                    val btn = uiObject.getChild(UiSelector().clickable(true).index(position))
                    if (btn.exists()) {
                        isClicked =
                            if (btn.isClickable.not()) false else if (isLongClick) btn.longClick() else btn.click()

                        writeLog(tag, " button clicked4 $isClicked")
                    }
                }


                if (isClicked.not()) {

                    isClicked = uiDevice.performActionAndWait({
                        val bounds = uiObject.bounds
                        val x = bounds.left + 50
                        val y = bounds.top + 50
                        uiDevice.click(x, y)
                    }, Until.newWindow(), 5000)

                    writeLog(tag, " button clicked5 $isClicked")
                }


                /*if (isClicked.not()) {

                    val list = uiDevice.findObjects(bySelector).filter {
                        it.className == "android.widget.TextView" || it.className == "android.widget.Button"
                    }


                    isClicked = try {
                        var node = list[1]

                        writeLog(tag,"node ${node.className}")

                        while (node != null && node.isClickable.not()) {
                            node = node.parent
                        }

                    } catch (e: Exception) {
                        return false
                    }

                }*/

            }
            return isClicked
        } catch (e: Exception) {
            writeLog(tag, " Exception in performClick ${e.message}")
            false
        }
    }

    override fun performSetText(selector: Selector, text: String): Boolean {
        //uiDevice.executeShellCommand("input text $text")
        var reqStr = ""
        return try {
            var uiSelector: UiSelector? = null
            when (selector) {
                is Selector.ByCls -> {
                    uiSelector = UiSelector().className(selector.clsName)
                    reqStr = selector.clsName
                }
                is Selector.ByPkg -> {
                    uiSelector = UiSelector().packageName(selector.pkgName)
                    reqStr = selector.pkgName
                }
                is Selector.ByRes -> {
                    uiSelector = UiSelector().resourceId(selector.resName)
                    reqStr = selector.resName
                }
                is Selector.ByText -> {
                    uiSelector = UiSelector().text(selector.text)
                    reqStr = selector.text
                }
                else -> {

                }
            }

            var outputTextStatus = false

            uiSelector?.let {
                val uiObject = uiDevice.findObject(it)
                if (uiObject.exists()) {
                    outputTextStatus = if (uiObject.childCount == 0) {
                        uiObject.setText(text)
                    } else {
                        val ib = uiObject.getChild(
                            UiSelector().className("android.widget.EditText").index(0)
                        )
                        if (ib.exists()) ib.setText(text) else false
                    }
                }
            }

            outputTextStatus
        } catch (e: Exception) {
            false
        }
    }

    override fun performGetText(selector: Selector, position: Int): String {
        var reqStr = ""
        return try {
            var uiSelector: UiSelector? = null
            when (selector) {
                is Selector.ByCls -> {
                    uiSelector = UiSelector().className(selector.clsName)
                    reqStr = selector.clsName
                }
                is Selector.ByPkg -> {
                    uiSelector = UiSelector().packageName(selector.pkgName)
                    reqStr = selector.pkgName
                }
                is Selector.ByRes -> {
                    uiSelector = UiSelector().resourceId(selector.resName)
                    reqStr = selector.resName
                }
                is Selector.ByText -> {
                    uiSelector = UiSelector().text(selector.text)
                    reqStr = selector.text
                }
                else -> {

                }
            }

            var outputText = ""

            uiSelector?.let {
                val uiObject = uiDevice.findObject(it)
                if (uiObject.exists()) {
                    outputText = if (uiObject.childCount == 0) {
                        uiObject.text
                    } else {
                        val ib = uiObject.getChild(
                            UiSelector().className("android.widget.ImageButton").index(position)
                        )
                        val tv = uiObject.getChild(
                            UiSelector().className("android.widget.TextView").index(position)
                        )
                        val iv = uiObject.getChild(
                            UiSelector().className("android.widget.ImageView").index(position)
                        )
                        if (ib.exists()) ib.text else if (tv.exists()) tv.text else if (iv.exists()) iv.text else ""
                    }
                }
            }

            "$reqStr#$outputText"
        } catch (e: Exception) {
            "$reqStr#"
        }
    }


    fun testWatcher() {
        val okCancelDialogWatcher = UiWatcher {

            val okCancelDialog = UiObject(UiSelector().textStartsWith("Now"))
            if (okCancelDialog.exists()) {


                return@UiWatcher okCancelDialog.waitUntilGone(25000)
            }
            false
        }

        uiDevice.registerWatcher("Now", okCancelDialogWatcher)
// Run watcher

// Run watcher
        uiDevice.runWatchers()


    }


}

