package com.marquistech.quickautomationlite.testcases.storefront

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.storefront.StoreFrontHelper

/**
 * Created by RAJESH on 10,January,2023,
 */
class StorefrontStability : TestFlow() {
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
        actions.add(Action.LaunchApp(AppSelector.ByUri("http://play.google.com/store/apps"), stepName = "Play store is open sucessfully"))
        actions.add(Action.Delay(milli = 2000))




        return  actions

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