package com.marquistech.quickautomationlite.testcases

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.GmailHelper
import com.marquistech.quickautomationlite.helpers.core.Helper

/**
 * Created by Ashutosh on 14,November,2022,
 */
class SendEmail : TestFlow() {
    override fun onCreateHelper(): Helper {
        return GmailHelper()
    }
    companion object {
        private const val MAIL_SENT_SUCESSFULLY = "Sent"
    }

    override fun onInitTestLoop(): Int {
        return 2
    }
override fun onCreateScript(): List<Action> {
    val actions = mutableListOf<Action>()
    actions.add(Action.SendEvent(EventType.HOME))
    actions.add(Action.Delay(milli = 500))
    actions.add(Action.SendEvent(EventType.RECENT_APP))
    actions.add(Action.Delay(milli = 500))
    actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
    actions.add(Action.Delay(second = 1))
    actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.gm"), stepName = "Launch Gmail APP"))
    actions.add(Action.Delay(second =5))
    actions.add(Action.Click(Selector.ByText("Compose"), stepName = "Compose is clicked successFully"))
    actions.add(Action.Delay(second =2))

    actions.add(
        Action.SetText(
            Selector.ByText("to"),
            "ashrun@gmail.com"

        )

    )
    actions.add(Action.Click(Selector.ByRes("com.google.android.gm:id/peoplekit_listview_contact_name")))
    actions.add(Action.SendEvent(EventType.ENTER))


    actions.add(Action.Delay(second =2))
    actions.add(
        Action.SetText(
            Selector.ByRes("com.google.android.gm:id/subject_content"),
            "Hello"

        )

    )
    actions.add(Action.SendEvent(EventType.ENTER))
    actions.add(Action.Delay(second =2))
    actions.add(
        Action.SetText(
            Selector.ByRes("com.google.android.gm:id/composearea_tap_trap_bottom"),
            "Hello_Ashutosh_how_are_you"
        )

    )
         actions.add(Action.Delay(second = 3))

    actions.add(Action.Click(Selector.ByRes("com.google.android.gm:id/send"),))

    actions.add(
        Action.GetText(
            Selector.ByText(MAIL_SENT_SUCESSFULLY),
            stepName = "Mail has been sent sucessfully from the sender"
            ),
    )


    actions.add(Action.SendEvent(EventType.HOME))
    return actions

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

    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
        val requestText = result.split("#").first()
        val resultText = result.split("#").last()
        if (stepName.isNotEmpty()&& requestText == SendEmail.MAIL_SENT_SUCESSFULLY) {
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
    StorageHandler.writeXLSFile(reportList, "Send_Email")



}
}