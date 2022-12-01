package com.marquistech.quickautomationlite.helpers.core

import android.util.Log
import androidx.test.uiautomator.*
import com.marquistech.quickautomationlite.core.ListItemEvent
import com.marquistech.quickautomationlite.core.Selector
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MmsReadHelper : Helper() {

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


    override fun performClick(selector: Selector, position: Int, lgClick: Boolean): Boolean {
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


    override fun performGetText(selector: Selector, position: Int): String {
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
                    val str:String=uiObj.text
                    if(str.contains("Received")){
                        val pos_1: Int = str.indexOf("Received:")
                        var recivedTime=str.substring(pos_1+10,pos_1+27)
                        var status=dateDifference(recivedTime)

                        Log.e("GetText","Condition Matched")
                        Log.e("GetText","receivedTime "+recivedTime)
                        Log.e("GetText","status "+status)
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

    override fun performListItemClickByText(selector: Selector, position: Int, itemClassname:String, itemSearch:String,testFlag: String): Boolean {
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
    override fun performListItemClickByIndex(selector: Selector, position: Int,itemClassname:String,itemIndex:Int,testFlag:String): Boolean {
        return try {
            var uiSelector: UiSelector? = null
            var bySelector:BySelector?=null
            when (selector) {
                is Selector.ByCls -> {
                    uiSelector =UiSelector().className(selector.clsName)
                    //bySelector=By.clazz(selector.clsName)
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
                    val childCount=uiObject.childCount-2
                    Log.e("itemChildCount","Select count "+childCount)
                    var item: UiObject = uiObject.getChild(UiSelector()
                        .resourceId(itemClassname).index(childCount))

                    Log.e("itemChildCount","Item Child count "+item.childCount)
                 var  child_item=  item.getChild(UiSelector().resourceId("com.google.android.apps.messaging:id/message_content").instance(0))
                    if(child_item.exists()) {
                        Log.e("ReadSms",""+item.text)
                        Log.e("itemChildCount","Recyler Child count "+uiObject.childCount)
                        Log.e("itemChildCount","Item Child count "+child_item.childCount)
                        //item.clickAndWaitForNewWindow(200)

                        performListItemEvent(ListItemEvent.DRAG,child_item,200)
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

    fun dateDifference( receivedTime:String):Boolean{

        try {
            val oldDate: Date = dateFormate(receivedTime)!!
            System.out.println(oldDate)
            Log.e(
                "GetText", " OldDate: " + oldDate    )
            val currentDate = System.currentTimeMillis()
            val diff: Long = currentDate - oldDate.time
            Log.e(
                "GetText", " Diff: " + diff
            )
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            Log.e(
                "GetText", " seconds: " + seconds + " minutes: " + minutes
                        + " hours: " + hours + " days: " + days
            )
            if(seconds<=100){
                return true
            }


            // Log.e("toyBornTime", "" + toyBornTime);
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }
    override fun performListItemGetTextByIndex(selector: Selector, position: Int,itemClassname:String,
                                               itemIndex:Int,
                                               testFlag: String): String {
        var outText:String=""
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
             if(testFlag.equals(UtilsClass.Received_MMS_Type))
            {
                val uiObject = UiScrollable(uiSelector)
                Log.e("ListItemCount", "Count " + uiObject.childCount)
                var outText = ""
                if (uiObject.exists()) {
                    if (uiObject.childCount == 0) {
                        outText = uiObject.text
                    } else {
                        val childCount = uiObject.childCount - 2
                        Log.e("itemChildCount", "Select count " + childCount)
                        var item: UiObject = uiObject.getChild(
                            UiSelector()
                                .resourceId(itemClassname).index(childCount)
                        )
                        var childItem: UiObject = item.getChild(
                            UiSelector()
                                .resourceId("com.google.android.apps.messaging:id/message_content")
                        )

                        Log.e("itemChildCount", "Item Child count " + item.childCount)
                        var isImageType = childItem.getChild(
                            UiSelector().resourceId("com.google.android.apps.messaging:id/image_attachment_view")
                                .instance(0)
                        )
                        var isVideoType = childItem.getChild(
                            UiSelector().resourceId("com.google.android.apps.messaging:id/video_attachment_view")
                                .instance(0)
                        )
                        var isAudioType = childItem.getChild(
                            UiSelector().resourceId("com.google.android.apps.messaging:id/audio_attachment_view")
                                .instance(0)
                        )
                        var isNormalMessage = childItem.getChild(
                            UiSelector().resourceId("com.google.android.apps.messaging:id/message_text_and_info")
                                .instance(0)
                        )
                        if (isImageType.exists()) {
                            outText = UtilsClass.IMAGE

                        }
                        if (isVideoType.exists()) {
                            outText = UtilsClass.VIDEO

                        }
                        if (isAudioType.exists()) {
                            outText = UtilsClass.AUDIO

                        }
                        if (isNormalMessage.exists()) {
                            outText = UtilsClass.TEXT

                        }
                    }

                }
                return outText
            }
            else {
                 if (uiObject.exists()){
                     if(uiObject.childCount==0){
                         uiObject.click()
                     }else{
                         val item: UiObject = uiObject.getChild(UiSelector()
                             .className(itemClassname).instance(itemIndex))
                         if(item.exists()) {
                             Log.e("ReadSms",""+item.text)
                             //item.clickAndWaitForNewWindow(200)
                             outText=item.text

                         }
                     }



                 }
            }


            return outText
        } catch (e: Exception) {
            Log.e("Helper", " exception ${e.message}")
            ""
        }

        return outText
    }
}

