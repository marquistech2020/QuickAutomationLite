package com.marquistech.quickautomationlite.core

import android.util.Log
import com.marquistech.quickautomationlite.helpers.core.Helper
import org.junit.Test
import java.util.concurrent.CountDownLatch

abstract class TestFlow<T : Helper> {

    // Abstract method
    abstract fun onCreateScript(): List<Action>
    abstract fun onStartIteration(testName: String, count: Int)
    abstract fun onEndIteration(testName: String, count: Int)

    // override methods
    open fun actionSendEventResult(count: Int, reqCode: EventType, result: Boolean) {}
    open fun actionClickResult(count: Int, reqSelector: Selector, result: Boolean) {}
    open fun actionClearRecentResult(count: Int, result: Boolean) {}
    open fun actionSwipeResult(count: Int, result: Boolean) {}
    open fun actionDragResult(count: Int, result: Boolean) {}
    open fun actionSetTextResult(count: Int, reqSelector: Selector, result: Boolean) {}
    open fun actionGetTextResult(count: Int, reqSelector: Selector, result: String) {}
    open fun actionSwitchResult(count: Int, reqSelector: Selector, result: Boolean) {}
    open fun actionLaunchAppResult(count: Int, result: Boolean) {}
    open fun actionCloseAppResult(count: Int, result: Boolean) {}


    private lateinit var helper: T
    val tag: String = javaClass.simpleName


    @Test
    fun mainTest() {
        val actions = onCreateScript()

        (1..1).forEach { count ->
            onStartIteration(tag, count)
            Log.e(tag, "################ Start Iteration $count  ################ ")
            actions.forEach {
                val latch = CountDownLatch(1)
                executeStepAndLatch(count, it, latch)
                latch.await()
            }
            Log.e(tag, "################ End Iteration $count  ################ ")
            //Log.e(tag, "Report == ${getReport()}")
            onEndIteration(tag, count)
        }

    }

    private fun executeStepAndLatch(
        count: Int,
        action: Action,
        latch: CountDownLatch
    ) {

        when (action) {
            is Action.ClearRecentApps -> {
                Log.e(tag, "ClearRecentApps")
                actionClearRecentResult(count, helper.clearRecentApps())
            }
            is Action.Click -> {
                Log.e(tag, "Click")
                val isDone = helper.performClick(action.selector, action.position)
                actionClickResult(count, action.selector, isDone)
            }
            is Action.LaunchApp -> {
                Log.e(tag, "LaunchApp")
                val isDone = helper.launchApp(action.appSelector)
                helper.waitFor(2)
                actionLaunchAppResult(count, isDone)
                Log.e(tag, "LaunchApp end")
            }
            is Action.CloseApp -> {
                Log.e(tag, "CloseApp")
                val isDone = helper.closeApp(action.packageName)
                actionCloseAppResult(count, isDone)
            }
            is Action.Delay -> {

                if (action.milli > 0) {
                    Log.e(tag, "Delay  milli ${action.milli}")
                    helper.waitDeviceForIdle((action.milli / 2).toLong())
                    helper.waitFor(action.milli.toLong())
                } else if (action.second > 0) {
                    Log.e(tag, "Delay seconds ${action.second}")
                    helper.waitDeviceForIdle(((action.second * 1000) / 2).toLong())
                    helper.waitFor((action.second * 1000).toLong())
                }
            }
            is Action.Drag -> {
                Log.e(tag, "Drag")
                val isDone = helper.drag(action.coordinate)
                actionDragResult(count, isDone)
            }
            is Action.GetText -> {
                Log.e(tag, "GetText")
                val output = helper.performGetText(action.selector)
                actionGetTextResult(count, action.selector, output)
            }
            is Action.SetText -> {
                Log.e(tag, "SetText")
                val isDone = helper.performSetText(action.selector, action.text)
                actionSetTextResult(count, action.selector, isDone)
            }
            is Action.SendEvent -> {
                Log.e(tag, "SendEvent")
                val isDone = helper.performSendEvent(action.type)
                actionSendEventResult(count, action.type, isDone)
            }

            is Action.Swipe -> {
                Log.e(tag, "Swipe")
                val isDone = helper.performSwipe(action.coordinate, action.steps)
                actionSwipeResult(count, isDone)
            }
            is Action.Switch -> {
                Log.e(tag, "Switch")
                val isDone = helper.performSwitch(action.selector)
                actionSwitchResult(count, action.selector, isDone)
            }
        }

        latch.countDown()
    }


}