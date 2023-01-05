package com.marquistech.quickautomationlite.testcases.storefront

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.StoreFrontHelper

/**
 * Created by ashutosh on 06,December,2022,
 */
class MainScrenAppTest :TestFlow() {
    override fun onCreateHelper(): Helper {
    return StoreFrontHelper()

    }



    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
        actions.add(Action.SendEvent(EventType.HOME))
        //actions(AppSelector.ByPkg)
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.snehitech.browseme")))
        actions.add(Action.Delay(3))
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(3))

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