package com.marquistech.quickautomationlite

import android.provider.Settings
import com.marquistech.quickautomationlite.core.Action
import com.marquistech.quickautomationlite.core.TestFlow
import com.marquistech.quickautomationlite.reports.WifiOnOffReport

class VtCallTest : TestFlow() {


    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.Home)
        actions.add(Action.ClearRecentApps)
        actions.add(Action.LaunchPackage(Settings.ACTION_AIRPLANE_MODE_SETTINGS, false))
        actions.add(
            Action.SwitchOn(
                byClass = "android.widget.Switch",
                isUiSelector = true,
                position = 0
            )
        )
        actions.add(Action.Delay(2000))
        actions.add(
            Action.SwitchOFF(
                byClass = "android.widget.Switch",
                isUiSelector = true,
                position = 0
            )
        )
        actions.add(Action.Delay(2000))

        return actions
    }

    var report: WifiOnOffReport? = null

    override fun onStartIteration(testName: String, count: Int) {
        report = WifiOnOffReport(count)

    }

    override fun actionHomeResult(count: Int, result: Boolean) {

    }

    override fun onEndIteration(testName: String, count: Int) {

    }
}