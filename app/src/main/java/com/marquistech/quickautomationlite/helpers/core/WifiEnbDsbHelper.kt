package com.marquistech.quickautomationlite.helpers.core

import android.util.Log
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.marquistech.quickautomationlite.core.Action
import com.marquistech.quickautomationlite.core.Selector
import com.marquistech.quickautomationlite.data.StorageHandler

/**
 * Created by RAJESH on 09,November,2022,
 */
class WifiEnbDsbHelper : Helper() {

    override fun clearRecentApps(): Boolean {


        val uiSelector = UiSelector().className("android.widget.ListView")

        val lv = uiDevice.findObject(uiSelector)

        lv.let { list ->


            while (list.getChild(UiSelector().className("android.widget.FrameLayout"))
                    .exists()
            ) {
                uiDevice.swipe(542, 1005, 542, 157, 30)
            }
        }

        return lv.exists().not()

    }


    override fun performClick(selector: Selector, position: Int, isLongClick: Boolean): Boolean {
        return try {
            var uiSelector: UiSelector? = null
            var isResId = false

            when (selector) {
                is Selector.ByCls -> {
                    uiSelector = UiSelector().className(selector.clsName)
                }
                is Selector.ByPkg -> {
                    uiSelector = UiSelector().packageName(selector.pkgName)
                }
                is Selector.ByRes -> {
                    uiSelector = UiSelector().resourceId(selector.resName)
                    isResId = true
                }
                is Selector.ByText -> {
                    uiSelector = UiSelector().text(selector.text)
                }
                else -> {}
            }


            var isClicked = false

            uiSelector?.let {
                val uiObject = uiDevice.findObject(uiSelector)

                if (uiObject.exists()) {
                    isClicked = if (uiObject.childCount == 0 || isResId) {
                        if (isLongClick) uiObject.longClick() else uiObject.click()
                    } else {
                        val btn = uiObject.getChild(UiSelector().clickable(true).index(position))
                        if (btn.exists()) {
                            if (isLongClick) btn.longClick() else btn.click()
                        } else false
                    }
                }
            }
            return isClicked
        } catch (e: Exception) {
            StorageHandler.writeLog("Helper", " exception ${e.cause?.message}")
            false
        }
    }

    override fun performSetText(selector: Selector, text: String): Boolean {
        uiDevice.executeShellCommand("input text $text")
        return true
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
                else -> {}
            }

            var outputText = ""

            uiSelector?.let {
                val uiObject = uiDevice.findObject(uiSelector)

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
    private fun getItemAddNetwork(): Boolean {

        val settingsItem = UiScrollable(UiSelector().className("android.widget.LinearLayout"))
        val actions = mutableListOf<Action>()
        Log.e("Watcher", "ChildCount"+settingsItem.childCount)
        val about: UiObject = settingsItem.getChildByText(
            UiSelector().className("android.widget.RelativeLayout"),"Add network")
        about.click()



        return true
    }


}

