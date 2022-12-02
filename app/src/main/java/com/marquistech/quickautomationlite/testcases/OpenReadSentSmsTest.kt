package com.marquistech.quickautomationlite.testcases

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.SmsReadHelper

class OpenReadSentSmsTest : TestFlow() {

    override fun onCreateHelper(): Helper {
        return SmsReadHelper()
    }


    override fun onCreateScript(): List<Action> {
        var actions = mutableListOf<Action>()

        actions = openSms()
        return actions
    }

    override fun onStartIteration(testName: String, count: Int) {

    }

    override fun onTestStart(testName: String) {

    }

    override fun onTestEnd(testName: String) {

    }


    override fun onEndIteration(testName: String, count: Int) {

    }

    fun openSms(): MutableList<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))

        actions.add(Action.Delay(4))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging")))

        actions.add(Action.Delay(2))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(
            Action.ClickListItem(
                Selector.ByCls("android.support.v7.widget.RecyclerView"),
                0,
                "android.widget.RelativeLayout",
                "099581 49304"
            )
        )
        actions.add(Action.Delay(2))
        actions.add(
            Action.ClickListItemByIndex(
                Selector.ByCls("android.support.v7.widget.RecyclerView"),
                0,
                "android.widget.TextView",
                3
            )
        )
        return actions

    }


}