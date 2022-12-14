package com.marquistech.quickautomationlite.core

import android.util.Log
import com.marquistech.quickautomationlite.callbacks.ResultCompleteCallback
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.helpers.core.Helper
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

abstract class TestFlow {

    // Abstract method
    protected abstract fun onCreateHelper(): Helper
    protected abstract fun onCreateScript(): List<Action>
    protected abstract fun onTestStart(testName: String)
    protected abstract fun onTestEnd(testName: String)
    protected abstract fun onStartIteration(testName: String, count: Int)
    protected abstract fun onEndIteration(testName: String, count: Int)
    var fileName:String=""
    var fileName2:String=""
    // override methods
    protected open fun onInitTestLoop(): Int {
        return 1
    }

    protected open fun actionSendEventResult(
        count: Int,
        reqCode: EventType,
        result: Boolean,
        stepName: String
    ) {
    }

    protected open fun actionClickResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {
    }

    protected open fun actionClickByCoordinateResult(
        count: Int,
        result: Boolean,
        stepName: String
    ) {
    }

    protected open fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionSwipeResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionDragResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionSetTextResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {
    }

    protected open fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
    }

    protected open fun actionSwitchResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {

    }

    protected open fun actionAdbCommandResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionEnableResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionCloseAppResult(count: Int, result: Boolean, stepName: String) {}
    open fun actionListItemClickByTextResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String,
        testFlagName: String
    ) {
    }

    open fun actionListItemClickByindexResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String,
        testFlagName: String
    ) {
    }

    open fun actionListItemGetTextByindexResult(
        count: Int,
        reqSelector: Selector,
        result: String,
        stepName: String,
        testFlagName: String
    ) {
    }

    open fun actionListItemClickResult(count: Int, reqSelector: Selector, result: Boolean) {}
    open fun actionListItemClickByindexResult(count: Int, reqSelector: Selector, result: Boolean) {}
    protected open fun onPreCondition(): List<Action> {
        return emptyList()
    }

    fun getLatLng(callback: ResultCompleteCallback<String>) =
        helper.getCurrentAddress(true, callback)

    fun getCurrentAddress(callback: ResultCompleteCallback<String>) =
        helper.getCurrentAddress(false, callback)

    protected open fun actionSwitchToEachAppResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionScrollResult(count: Int, result: Boolean, stepName: String) {}


    val tag: String = javaClass.simpleName
    var stepsCount = 0
    private val helper: Helper = onCreateHelper()


    private fun preconditionTest() {

        val actions = onPreCondition()

        if (actions.isNotEmpty()) {
            (1..1).forEach { count ->
                writeLog(tag, "################ PreCondition Start ################ ")
                actions.forEach {
                    val latch = CountDownLatch(1)
                    executeStepAndLatch(count, it, latch)
                    latch.await()
                }
                writeLog(tag, "################ PreCondition End ################ ")
            }
        }
    }

    @Test
    fun mainTest() {
        try {

            preconditionTest()
          //  val testLoop = onInitTestLoop()
            val testLoop = 1000
            val actions = onCreateScript()
            fileName = tag + "_Logs_" + System.currentTimeMillis()
            fileName2 = tag + "_Logs2_" + System.currentTimeMillis()
            onTestStart(tag)
            (1..testLoop).forEach { count ->
                helper.wakeDevice()
                onStartIteration(tag, count)
                writeLog(tag, "################ Start Iteration $count  ################ ")
                actions.forEach {
                    val latch = CountDownLatch(1)
                    executeStepAndLatch(count, it, latch)
                    latch.await()
                }
                writeLog(tag, "################ End Iteration $count  ################ ")
                //writeLog(tag, "Report == ${getReport()}")
                onEndIteration(tag, count)
            }
            onTestEnd(tag)
        } catch (e: Exception) {
            onTestEnd(tag)
            Log.e("MainTest", "Exception " + e.message)
            StorageHandler.writeCrash(tag,e)
        }
    }


    private fun executeStepAndLatch(
        count: Int,
        action: Action,
        latch: CountDownLatch
    ) {
        Log.e("CheckLog", "LogFileName_" + tag)
        when (action) {
            is Action.ClearRecentApps -> {
                writeLog(tag, "ClearRecentApps")
                val isDone = helper.clearRecentApps()
                actionClearRecentResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.Click -> {
                writeLog(tag, "Click")
                val isDone =
                    helper.performClick(action.selector, action.position, action.isLongClick)
                actionClickResult(count, action.selector, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.ClickBYCordinate -> {
                writeLog(tag, "ClickByAxis")
                val isDone = helper.performClickByCordinate(action.panelArea)
                actionClickByCoordinateResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.LaunchApp -> {
                writeLog(tag, "LaunchApp")
                val isDone = helper.launchApp(action.appSelector)
                helper.waitFor(2)
                actionLaunchAppResult(count, isDone, action.stepName)
                writeLog(tag, "LaunchApp end")
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.CloseApp -> {
                writeLog(tag, "CloseApp")
                val isDone = helper.closeApp(action.packageName)
                actionCloseAppResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.Delay -> {

                if (action.milli > 0) {
                    writeLog(tag, "Delay  milli ${action.milli}")
                    /*helper.waitDeviceForIdle((action.milli / 2).toLong())
                    helper.waitFor(action.milli.toLong())*/
                    helper.waitDevice(action.milli.toLong())
                    CountDownLatch(1).await(action.milli.toLong(), TimeUnit.MILLISECONDS)
                } else if (action.second > 0) {
                    writeLog(tag, "Delay seconds ${action.second}")
                    /*helper.waitDeviceForIdle(((action.second * 1000) / 2).toLong())
                    helper.waitFor((action.second * 1000).toLong())*/
                    helper.waitDevice(action.second.toLong())
                    CountDownLatch(1).await(action.second.toLong(), TimeUnit.SECONDS)
                }
            }
            is Action.Drag -> {
                writeLog(tag, "Drag")
                val isDone = helper.drag(action.coordinate)
                actionDragResult(count, isDone, action.stepName)
            }
            is Action.GetText -> {
                writeLog(tag, "GetText")
                val output = helper.performGetText(action.selector, action.position)
                actionGetTextResult(count, output, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    output,
                    fileName
                )
            }
            is Action.SetText -> {
                writeLog(tag, "SetText")
                val isDone = helper.performSetText(action.selector, action.text)
                actionSetTextResult(count, action.selector, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.SendEvent -> {
                writeLog(tag, "SendEvent")
                val isDone = helper.performSendEvent(action.type)
                actionSendEventResult(count, action.type, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }

            is Action.Swipe -> {
                writeLog(tag, "Swipe")
                val isDone = helper.performSwipe(action.coordinate,action.selector, action.steps)
                actionSwipeResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.Switch -> {
                writeLog(tag, "Switch")
                val isDone = helper.performSwitch(action.selector)
                actionSwitchResult(count, action.selector, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.ClickListItem -> {
                Log.e(tag, "Click")
                val isDone = helper.performListItemClickByText(
                    action.selector,
                    action.position,
                    action.itemClassname,
                    action.itemSearch,
                    action.testFlag
                )
                actionListItemClickByTextResult(
                    count,
                    action.selector,
                    isDone,
                    action.stepName,
                    action.testFlag
                )
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.ClickListItemByIndex -> {
                Log.e(tag, "Click")
                val isDone = helper.performListItemClickByIndex(
                    action.selector,
                    action.position,
                    action.itemClassname,
                    action.itemSearchIndex,
                    action.testFlag
                )
                actionListItemClickByindexResult(
                    count,
                    action.selector,
                    isDone,
                    action.stepName,
                    action.testFlag
                )
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.GetTextListItemByIndex -> {
                Log.e(tag, "GetTextByindex")
                val isDone = helper.performListItemGetTextByIndex(
                    action.selector,
                    action.position,
                    action.itemClassname,
                    action.itemSearchIndex,
                    action.testFlag
                )
                actionListItemGetTextByindexResult(
                    count,
                    action.selector,
                    isDone,
                    action.stepName,
                    action.testFlag
                )
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    isDone,
                    fileName
                )
            }
            is Action.SendAdbCommand -> {
                Log.e(tag, "SendAdbCommand")
                val isDone = helper.performActionUsingShell(action.command)
                actionAdbCommandResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )

            }
            is Action.SetEnable -> {
                Log.e(tag, "SetEnable")
                val isDone = helper.performEnable(action.type, action.enable)
                actionEnableResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.SwitchToEachApp -> {
                Log.e(tag, "SwitchToEachApp")
                val isDone = helper.performSwitchApp(action.loop, action.endToPackage)
                actionSwitchToEachAppResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
            is Action.Scroll -> {
                Log.e(tag, "Scroll")
                val isDone = helper.performScroll(action.scrollDirection)
                actionScrollResult(count, isDone, action.stepName)
                StorageHandler.createTestCaseLogFile(
                    count,
                    fileName,
                    action.stepName,
                    if (isDone) "Pass" else "failed",
                    fileName
                )
            }
        }

        latch.countDown()
    }


}