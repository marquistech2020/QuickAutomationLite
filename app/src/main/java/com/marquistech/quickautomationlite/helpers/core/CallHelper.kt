package com.marquistech.quickautomationlite.helpers.core

import android.util.Log
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiSelector
import com.marquistech.quickautomationlite.core.Selector

class CallHelper : Helper() {

    override fun clearRecentApps(): Boolean {

        val uiSelector = UiSelector().className("android.widget.ListView")

        val lv = uiDevice.findObject(uiSelector)

        lv.let { list ->


            while (list.getChild(UiSelector().className("android.widget.FrameLayout"))
                    .exists()
            ) {
                uiDevice.swipe(542, 1005, 542, 157, 50)
            }
        }

        return lv.exists().not()

    }


    override fun performClick(selector: Selector, position: Int): Boolean {
        return try {
            var bySelector: BySelector? = null
            var uiSelector: UiSelector? = null

            when (selector) {
                is Selector.ByCls -> {
                    bySelector = By.clazz(selector.clsName)
                    uiSelector = UiSelector().className(selector.clsName)
                }
                is Selector.ByPkg -> {
                    bySelector = By.pkg(selector.pkgName)
                    uiSelector = UiSelector().packageName(selector.pkgName)
                }
                is Selector.ByRes -> {
                    bySelector = By.res(selector.resName)
                    uiSelector = UiSelector().resourceId(selector.resName)
                }
                is Selector.ByText -> {
                    bySelector = By.text(selector.text)
                    uiSelector = UiSelector().text(selector.text)
                }
            }


            val uiObject2List = uiDevice.findObjects(bySelector)

            if (uiObject2List.size > 0 && uiObject2List[position] != null) {
                waitFor(500L)
                uiObject2List[position].click()
            }


            if (uiObject2List.size == 0){
                val uiObjectList = uiDevice.findObject(UiSelector().className("android.view.ViewGroup").index(0))
                uiObject2List.get(0).click()
                Log.e("Helper"," exception 1 ${uiObjectList.exists()}")
                //val childCount = uiObjectList.childCount
                //Log.e("Helper"," child count $childCount")
            }


            /*if (childCount > 0) {
                (0 until childCount).forEach {
                    if (position == it) {
                        val btn = uiObjectList.getChild(uiSelector)
                        if (btn.exists()) {
                            waitFor(500L)
                            btn.click()
                        }
                    }

                }
            }
            */
            return true
        } catch (e: Exception) {
            Log.e("Helper"," exception ${e.message}")
            false
        }
    }

    override fun performSetText(selector: Selector, text: String): Boolean {
        uiDevice.executeShellCommand("input text $text")
        return true
    }

    override fun performGetText(selector: Selector): String {
        return try {
            var bySelector: BySelector? = null
            var uiSelector: UiSelector? = null

            when (selector) {
                is Selector.ByCls -> {}
                is Selector.ByPkg -> {}
                is Selector.ByRes -> {
                    bySelector = By.res(selector.resName)
                }
                is Selector.ByText -> {}
            }

            var outputText = ""

            bySelector?.let {
                val uiObj = uiDevice.findObject(it)
                waitFor(1)
                outputText = uiObj.text
            }
            outputText
        } catch (e: Exception) {
            ""
        }
    }


}

