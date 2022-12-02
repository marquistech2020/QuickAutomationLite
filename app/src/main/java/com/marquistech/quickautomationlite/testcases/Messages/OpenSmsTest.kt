package com.marquistech.quickautomationlite.testcases.Messages

import com.marquistech.quickautomationlite.core.Action
import com.marquistech.quickautomationlite.core.AppSelector
import com.marquistech.quickautomationlite.core.EventType
import com.marquistech.quickautomationlite.core.TestFlow
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.MmsHelper

class OpenSmsTest : TestFlow() {
    private val reportList = mutableListOf<Report>()
    private var report: Report? = null


    override fun onInitTestLoop(): Int {
        return 2
    }

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 2)
    }



    override fun onEndIteration(testName: String, count: Int) {

        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        StorageHandler.writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }
    }
    override fun onCreateHelper(): Helper {
        return MmsHelper()
    }


    override fun onCreateScript(): List<Action> {
        var actions = mutableListOf<Action>()

        actions = openSms()
        return actions
    }



    override fun onTestStart(testName: String) {
        reportList.clear()
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Open Message App")
    }
    fun openSms(): MutableList<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP, ))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))

        actions.add(Action.Delay(4))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging"), stepName = "Open Message App"))

        actions.add(Action.Delay(2))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(Action.SendEvent(EventType.HOME, stepName = "Close Message App"))
        return actions

    }

    override fun actionSendEventResult(
        count: Int,
        reqCode: EventType,
        result: Boolean,
        stepName: String
    ) {
        if(stepName.isNotEmpty()){
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        super.actionSendEventResult(count, reqCode, result, stepName)
    }

    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {


        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        StorageHandler.writeLog(tag, "actionClearRecentResult  result $result")
        super.actionClearRecentResult(count, result, stepName)
    }
    override fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {

        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        StorageHandler.writeLog(tag, "actionLaunchAppResult  result $result")
        super.actionLaunchAppResult(count, result, stepName)
    }
}