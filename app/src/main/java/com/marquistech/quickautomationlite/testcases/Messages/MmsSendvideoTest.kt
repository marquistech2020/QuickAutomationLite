package com.marquistech.quickautomationlite.testcases.Messages

import android.util.Log
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.MmsHelper
import com.marquistech.quickautomationlite.helpers.core.UtilsClass

class MmsSendvideoTest : TestFlow() {
    private val reportList = mutableListOf<Report>()
    private var report: Report? = null

    override fun onCreateHelper(): Helper {
        return MmsHelper()
    }
    override fun onInitTestLoop(): Int {
        return 2
    }override fun onStartIteration(testName: String, count: Int) {
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



    override fun onCreateScript(): List<Action> {
        var actions = mutableListOf<Action>()

        actions = sendAudioOnePlusDevice()
        return actions
    }



    fun sendAudioOnePlusDevice(): MutableList<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.Delay(second = 1))
        actions.add(Action.Swipe(Coordinate(584, 1847, 619, 1277), 40))

        actions.add(Action.Delay(4))
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.apps.messaging"),stepName = "Launch Message app"))

        actions.add(Action.Delay(2))
        //com.google.android.apps.messaging:id/start_chat_fab
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/start_chat_fab"),stepName = "Start Chat Open"))

        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/recipient_text_view")))
        actions.add(Action.Delay(1))
        actions.add(
            Action.SetText(
                Selector.ByRes("com.google.android.apps.messaging:id/recipient_text_view"),
                "7011046214"
            )
        )
        actions.add(Action.Delay(1))
        actions.add(Action.SendEvent(EventType.ENTER))
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/plus_button")))
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByText("Files")))
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByText("Videos")))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByRes("com.google.android.documentsui:id/option_menu_search")))
        actions.add(Action.Delay(1))
        actions.add(Action.SetText(Selector.ByRes("com.google.android.documentsui:id/search_src_text"), stepName = "", text = "1.mp4"))
        actions.add(Action.Delay(1))
        actions.add(Action.SendEvent(EventType.BACK))
        actions.add(Action.Delay(1))
/*        var mmsHelper=MmsHelper()
        mmsHelper.clickListViewItem(1)*/
        actions.add(Action.ClickListItem(Selector.ByRes("com.google.android.documentsui:id/dir_list"), itemSearch = "1.mp4",stepName = "Select Image from Image", testFlag = UtilsClass.SEND_Image_MMS, itemClassname = "com.google.android.documentsui:id/item_root"))
        actions.add(Action.Delay(4))
/*        var mmsHelper=MmsHelper()
        mmsHelper.clickListViewItem(1)*/
        //actions.add(Action.Click(Selector.ByText("1.aac")))
        actions.add(Action.Click(Selector.ByText("1.mp4"),stepName = "Select Video from internal Storage"))
        //actions.add(Action.Click(Selector.ByText("1.mp4")))
        actions.add(Action.Delay(1))
        // actions.add(Action.Click(Selector.ByText("SIM1")))
        //actions.add(Action.Delay(2000))

        /*actions.add(Action.Click(By.res("com.google.android.apps.messaging:id/full_screen_gallery_item_icon")))
        actions.add(Action.Delay(1000))
        actions.add(Action.Click(By.res("com.google.android.apps.messaging:id/gallery_content_async_image")))
        actions.add(Action.Delay(1000))
        actions.add(Action.Click(By.res("com.google.android.apps.messaging:id/container_action_button")))
        actions.add(Action.Delay(1000))*/
        actions.add(Action.Click(Selector.ByRes("com.google.android.apps.messaging:id/send_message_button_icon")))
        actions.add(Action.Delay(40))

        actions.add(
            Action.GetTextListItemByIndex(
                Selector.ByRes("android:id/list"),
                0,
                "com.google.android.apps.messaging:id/conversation_message_view",
                1
                ,stepName = "Send video MMS Successfully ", testFlag = UtilsClass.SEND_Video_MMS)
        )

        return actions

    }
    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {
        super.actionClearRecentResult(count, result, stepName)
        /* if (stepName.isNotEmpty()) {
             report?.insertStep(stepName, if (result) "Pass" else "Fail")
         }*/
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
        StorageHandler.writeXLSFile(reportList, "MMS_sendVideo_MMs")
    }

    override fun actionListItemGetTextByindexResult(
        count: Int,
        reqSelector: Selector,
        result: String,
        stepName: String,
        testFlagName: String
    ) {
        if(stepName.isNotEmpty()) {
            Log.e("GetTextListItem", " TagName " + testFlagName + " result " + result)
            if (testFlagName.contains(UtilsClass.SEND_Video_MMS)) {
                if (result.contains("Sending")) {
                    report!!.insertStep(stepName, "Failed")
                } else {
                    report!!.insertStep(stepName, "Pass")
                }
            }
        }
        super.actionListItemGetTextByindexResult(count, reqSelector, result, stepName, testFlagName)
    }

}
