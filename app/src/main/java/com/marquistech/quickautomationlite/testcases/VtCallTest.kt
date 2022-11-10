package com.marquistech.quickautomationlite.testcases

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.VtCallReport
import com.marquistech.quickautomationlite.helpers.core.CallHelper
import com.marquistech.quickautomationlite.helpers.core.Helper

class VtCallTest : TestFlow() {


    override fun onCreateHelper(): Helper {
        return CallHelper()
    }

    companion object {
        private const val CONTACT_SEARCH_BAR_RES =
            "com.google.android.contacts:id/open_search_bar_text_view"
        private const val CONTACT_BUTTON_RES = "com.google.android.contacts:id/search_result_list"
        private const val VIDEO_BUTTON_RES = "com.google.android.contacts:id/verb_video"
        private const val FLIP_CAMERA_TEXT = "Flip camera"
        private const val PHONE_BOOK_CONTACT_NAME = "contact2"
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps())
        actions.add(Action.LaunchApp(AppSelector.ByPkg("com.google.android.contacts")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByRes(CONTACT_SEARCH_BAR_RES)))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.SetText(
                Selector.ByRes(CONTACT_SEARCH_BAR_RES),
                PHONE_BOOK_CONTACT_NAME
            )
        )
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByRes(CONTACT_BUTTON_RES)))
        actions.add(Action.Click(Selector.ByRes(VIDEO_BUTTON_RES)))
        actions.add(Action.Delay(2))
        actions.add(Action.GetText(Selector.ByText(FLIP_CAMERA_TEXT)))
        actions.add(Action.Delay(milli = 500))


        return actions
    }

    private var report: VtCallReport? = null

    override fun onStartIteration(testName: String, count: Int) {
        report = VtCallReport(count)
    }


    override fun actionClickResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {


        writeLog(tag, "actionClickResult result $result")
    }

    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {


        writeLog(tag, "actionGetTextResult  result $result")
    }


    override fun onEndIteration(testName: String, count: Int) {
        writeLog(tag, "onEndIteration  report $report")
    }

    override fun onTestStart(testName: String) {

    }

    override fun onTestEnd(testName: String) {

    }
}