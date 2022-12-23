package com.marquistech.quickautomationlite.testcases.call

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.CallHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import java.util.regex.Pattern

class VoiceCallTestReceive : TestFlow() {


    override fun onCreateHelper(): Helper {
        return CallHelper()
    }


    override fun onInitTestLoop(): Int {
        return 1
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all apps from recent"))
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SetEnable(Type.WIFI, enable = false, stepName = "Disable wifi"))
        actions.add(Action.Delay(20))
        actions.add(Action.ClickBYCordinate(780, 416))
        actions.add(Action.Delay(5))
        actions.add(
            Action.GetText(
                Selector.ByRes("com.google.android.dialer:id/contactgrid_bottom_timer"),
                stepName = "Received voice call"
            )
        )
        actions.add(Action.Delay(milli = 500))
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

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 4)
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

    override fun actionClickByCoordinateResult(
        count: Int,
        result: Boolean,
        stepName: String
    ) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionClickByCoordinateResult  $stepName  result $result")
    }


    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
        val requestText = result.split("#").first()
        val resultText = result.split("#").last()
        if (stepName.isNotEmpty()){
            if (Pattern.matches(("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\$"),resultText)){
                report?.insertStep(stepName, "Pass")
            }else{
                report?.insertStep(stepName, "Fail")
            }
        }
        writeLog(tag, "actionGetTextResult  result $result")
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
        StorageHandler.writeXLSFile(reportList, "Voice_call_receive")
    }


}