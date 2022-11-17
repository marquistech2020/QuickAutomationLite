package com.marquistech.quickautomationlite.helpers.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.marquistech.quickautomationlite.core.Selector
import com.marquistech.quickautomationlite.data.StorageHandler


/**
 * Created by Ashutosh on 14,November,2022,
 */
class StoreFrontHelper :Helper() {


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
            }


            var isClicked = false

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
            }

            var outputText = ""

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
            "$reqStr#$outputText"
        } catch (e: Exception) {
            "$reqStr#"
        }
    }
    override fun performListItemClickByIndex(selector: Selector, position: Int,itemClassname:String,itemIndex:Int): Boolean {
        return try {
            var uiSelector: UiSelector? = null

            when (selector) {
                is Selector.ByCls -> {
                    uiSelector =UiSelector().className(selector.clsName)
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
            }
            val uiObject = UiScrollable(uiSelector)
            Log.e("ListItemCount","Count "+uiObject.childCount)

            if (uiObject.exists()){
                if(uiObject.childCount==0){
                    uiObject.click()
                }else{
                    val item: UiObject = uiObject.getChild(UiSelector()
                        .className(itemClassname).instance(itemIndex))
                    if(item.exists()) {
                       val item =  item.getChild(UiSelector()
                            .className(itemClassname).instance(itemIndex))
                           .clickAndWaitForNewWindow(200)

                        //item.dragTo(item,200)
                    }
                }

            }

            return true
        } catch (e: Exception) {
            Log.e("Helper", " exception ${e.message}")
            false
        }

        return true
    }

}

