package com.marquistech.quickautomationlite.testcases.Messages

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.*

class DeleteSmsTest : TestFlow() {
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
        return SmsMmsDeleateHelper()
    }



    override fun onCreateScript(): List<Action> {
        var actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))

        // actions=sendAudioOnePlusDevice()
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging"), stepName = "Message App Launch"))

        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_DW,40))
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_UP,40))
        actions.add(Action.Delay(1))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(Action.ClickListItem(Selector.ByCls("android.support.v7.widget.RecyclerView"),0,"android.widget.RelativeLayout","070110 46214","",""))

        /*actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/action_bar_overflow")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByText("Delete")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("android:id/button1")))
*/
        actions.add(Action.Delay(20))
        actions.add(Action.ClickListItem(Selector.ByCls("android.support.v7.widget.RecyclerView"),0,"android.widget.RelativeLayout","070110 46214", stepName = "Contact Chat screen open",""))
        actions.add(Action.Delay(1))
        actions.add(Action.ClickListItemByIndex(Selector.ByRes("android:id/list"),0,"com.google.android.apps.messaging:id/conversation_message_view",2, stepName = "Select  Last Text message for  Delete", testFlag = UtilsClass.Delete_MMS))
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_UP,40))
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_DW,40))

        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/action_delete_message")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("android:id/button1"), stepName = "Delete Message"))
        actions.add(Action.Delay(1))
        /*  actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/action_bar_overflow")))
          actions.add(Action.Delay(1))
          actions.add(Action.Click(Selector.ByText("View details")))
          actions.add(Action.Delay(1))*/
        actions.add(Action.GetText(Selector.ByRes("com.google.android.apps.messaging:id/message")))
        actions.add(Action.Delay(1))


        /*     actions.add(Action.SendEvent(EventType.BACK))
             actions.add(Action.Delay(7))
             actions.add(Action.ClickListItem(Selector.ByCls("android.support.v7.widget.RecyclerView"),0,"android.widget.FrameLayout","android.widget.ImageView"))
             actions.add(Action.Delay(1))*/
        return actions

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
        StorageHandler.writeLog(tag, "actionClickResult  $stepName  result $result")
    }


    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {

    }





    override fun onTestStart(testName: String) {
        reportList.clear()
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "SMS_Delete_SMs")
    }

    override fun actionListItemGetTextByindexResult(
        count: Int,
        reqSelector: Selector,
        result: String,
        stepName: String,
        testFlag :String
    ) {
        if(stepName.isNotEmpty()) {
            if (result.isNotEmpty()) {
                report?.insertStep(
                    stepName,
                    if (result.contains("MultimediaMessage")) "Pass" else "Fail"
                )
            }
        }
    }

    override fun actionListItemClickByTextResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String,
        testFlag: String
    ) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        StorageHandler.writeLog(tag, "actionClickResult  $stepName  result $result")
        super.actionListItemClickByTextResult(count, reqSelector, result, stepName,"")
    }

}