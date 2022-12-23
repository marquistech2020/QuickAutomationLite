package com.marquistech.quickautomationlite.testcases

import android.provider.Settings
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.WifiEnbDsbHelper

/**
 * Created by Ashutosh on 10,November,2022,
 */
class WifiOnOff :TestFlow() {


    override fun onCreateHelper(): Helper {
        return WifiEnbDsbHelper()
    }
    override fun onInitTestLoop(): Int {
        return 8000
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
        actions.add(Action.Delay(second = 1))
/*
        actions.add(
            Action.LaunchApp(
                AppSelector.ByAction(Settings.ACTION_WIFI_SETTINGS),
                stepName = "Launch WIfi App"
            )
        )

 */

/*
//for realme C35 this package is required
//It is Android 11 device
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.android.settings"),
                stepName = "Launch WIfi App"
            ))

 */
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.oplus.wirelesssettings"),
                stepName = "Launch WIfi App"
            ))
        actions.add(Action.Delay(second = 3))
        actions.add(Action.Click(Selector.ByText("Wi-Fi"), stepName = "Wifi is enabled successfully"))
        actions.add(Action.Delay(second = 3))
        actions.add(Action.Click(Selector.ByText("Wi-Fi"), stepName = "Wifi is disabled successfully"))
        actions.add(Action.Delay(second = 3))
        actions.add(Action.SendEvent(EventType.HOME))
        return  actions

    }


    private val reportList = mutableListOf<Report>()
    private var report: Report? = null
    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count,4)


    }

    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {
        super.actionClearRecentResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        StorageHandler.writeLog(tag, "actionClearRecentResult  result $result")
    }

    override fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {
        super.actionLaunchAppResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        StorageHandler.writeLog(tag, "actionLaunchAppResult  result $result")
    }

    override fun actionClickResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        StorageHandler.writeLog(tag, "actionClickResult result $result")
    }
/*
    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
        val requestText = result.split("#").first()
        val resultText = result.split("#").last()
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (resultText.isNotEmpty()) "Pass" else "Fail")
        }
        writeLog(tag, "actionGetTextResult  result $result")

    }

 */

    override fun onEndIteration(testName: String, count: Int) {
        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        StorageHandler.writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }

    }

    override fun onTestStart(testName: String) {
        reportList.clear()

    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Wifi_On_Off")



    }
}