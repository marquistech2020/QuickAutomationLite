package com.marquistech.quickautomationlite

import android.util.Log
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.core.CallHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.MmsHelper

class MmsTest : TestFlow() {


    override fun onCreateHelper(): Helper {
        return MmsHelper()
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps)
        actions.add(Action.Delay(second = 1))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.contacts")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByRes("com.google.android.contacts:id/open_search_bar_text_view")),)
        actions.add(Action.Delay(1))
        actions.add(
            Action.SetText(
                Selector.ByRes("com.google.android.contacts:id/open_search_bar_text_view"),
                "contact1"
            )
        )
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByRes("android:id/list")))
        actions.add(Action.Click(Selector.ByRes("com.google.android.contacts:id/verb_video")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.GetText(Selector.ByText("Flip camera")))


        return actions
    }

    override fun onStartIteration(testName: String, count: Int) {

    }

    override fun actionSendEventResult(count: Int, reqCode: EventType, result: Boolean) {
        Log.e(tag,"actionSendEventResult  code $reqCode  result $result")
    }

    override fun actionClearRecentResult(count: Int, result: Boolean) {
        Log.e(tag,"actionClearRecentResult  result $result")
    }

    override fun actionLaunchAppResult(count: Int, result: Boolean) {
        Log.e(tag,"actionLaunchAppResult  result $result")
    }

    override fun actionSetTextResult(count: Int, reqSelector: Selector, result: Boolean) {
        Log.e(tag,"actionSetTextResult  requester = $reqSelector result $result")
    }

    override fun actionClickResult(count: Int, reqSelector: Selector, result: Boolean) {
        Log.e(tag,"actionClickResult  requester = $reqSelector result $result")
    }

    override fun actionGetTextResult(count: Int, reqSelector: Selector, result: String) {
        Log.e(tag,"actionGetTextResult  requester = $reqSelector result $result")
    }



    override fun onEndIteration(testName: String, count: Int) {

    }
}