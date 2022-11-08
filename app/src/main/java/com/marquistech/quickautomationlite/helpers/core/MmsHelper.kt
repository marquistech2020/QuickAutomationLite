package com.marquistech.quickautomationlite.helpers.core

import android.util.Log
import androidx.test.uiautomator.*
import androidx.test.uiautomator.UiSelector
import com.marquistech.quickautomationlite.core.Selector


class MmsHelper : Helper() {

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
            }

            val uiObject = uiDevice.findObject(uiSelector)

            if (uiObject.exists()){
                if(uiObject.childCount==0){
                    uiObject.click()
                }else{
                    uiObject.getChild(UiSelector().clickable(true).index(position)).click()
                }

            }
            
            return true
        } catch (e: Exception) {
            Log.e("Helper", " exception ${e.message}")
            false
        }
    }

    override fun performSetText(selector: Selector, text: String): Boolean {
        //uiDevice.executeShellCommand("input text $text")
        var bySelector: BySelector? = null
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
        }
        uiSelector?.let {
            var uiObj = uiDevice.findObject(it)
            if(uiObj.exists()){
                uiObj.setText(text)
            }


            waitFor(1)

        }
        return true
    }

    override fun performGetText(selector: Selector): String {
        return try {
            var bySelector: BySelector? = null
            var uiSelector: UiSelector? = null
           // performListItemText()
            textWatcher()
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
            }

            var outputText = ""

            uiSelector?.let {
                val uiObj = uiDevice.findObject(it)
                waitFor(1)
                if(uiObj.exists()) {
                    outputText = uiObj.text
                    Log.e("GetText","Text "+uiObj.text)
                    if(uiObj.text.equals("Now"
                    )){
                        Log.e("GetText","Condition Matched")
                    }else{

                    }

                }
            }
            outputText
        } catch (e: Exception) {
            ""
        }
    }
fun textWatcher(){
    // Define watcher
    // Define watcher
    val okCancelDialogWatcher = UiWatcher {
        val okCancelDialog = UiObject(UiSelector().textStartsWith("Now"))
        if (okCancelDialog.exists()) {
            Log.e("Watcher", "Found Condition")
            //Log.e("Found", "Found the example OK/Cancel dialog")
            /*val okButton = UiObject(UiSelector().className("android.widget.Button").text("OK"))
            try {

                okButton.click()
            } catch (e: UiObjectNotFoundException) {
// TODO Auto-generated catch block
                e.printStackTrace()
            }*/
            return@UiWatcher okCancelDialog.waitUntilGone(25000)
        }
        false
    }
// Register watcher
// Register watcher
    UiDevice.getInstance().registerWatcher("Now", okCancelDialogWatcher)
    Log.e("Watcher", "Register")
// Run watcher

// Run watcher
    UiDevice.getInstance().runWatchers()
    /*val settingsItem = UiScrollable(UiSelector().className("android.support.v7.widget.RecyclerView"))
    val about: UiObject = settingsItem.getChildByText(
        UiSelector().className("android.widget.RelativeLayout"),"070110 46214")
    about.click()*/


}
    fun ClickonListItem(){
        val settingsItem = UiScrollable(UiSelector().className("android.support.v7.widget.RecyclerView"))

        Log.e("Watcher", "ChildCount"+settingsItem.childCount)
        val about: UiObject = settingsItem.getChildByText(
            UiSelector().className("android.widget.RelativeLayout"),"070110 46214")
        about.click()
    }

    override fun performListItemClick(selector: Selector, position: Int,itemClassname:String,itemSearch:String): Boolean {
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

                    val item: UiObject = uiObject.getChildByText(
                        UiSelector().className(itemClassname),itemSearch)
                    if(item.exists()) {
                        item.click()
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
                        .className(itemClassname).instance(8))
                    if(item.exists()) {
                        item.longClick()
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
     fun performListItemText(): Boolean {

         val settingsItem = UiScrollable(UiSelector().className("android.support.v7.widget.RecyclerView"))
val uiObject=UiScrollable(UiSelector().className("android.view.ViewGroup"))
         Log.e("Watcher", "ChildCount"+settingsItem.childCount)
         val about: UiObject = uiObject.getChildByText(
             UiSelector().className("android.widget.FrameLayout"),"Now")
         about.text
         Log.e("GetText", "text "+about.text)
        return true
    }
}

