package com.marquistech.quickautomationlite.testcases

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.StoreFrontHelper


/**
 * Created by Ashutosh on 14,November,2022,
 */
class OpenStoreFrontAppInUnTest :TestFlow() {
    override fun onCreateHelper(): Helper {
        return StoreFrontHelper()
    }
    override fun onInitTestLoop(): Int {
        return 5
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
        // actions.add(Action.LaunchApp(AppSelector.ByUri("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")))
        actions.add(Action.LaunchApp(AppSelector.ByUri("http://play.google.com/store/apps/details?id=com.snehitech.browseme"), stepName = "Browse-me App is open Sucessfully"))
        actions.add(Action.Delay(10))
/*
//for opening the App
        actions.add(
            Action.ClickListItemByIndex(
                Selector.ByCls("androidx.compose.ui.platform.ComposeView"),
                0,
                "android.view.View",
                8,


            ))
        actions.add(Action.Delay(5))


 */


        //For installing the App
        actions.add(
            Action.ClickListItemByIndex(
                Selector.ByCls("androidx.compose.ui.platform.ComposeView"),
                0,
                "android.view.View",
                7,
                stepName = "App is installed sucessfully"
                , testFlag = ""))
        actions.add(Action.Delay(30))

        //For Uninstalling the app
        actions.add(Action.ClickListItemByIndex(
            Selector.ByCls("androidx.compose.ui.platform.ComposeView"),
            0,
            "android.view.View",
            5))
        actions.add(Action.Delay(5))
        actions.add(Action.ClickListItemByIndex(
            Selector.ByCls("android.view.View"),
            0,
            "android.view.View",
            2,
            stepName = "App is un-installed sucessfully"))
        actions.add(Action.Delay(5))
        return actions
    }
    private val reportList = mutableListOf<Report>()
    private var report: Report? = null
    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count,4)
    }

    override fun actionListItemClickByindexResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String,
        testFlagName: String
    ) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        super.actionListItemClickByindexResult(count, reqSelector, result, stepName, testFlagName)
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
        StorageHandler.writeXLSFile(reportList, "Open_Store_Front_In_Un")
    }
}


