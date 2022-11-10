package com.marquistech.quickautomationlite.testcases

import android.provider.Settings
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.WifiEnbDsbHelper

/**
 * Created by RAJESH on 10,November,2022,
 */
class WifiOnOff :TestFlow() {


    override fun onCreateHelper(): Helper {
        return WifiEnbDsbHelper()
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
        actions.add(Action.Delay(second = 1))
        actions.add(
            Action.LaunchApp(
                AppSelector.ByAction(Settings.ACTION_WIFI_SETTINGS),
                stepName = "Launch WIfi App"
            )
        )

        actions.add(Action.Click(Selector.ByText("Wi-Fi")))
        actions.add(Action.Delay(second = 5))
        actions.add(Action.Click(Selector.ByText("Wi-Fi")))
        actions.add(Action.Delay(second = 5))




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