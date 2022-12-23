package com.marquistech.quickautomationlite.testcases.call

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.CallHelper
import com.marquistech.quickautomationlite.helpers.core.Helper

class VoiceCallTestUsingHistory : TestFlow() {


    override fun onCreateHelper(): Helper {
        return CallHelper()
    }

    override fun onCreateScript(): List<Action> {

        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(1))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(1))
        actions.add(Action.ClearRecentApps())
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.google.android.dialer"),
                stepName = "Launch contact app"
            )
        )
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.dialer:id/tab_call_history")));
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.dialer:id/call_button"), stepName = "Initiate the voice call"))
        actions.add(Action.Delay(5))
        actions.add(
            Action.Click(
                Selector.ByContentDesc("End call"),
                stepName = "Disconnect the call"
            )
        )

        return actions
    }


    private val reportList = mutableListOf<Report>()
    private var report: Report? = null

    override fun onInitTestLoop(): Int {
        return 2
    }

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 3)
    }


    override fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {
        super.actionLaunchAppResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(tag, "actionLaunchAppResult  result $result")
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
        writeLog(tag, "actionClickResult  $stepName  result $result")
    }



    override fun onEndIteration(testName: String, count: Int) {


        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }

    }

    override fun onTestStart(testName: String) {
        reportList.clear()
    }


    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Voice_call_using_history")
    }


}