package com.marquistech.quickautomationlite.testcases.Messages

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.MmsHelper

class SendSegmentSmsTest ( ) : TestFlow() {
    private val reportList = mutableListOf<Report>()
    private var report: Report? = null
    private var fileName:String=""
    override fun onCreateHelper(): Helper {
        return MmsHelper()
    }

    override fun onCreateScript(): List<Action> {
        var actions = mutableListOf<Action>()
        actions = sendSegmentSms()
        return actions
    }

    override fun onTestStart(testName: String) {
        reportList.clear()
        fileName=testName + "_Logs_" + System.currentTimeMillis()
        StorageHandler.writeXLSFileIterator( testName =tag,fileName )
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Sms_send_segment_Text")
    }
    override fun onInitTestLoop(): Int {
        return 2
    }

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 5)
    }


    override fun onEndIteration(testName: String, count: Int) {

        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        StorageHandler.writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }
    }


    fun sendSegmentSms(): MutableList<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))

        actions.add(Action.Delay(6))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging"),stepName = "Launch Message app"))

        actions.add(Action.Delay(2))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/start_chat_fab"),stepName = "Open Chat Window " ))

        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/recipient_text_view")))
        actions.add(Action.Delay(1))
        actions.add(
            Action.SetText(
                Selector.ByRes("com.google.android.apps.messaging:id/recipient_text_view"),
                "7011046214"
                ,stepName = "")

        )
        actions.add(Action.Delay(1))
        actions.add(Action.SendEvent(EventType.ENTER))
        actions.add(Action.Delay(1))
        actions.add(
            Action.SetText(
                Selector.ByRes("com.google.android.apps.messaging:id/compose_message_text"),

                getLargeText(),stepName = "Input Message large Text ")
        )
        actions.add(Action.Delay(5))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/send_message_button_icon"),stepName = "Send Message Successfully "))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/action_bar_overflow")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByText("Delete")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("android:id/button1"), stepName = "Delete Previous sent message "))
        return actions
    }
    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {
        super.actionClearRecentResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        StorageHandler.writeLog(tag, "actionClearRecentResult  result $result")
    }

    override fun actionSetTextResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {
        if(stepName.isNotEmpty()){
            report?.insertStep(stepName,if(result) "Pass" else "Fail")
            StorageHandler.addRowXLSFileIterator(count, testName = tag,stepName,if (result) "Pass" else "Fail",fileName)
        }
        super.actionSetTextResult(count, reqSelector, result, stepName)
    }

    override fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {
        super.actionLaunchAppResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
            StorageHandler.addRowXLSFileIterator(count, testName = tag,stepName,if (result) "Pass" else "Fail",fileName)
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
                 StorageHandler.addRowXLSFileIterator(count, testName = tag,stepName,if (result) "Pass" else "Fail",fileName)
        }
        StorageHandler.writeLog(tag, "actionClickResult  $stepName  result $result")
    }


    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {

    }
    fun getLargeText():String{
        return "This is demo Segment Messaging," +
                "This is demo Segment Messaging," +
                "This is demo Segment Messaging,"
               /* "This is demo Segment Messaging," +
                "This is demo Segment Messaging," +
                "This is demo Segment Messaging," +
                "This is demo Segment Messaging," +
                "This is demo Segment Messaging," +
                "This is demo Messaging "*/
    }

}