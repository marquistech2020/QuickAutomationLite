package com.marquistech.quickautomationlite.core

import android.util.Log
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.helpers.core.Helper
import org.junit.Test
import java.util.concurrent.CountDownLatch

abstract class TestFlow {

    // Abstract method
    protected abstract fun onCreateHelper(): Helper
    protected abstract fun onCreateScript(): List<Action>
    protected abstract fun onTestStart(testName: String)
    protected abstract fun onTestEnd(testName: String)
    protected abstract fun onStartIteration(testName: String, count: Int)
    protected abstract fun onEndIteration(testName: String, count: Int)

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
        reqSelector: Selector,
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

    protected open fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {}
    protected open fun actionCloseAppResult(count: Int, result: Boolean, stepName: String) {}


    val tag: String = javaClass.simpleName
    private lateinit var helper: Helper


    @Test
    fun mainTest() {
        val testLoop = onInitTestLoop()
        helper = onCreateHelper()
        val actions = onCreateScript()
        onTestStart(tag)
        (1..testLoop).forEach { count ->
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
    }

    private fun executeStepAndLatch(
        count: Int,
        action: Action,
        latch: CountDownLatch
    ) {

        when (action) {
            is Action.ClearRecentApps -> {
                writeLog(tag, "ClearRecentApps")
                actionClearRecentResult(count, helper.clearRecentApps(), action.stepName)
            }
            is Action.Click -> {
                writeLog(tag, "Click")
                val isDone =
                    helper.performClick(action.selector, action.position, action.isLongClick)
                actionClickResult(count, action.selector, isDone, action.stepName)
            }
            is Action.LaunchApp -> {
                writeLog(tag, "LaunchApp")
                val isDone = helper.launchApp(action.appSelector)
                helper.waitFor(2)
                actionLaunchAppResult(count, isDone, action.stepName)
                writeLog(tag, "LaunchApp end")
            }
            is Action.CloseApp -> {
                writeLog(tag, "CloseApp")
                val isDone = helper.closeApp(action.packageName)
                actionCloseAppResult(count, isDone, action.stepName)
            }
            is Action.Delay -> {

                if (action.milli > 0) {
                    writeLog(tag, "Delay  milli ${action.milli}")
                    helper.waitDeviceForIdle((action.milli / 2).toLong())
                    helper.waitFor(action.milli.toLong())
                } else if (action.second > 0) {
                    writeLog(tag, "Delay seconds ${action.second}")
                    helper.waitDeviceForIdle(((action.second * 1000) / 2).toLong())
                    helper.waitFor((action.second * 1000).toLong())
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
                actionGetTextResult(count, action.selector, output, action.stepName)
            }
            is Action.SetText -> {
                writeLog(tag, "SetText")
                val isDone = helper.performSetText(action.selector, action.text)
                actionSetTextResult(count, action.selector, isDone, action.stepName)
            }
            is Action.SendEvent -> {
                writeLog(tag, "SendEvent")
                val isDone = helper.performSendEvent(action.type)
                actionSendEventResult(count, action.type, isDone, action.stepName)
            }

            is Action.Swipe -> {
                writeLog(tag, "Swipe")
                val isDone = helper.performSwipe(action.coordinate, action.steps)
                actionSwipeResult(count, isDone, action.stepName)
            }
            is Action.Switch -> {
                writeLog(tag, "Switch")
                val isDone = helper.performSwitch(action.selector)
                actionSwitchResult(count, action.selector, isDone, action.stepName)
            }
        }

        latch.countDown()
    }


}