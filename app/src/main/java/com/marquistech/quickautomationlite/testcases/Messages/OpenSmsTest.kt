package com.marquistech.quickautomationlite.testcases.Messages

import android.util.Log
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.MmsHelper

class OpenSmsTest : TestFlow() {
    private val reportList = mutableListOf<Report>()
    private var report: Report? = null
    private var count:Int=0;

    override fun onInitTestLoop(): Int {
        return 1500
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
        StorageHandler.createTestCaseLog2File(tag,fileName2,report)
    }
    override fun onCreateHelper(): Helper {
        return MmsHelper()
    }


    override fun onCreateScript(): List<Action> {
        Log.e("TestCaseCount","Iteration "+count+1)
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



        Log.e("TestCaseCount","Method Call "+count+1)
        val actions = mutableListOf<Action>()
        try{
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
        }catch (e:Exception){
            Log.e("TestCaseCount","Error Catch ")
        }
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