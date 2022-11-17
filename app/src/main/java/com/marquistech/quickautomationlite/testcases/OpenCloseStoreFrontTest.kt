package com.marquistech.quickautomationlite.testcases

import com.marquistech.quickautomationlite.core.Action
import com.marquistech.quickautomationlite.core.AppSelector
import com.marquistech.quickautomationlite.core.EventType
import com.marquistech.quickautomationlite.core.TestFlow
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.StoreFrontHelper

/**
 * Created by Ashutosh on 16,November,2022,
 */
class OpenCloseStoreFrontTest : TestFlow() {
    override fun onCreateHelper(): Helper {
        return StoreFrontHelper()
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
        actions.add(Action.LaunchApp(AppSelector.ByUri("http://play.google.com/store/apps")))
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
}