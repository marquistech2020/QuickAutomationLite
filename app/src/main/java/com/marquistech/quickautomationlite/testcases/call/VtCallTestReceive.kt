package com.marquistech.quickautomationlite.testcases.call

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.CallHelper
import com.marquistech.quickautomationlite.helpers.core.CordinateHelper
import com.marquistech.quickautomationlite.helpers.core.Helper

class VtCallTestReceive : TestFlow() {


    override fun onCreateHelper(): Helper {
        return CallHelper()
    }

    companion object {
        private const val FLIP_CAMERA_TEXT = "Flip camera"
    }

    override fun onInitTestLoop(): Int {
        return 3
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 200))
        actions.add(Action.SetEnable(Type.WIFI, enable = false, stepName = "Disable wifi"))
        actions.add(Action.Delay(milli = 200))
        actions.add(Action.ClickBYCordinate(PanelArea.RECEIVE_CALL, stepName = "Receive video call"))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Swipe(selector = Selector.ByRes("com.google.android.dialer:id/incoming_call_puck_container"), steps = 10))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.GetText(
                Selector.ByText(FLIP_CAMERA_TEXT),
                stepName = "Video call established"
            )
        )

        actions.add(Action.Delay(10))

        return actions
    }

    private val reportList = mutableListOf<Report>()
    private var report: Report? = null

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 4)
    }


    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {
        super.actionClearRecentResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(tag, "actionClearRecentResult  result $result")
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
        writeLog(tag, "actionClickResult  $stepName  result $result")
    }


    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
        val requestText = result.split("#").first()
        val resultText = result.split("#").last()
        if (stepName.isNotEmpty() && requestText == FLIP_CAMERA_TEXT) {
            report?.insertStep(stepName, if (resultText.isNotEmpty()) "Pass" else "Fail")
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
        StorageHandler.writeXLSFile(reportList, "Video_call_receive")
    }


}