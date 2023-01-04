package com.marquistech.quickautomationlite.testcases.stability

import android.content.Intent
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.AdbCommand
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.call.StabilityHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import java.util.regex.Pattern

class MultiTaskingStability : TestFlow() {


    override fun onInitTestLoop(): Int {
        return 2000
    }
    override fun onCreateHelper(): Helper {
        return StabilityHelper()
    }

    private fun makePhoneCall(stepName: String): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.LaunchApp(AppSelector.ByAction(Intent.ACTION_DIAL)))
        actions.add(Action.Delay(1))
        actions.addAll(dialNoActions("+919821592522".toCharArray(), "com.google.android.dialer:id"))
        actions.add(Action.Delay(1))
        actions.add(Action.Click(Selector.ByContentDesc("dial")))
        actions.add(Action.Delay(10))
        actions.add(
            Action.GetText(
                Selector.ByRes("com.google.android.dialer:id/contactgrid_bottom_timer"),
                stepName = stepName
            )
        )
        return actions
    }

    private fun switchToRunningApplication(stepName: String, endToPackage: String): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(
            Action.SwitchToEachApp(
                loop = 2,
                stepName = stepName,
                endToPackage = endToPackage
            )
        )
        return actions
    }

    private fun startBrowserSession(stepName: String): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.android.chrome")
            )
        )
        actions.add(Action.Delay(2))
        actions.add(
            Action.Click(
                Selector.ByContentDesc("Home"),
                stepName = stepName
            )
        )
        return actions
    }


    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()

        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.addAll(makePhoneCall("Make a phone call"))
        actions.add(Action.Delay(1))
        actions.addAll(
            switchToRunningApplication(
                "Switch from the telephony application to each running application", ""
            )
        )
        actions.add(Action.Delay(2))
        actions.add(Action.SendAdbCommand(AdbCommand.COMMAND_END_CALL, "End the phone call"))
        actions.add(Action.Delay(2))
        actions.addAll(startBrowserSession("Start a browser session and load home page"))
        actions.add(Action.Delay(2))
        actions.addAll(
            switchToRunningApplication(
                "Switch from the browser application to each running application",
                "com.android.chrome"
            )
        )
        actions.add(Action.Delay(2))
        actions.add(
            Action.CloseApp(
                packageName = "com.android.chrome",
                stepName = "Close the browser"
            )
        )

        return actions
    }

    private val reportList = mutableListOf<Report>()
    private var report: Report? = null


    override fun onTestStart(testName: String) {
        reportList.clear()
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "multi_task_stability")
    }


    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count, 6)
    }

    override fun onEndIteration(testName: String, count: Int) {
        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }
    }

    private fun dialNoActions(charArray: CharArray, resIdPrefix: String): List<Action> {
        val actions = mutableListOf<Action>()

        charArray.forEach {

            when (it) {
                '+' -> {
                    actions.add(
                        Action.Click(
                            Selector.ByRes("$resIdPrefix/zero"),
                            isLongClick = true
                        )
                    )
                }
                '0' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/zero")
                    )
                )
                '1' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/one")
                    )
                )
                '2' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/two")
                    )
                )
                '3' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/three")
                    )
                )
                '4' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/four")
                    )
                )
                '5' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/five")
                    )
                )
                '6' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/six")
                    )
                )
                '7' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/seven")
                    )
                )
                '8' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/eight")
                    )
                )
                '9' -> actions.add(
                    Action.Click(
                        Selector.ByRes("$resIdPrefix/nine")
                    )
                )

            }
        }
        return actions
    }

    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
        val requestText = result.split("#").first()
        val resultText = result.split("#").last()
        if (stepName.isNotEmpty()) {
            if (Pattern.matches(("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\$"), resultText)) {
                report?.insertStep(stepName, "Pass")
            } else {
                report?.insertStep(stepName, "Fail")
            }
        }

        writeLog(tag, "actionGetTextResult  $stepName  result $resultText")
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

    override fun actionAdbCommandResult(count: Int, result: Boolean, stepName: String) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(tag, "actionClickResult  $stepName  result $result")
    }


    override fun actionSwitchToEachAppResult(count: Int, result: Boolean, stepName: String) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(tag, "actionSwitchToEachAppResult  $stepName  result $result")
    }

    override fun actionCloseAppResult(count: Int, result: Boolean, stepName: String) {

        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }

        writeLog(tag, "actionCloseAppResult  $stepName  result $result")
    }

}