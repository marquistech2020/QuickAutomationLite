package com.marquistech.quickautomationlite.testcases

import android.util.Log
import androidx.test.uiautomator.By
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.core.CallHelper
import com.marquistech.quickautomationlite.helpers.core.CordinateHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.MmsHelper

class MmsReceivedImageTest : TestFlow() {

    override fun onCreateHelper(): Helper {
        return MmsHelper()
    }


    override fun onCreateScript(): List<Action> {
        var actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))

        // actions=sendAudioOnePlusDevice()
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging")))

        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_DW,40))
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_UP,40))
        actions.add(Action.Delay(1))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(Action.ClickListItem(Selector.ByCls("android.support.v7.widget.RecyclerView"),0,"android.widget.RelativeLayout","070110 46214"))

        actions.add(Action.Delay(1))

        actions.add(Action.ClickListItemByIndex(Selector.ByCls("android.support.v7.widget.RecyclerView"),0,"android.widget.FrameLayout",3))

        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_DW,40))
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_UP,40))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/action_bar_overflow")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByText("View details")))
        actions.add(Action.Delay(1))
        actions.add(Action.GetText(Selector.ByRes("com.google.android.apps.messaging:id/message")))
        actions.add(Action.Delay(1))

        /*     actions.add(Action.SendEvent(EventType.BACK))
             actions.add(Action.Delay(7))
             actions.add(Action.ClickListItem(Selector.ByCls("android.support.v7.widget.RecyclerView"),0,"android.widget.FrameLayout","android.widget.ImageView"))
             actions.add(Action.Delay(1))*/
        return actions
    }

    override fun onTestStart(testName: String) {


    }

    override fun onTestEnd(testName: String) {

    }

    override fun onStartIteration(testName: String, count: Int) {

    }



    override fun onEndIteration(testName: String, count: Int) {

    }

    fun sendAudioOnePlusDevice():MutableList<Action>{
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(Coordinate(584,1847,619,1277),40))

        actions.add(Action.Delay(4))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging")))

        actions.add(Action.Delay(2))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/start_chat_fab")))

        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/recipient_text_view")))
        actions.add(Action.Delay(1))
        actions.add(Action.SetText(Selector.ByRes("com.google.android.apps.messaging:id/recipient_text_view"),"7011046214"))
        actions.add(Action.Delay(1))
        actions.add(Action.SendEvent(EventType.ENTER))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/plus_button")))
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByText("Files")))
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByText("Videos")))
        actions.add(Action.Delay(1))
/*        var mmsHelper=MmsHelper()
        mmsHelper.clickListViewItem(1)*/
        //actions.add(Action.Click(Selector.ByText("1.aac")))
        actions.add(Action.Click(Selector.ByText("1.mp4")))
        //actions.add(Action.Click(Selector.ByText("1.mp4")))
        actions.add(Action.Delay(1))
       // actions.add(Action.Click(Selector.ByText("SIM1")))
        //actions.add(Action.Delay(2000))

        /*actions.add(Action.Click(By.res("com.google.android.apps.messaging:id/full_screen_gallery_item_icon")))
        actions.add(Action.Delay(1000))
        actions.add(Action.Click(By.res("com.google.android.apps.messaging:id/gallery_content_async_image")))
        actions.add(Action.Delay(1000))
        actions.add(Action.Click(By.res("com.google.android.apps.messaging:id/container_action_button")))
        actions.add(Action.Delay(1000))*/
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/send_message_button_icon")))
        actions.add(Action.Delay(1))

        return actions

    }

    
}