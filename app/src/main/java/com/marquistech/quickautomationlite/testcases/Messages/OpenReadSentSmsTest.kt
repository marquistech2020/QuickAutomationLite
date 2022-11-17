package com.marquistech.quickautomationlite.testcases.Messages

import android.util.Log
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.SmsReadHelper

class OpenReadSentSmsTest : TestFlow() {
    private val reportList = mutableListOf<Report>()
    private var report: Report? = null


    override fun onInitTestLoop(): Int {
        return 2
    }
    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 4)
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
        return SmsReadHelper()
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
        StorageHandler.writeXLSFile(reportList, "MMS_OpenRead_SMs")
    }


    fun openSms(): MutableList<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))

        actions.add(Action.Delay(4))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging"), stepName = "Open Message App"))

        actions.add(Action.Delay(2))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(
            Action.ClickListItem(
                Selector.ByCls("android.support.v7.widget.RecyclerView"),
                0,
                "android.widget.RelativeLayout",
                "070110 46214"
                , stepName = "Open Chat Window","")
        )
        actions.add(Action.Delay(3))
        actions.add(
            Action.ClickListItemByIndex(
                Selector.ByCls("android.support.v7.widget.RecyclerView"),
                0,
                "android.widget.TextView",
                4
                , stepName = "Select Received Text message ","")
        )
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/action_bar_overflow")))
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByText("View details")))
        actions.add(Action.Delay(2))
        actions.add(Action.GetText(Selector.ByRes("com.google.android.apps.messaging:id/message"), stepName = "View received Sms Details"))
        return actions
    }


    override fun actionListItemGetTextByindexResult(
        count: Int,
        reqSelector: Selector,
        result: String,
        stepName: String,
        testFalg :String
    ) {
        Log.e("GetTextByIndex", "" + result)
    }

    override fun actionGetTextResult(count: Int, result: String, stepName: String) {
        super.actionGetTextResult(count, result, stepName)
        if(result.isNotEmpty()){
            report?.insertStep(stepName, if (result.contains("Text message")) "Pass" else "Fail")

        }
    }
}