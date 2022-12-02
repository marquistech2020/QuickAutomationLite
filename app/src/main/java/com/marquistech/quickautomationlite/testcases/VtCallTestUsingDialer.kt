package com.marquistech.quickautomationlite.testcases

import android.content.Intent
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.CallHelper
import com.marquistech.quickautomationlite.helpers.core.Helper

class VtCallTestUsingDialer : TestFlow() {
    override fun onCreateHelper(): Helper {
        return CallHelper()
    }

    companion object {
        private const val FLIP_CAMERA_TEXT = "Flip camera"
    }

    override fun onInitTestLoop(): Int {
        return 5
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all apps from recent"))
        actions.add(
            Action.LaunchApp(
                AppSelector.ByAction(Intent.ACTION_DIAL),
                stepName = "Launch calling app"
            )
        )
        actions.add(Action.Delay(milli = 500))
        actions.addAll(dialNoActions("+917011998220".toCharArray()))
        //actions.addAll(dialNoActions("+919650108704".toCharArray()))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.Click(
                Selector.ByText("Video call"),
                stepName = "Initiate the call"
            )
        )
        actions.add(Action.Delay(2))
        actions.add(
            Action.GetText(
                Selector.ByText(FLIP_CAMERA_TEXT),
                stepName = "Video call established"
            )
        )
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.Click(
                Selector.ByRes("com.google.android.dialer:id/videocall_end_call"),
                stepName = "Disconnect the call"
            )
        )




        return actions
    }

    private val reportList = mutableListOf<Report>()
    private var report: Report? = null

    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 6)
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
        StorageHandler.writeXLSFile(reportList, "Video_call_using_dialer")
    }


    private fun dialNoActions(charArray: CharArray): List<Action> {
        val actions = mutableListOf<Action>()

        charArray.forEach {
            var str = ""
            if (charArray.first() == it) {
                str = "Dialing the Number ${String(charArray)}"
            }
            when (it) {
                '+' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/zero"),
                        isLongClick = true,
                        stepName = str
                    )
                )
                '0' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/zero"),
                        stepName = str
                    )
                )
                '1' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/one"),
                        stepName = str
                    )
                )
                '2' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/two"),
                        stepName = str
                    )
                )
                '3' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/three"),
                        stepName = str
                    )
                )
                '4' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/four"),
                        stepName = str
                    )
                )
                '5' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/five"),
                        stepName = str
                    )
                )
                '6' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/six"),
                        stepName = str
                    )
                )
                '7' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/seven"),
                        stepName = str
                    )
                )
                '8' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/eight"),
                        stepName = str
                    )
                )
                '9' -> actions.add(
                    Action.Click(
                        Selector.ByRes("com.google.android.dialer:id/nine"),
                        stepName = str
                    )
                )
            }
        }
        return actions
    }

}