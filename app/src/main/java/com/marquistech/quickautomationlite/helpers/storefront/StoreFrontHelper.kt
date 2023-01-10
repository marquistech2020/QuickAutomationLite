package com.marquistech.quickautomationlite.helpers.storefront

import android.util.Log
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.marquistech.quickautomationlite.core.Selector
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.helpers.core.Helper


/**
 * Created by Ashutosh on 14,November,2022,
 */
class StoreFrontHelper : Helper() {
    override fun clearRecentApps(): Boolean {


        val uiSelector = UiSelector().className("android.widget.ListView")

        var uiObject = uiDevice.findObject(uiSelector)
        val width = uiDevice.displayWidth
        val height = uiDevice.displayHeight

        if (uiObject.exists().not()){
            uiObject = uiDevice.findObject(UiSelector().className("android.widget.ScrollView"))
        }

        var isClearAll = false

        if (uiObject.exists()) {
            val childItem = uiObject.getChild(UiSelector().clickable(true))
            while (childItem.exists() && childItem.childCount != 0) {
                uiDevice.swipe(width / 2, height / 2, width / 2, 0, 10)
            }

            isClearAll = childItem.exists().not()
        }

        return isClearAll

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
                is Selector.ByContentDesc -> {
                    uiSelector = UiSelector().descriptionContains(selector.contentDesc)
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

    override fun performListItemClickByIndex(
        selector: Selector,
        position: Int,
        itemClassname: String,
        itemSearchIndex: Int,
        testFlag: String
    ): Boolean {
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
                is Selector.ByContentDesc -> {
                    uiSelector = UiSelector().descriptionContains(selector.contentDesc)
                }
                else -> {}
            }
            uiSelector?.let {
                val uiObject = UiScrollable(uiSelector)
                Log.e("ListItemCount","Count "+uiObject.childCount)

                if (uiObject.exists()){
                    if(uiObject.childCount==0){
                        uiObject.click()
                    }else{
                        val item: UiObject = uiObject.getChild(UiSelector()
                            .className(itemClassname).instance(itemSearchIndex))
                        if(item.exists()) {
                            item.getChild(UiSelector()
                                .className(itemClassname).instance(itemSearchIndex))
                                .clickAndWaitForNewWindow(200)

                            //item.dragTo(item,200)
                        }
                    }

                }
            }


            return true
        } catch (e: Exception) {
            Log.e("Helper", " exception ${e.message}")
            false
        }
    }

}

