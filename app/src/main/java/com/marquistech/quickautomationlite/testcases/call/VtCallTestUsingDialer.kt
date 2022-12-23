package com.marquistech.quickautomationlite.testcases.call

import android.content.Intent
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.CallHelper
import com.marquistech.quickautomationlite.helpers.core.CordinateHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import java.util.regex.Pattern

class VtCallTestUsingDialer : TestFlow() {



    override fun onCreateHelper(): Helper {
        return CallHelper()
    }

    companion object {
        private const val FLIP_CAMERA_TEXT = "Flip camera"
    }

    override fun onInitTestLoop(): Int {
        return 10
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
        actions.add(Action.SetEnable(Type.WIFI, enable = false, stepName = "Disable wifi"))
        actions.add(Action.Delay(1))
        actions.addAll(dialNoActions("+917209732588".toCharArray(), "com.google.android.dialer:id"))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.Scroll(ScrollDirection.UP))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.Click(
                Selector.ByText("Video call"),
                stepName = "Initiate the video call"
            )
        )
        actions.add(Action.Delay(5))
        actions.add(
            Action.GetText(
                Selector.ByRes("com.google.android.dialer:id/contactgrid_bottom_timer"),
                stepName = "Call received by other side"
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
        report = Report(count, 8)
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
        if (stepName.isNotEmpty()){
            if (resultText == FLIP_CAMERA_TEXT){
                report?.insertStep(stepName, "Pass")
            }else if (Pattern.matches(("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\$"),resultText)){
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

    override fun actionEnableResult(count: Int, result: Boolean, stepName: String) {
        super.actionEnableResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionEnableResult  $stepName  result $result")

    }

    override fun onTestStart(testName: String) {
        reportList.clear()
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Video_call_using_dialer")
    }


    private fun dialNoActions(charArray: CharArray, resIdPrefix: String): List<Action> {
        val actions = mutableListOf<Action>()

        charArray.forEach {
            var str = ""
            if (charArray.first() == it) {
                str = "Dialing the Number ${String(charArray)}"
            }
            when (it) {
                '+' -> {
                    actions.add(Action.Delay(second = 1))
                    actions.add(
                        Action.Click(
                            Selector.ByRes("$resIdPrefix/zero"),
                            isLongClick = true,
                            stepName = str
                        )
                    )
                }
                '0' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/zero"),
                        stepName = str
                    )
                )
                '1' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/one"),
                        stepName = str
                    )
                )
                '2' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/two"),
                        stepName = str
                    )
                )
                '3' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/three"),
                        stepName = str
                    )
                )
                '4' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/four"),
                        stepName = str
                    )
                )
                '5' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/five"),
                        stepName = str
                    )
                )
                '6' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/six"),
                        stepName = str
                    )
                )
                '7' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/seven"),
                        stepName = str
                    )
                )
                '8' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/eight"),
                        stepName = str
                    )
                )
                '9' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/nine"),
                        stepName = str
                    )
                )
            }
        }
        return actions
    }

}