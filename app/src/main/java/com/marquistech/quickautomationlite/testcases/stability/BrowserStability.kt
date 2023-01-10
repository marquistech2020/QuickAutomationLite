package com.marquistech.quickautomationlite.testcases.stability

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.StabilityHelper
import com.marquistech.quickautomationlite.helpers.core.Helper

class BrowserStability : TestFlow() {


    override fun onInitTestLoop(): Int {
        return 1
    }

    override fun onCreateHelper(): Helper {
        return StabilityHelper()
    }


    override fun onPreCondition(): List<Action> {

        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.android.chrome")
            )
        )
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/home_button")))
        actions.add(Action.Delay(milli = 200))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/url_bar")))
        actions.add(Action.Delay(milli = 200))
        actions.add(Action.Click(Selector.ByText("Search or type web address")))
        actions.add(Action.Delay(milli = 200))
        actions.add(
            Action.SetText(
                Selector.ByText("Search or type web address"),
                "http://www.att.com"
            )
        )
        actions.add(Action.SendEvent(EventType.ENTER))
        actions.add(Action.Delay(5))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/menu_button")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByContentDesc("Bookmark")))
        actions.add(Action.Delay(milli = 200))

        return actions

    }

    private fun navigateToLink(stepName: String): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.android.chrome")
            )
        )
        actions.add(Action.Delay(2))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/home_button")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/menu_button")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/all_bookmarks_menu_id")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByText("Mobile bookmarks")))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Click(Selector.ByText("www.att.com"), stepName = stepName))
        actions.add(Action.Delay(10))
        return actions
    }


    private fun openTopWebSites(stepName: String, website: MutableList<String>): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/home_button")))
        actions.add(Action.Delay(milli = 200))
        actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/url_bar")))
        actions.add(Action.Delay(milli = 200))
        actions.add(Action.Click(Selector.ByText("Search or type web address")))
        actions.add(Action.Delay(milli = 200))
        website.forEach {
            if (website.indexOf(it) == 0) {
                actions.add(
                    Action.SetText(
                        Selector.ByText("Search or type web address"),
                        it
                    )
                )
                actions.add(Action.Delay(milli = 200))
                actions.add(Action.SendEvent(EventType.ENTER,stepName = "$stepName launching $it"))
                actions.add(Action.Delay(10))

            } else {
                actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/menu_button")))
                actions.add(Action.Delay(milli = 200))
                actions.add(Action.Click(Selector.ByContentDesc("New tab")))
                actions.add(Action.Delay(milli = 200))
                actions.add(Action.Click(Selector.ByRes("com.android.chrome:id/url_bar")))
                actions.add(Action.Delay(milli = 200))
                actions.add(Action.Click(Selector.ByText("Search or type web address")))
                actions.add(Action.Delay(milli = 200))
                actions.add(
                    Action.SetText(
                        Selector.ByText("Search or type web address"),
                        it
                    )
                )
                actions.add(Action.Delay(milli = 200))
                actions.add(Action.SendEvent(EventType.ENTER,stepName = "$stepName launching $it"))
                actions.add(Action.Delay(10))

            }

        }

        return actions
    }


    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all apps from recent"))
        actions.add(Action.Delay(milli = 500))
        actions.addAll(navigateToLink("Navigate to a link"))

        val websites = mutableListOf(
            "http://www.cricketwireless.com",
            "http://www.att.com",
            "http://www.amazon.com",
            "http://www.youtube.com",
            "http://www.cnn.com"
        )

        actions.addAll(openTopWebSites("Top Websites", websites))



        return actions
    }


    private val reportList = mutableListOf<Report>()
    private var report: Report? = null


    override fun onTestStart(testName: String) {
        reportList.clear()
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "browser_stability")
    }


    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 6)
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

    override fun actionSendEventResult(
        count: Int,
        reqCode: EventType,
        result: Boolean,
        stepName: String
    ) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(tag, "actionClickResult  $stepName  result $result")
    }

    override fun onEndIteration(testName: String, count: Int) {
        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }
    }

}