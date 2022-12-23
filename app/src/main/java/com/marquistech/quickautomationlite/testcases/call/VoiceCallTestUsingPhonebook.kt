package com.marquistech.quickautomationlite.testcases.call

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.CallHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import java.util.regex.Pattern

class VoiceCallTestUsingPhonebook : TestFlow() {


    override fun onCreateHelper(): Helper {
        return CallHelper()
    }

    companion object {
        private const val CONTACT_SEARCH_BAR_RES =
            "com.google.android.contacts:id/open_search_bar"
        private const val CONTACT_SEARCH_EDIT_RES =
            "com.google.android.contacts:id/open_search_view_edit_text"
        private const val CONTACT_RESULT_BUTTON_RES =
            "com.google.android.contacts:id/search_result_list"
        private const val VOICE_BUTTON_RES = "com.google.android.contacts:id/verb_call"
        private const val PHONE_BOOK_CONTACT_NAME = "contact1"
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
                AppSelector.ByPkg("com.google.android.contacts"),
                stepName = "Launch contact app"
            )
        )
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes(CONTACT_SEARCH_BAR_RES)))
        actions.add(Action.Delay(2))
        actions.add(
            Action.SetText(
                Selector.ByRes(CONTACT_SEARCH_EDIT_RES),
                PHONE_BOOK_CONTACT_NAME, stepName = "Fetching contact from phonebook"
            )
        )
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes(CONTACT_RESULT_BUTTON_RES)))
        actions.add(Action.Delay(1))
        actions.add(
            Action.Click(
                Selector.ByRes(VOICE_BUTTON_RES),
                stepName = "Initiate the voice call"
            )
        )
        actions.add(Action.Delay(5))
        actions.add(
            Action.GetText(
                Selector.ByRes("com.google.android.dialer:id/contactgrid_bottom_timer"),
                stepName = "Call received by other side"
            )
        )
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
        return 1
    }

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 5)
    }


    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {
        super.actionClearRecentResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(
            tag,
            "actionClearRecentResult  result $result Thread name ${Thread.currentThread().name}"
        )
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


    override fun actionEnableResult(count: Int, result: Boolean, stepName: String) {
        super.actionEnableResult(count, result, stepName)

        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionEnableResult  $stepName  result $result")
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

        writeLog(tag, "actionGetTextResult  $stepName  result $result")
    }


    override fun onEndIteration(testName: String, count: Int) {


        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }

    }

    override fun actionSetTextResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {
        super.actionSetTextResult(count, reqSelector, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionSetTextResult  result $result")
    }

    override fun onTestStart(testName: String) {
        reportList.clear()
    }


    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Voice_call_using_phonebook")
    }


}