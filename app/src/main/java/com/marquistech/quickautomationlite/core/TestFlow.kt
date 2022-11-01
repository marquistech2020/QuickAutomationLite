package com.marquistech.quickautomationlite.core

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.marquistech.quickautomationlite.actions.Action
import com.marquistech.quickautomationlite.actions.ActionHelper
import org.junit.Test
import java.util.concurrent.CountDownLatch

abstract class TestFlow : Core() {

    private val context: Context
    private val uiDevice: UiDevice
    private val actionHelper: ActionHelper


    init {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        uiDevice = UiDevice.getInstance(instrumentation)
        actionHelper = ActionHelper(uiDevice)
        TAG = javaClass.simpleName
    }

    abstract fun onCreateScript(): List<Action>
    abstract fun onStartIteration(testName: String, count: Int)
    abstract fun onEndIteration(testName: String, count: Int)


    @Test
    fun mainTest() {
        val actions = onCreateScript()

        (1..2).forEach { count ->
            onStartIteration("Wifi", count)
            Log.e(TAG, "################ Start Iteration $count  ################ ")
            actions.forEach {
                val latch = CountDownLatch(1)
                executeStepAndLatch(count, it, latch)
                latch.await()
            }
            Log.e(TAG, "################ End Iteration $count  ################ ")
            //Log.e(TAG, "Report == ${getReport()}")
            onEndIteration("Wifi", count)
        }

    }

    private fun executeStepAndLatch(
        count: Int,
        action: Action,
        latch: CountDownLatch
    ) {

        when (action) {

            is Action.Home -> {
                Log.e(TAG, "Home")
                actionHomeResult(count, uiDevice.pressHome())

            }
            is Action.ClearRecentApps -> {
                Log.e(TAG, "ClearRecentApps")
                actionClearRecentResult(count, uiDevice.pressRecentApps())
            }
            is Action.LaunchPackage -> {
                Log.e(TAG, "LAUNCH ${action.packageName}")
                val isDone = actionHelper.performLaunchPackage(context,action.packageName,action.isLauncher)
                actionLaunchPackageResult(count, isDone)
            }
            is Action.Click -> {
                Log.e(TAG, "CLICK  ${action.bySelector}")
                val isDone = actionHelper.performClick(action.bySelector)
                actionClickResult(count, isDone)
            }
            is Action.SwitchOn -> {
                Log.e(TAG, "Switch On ")
                val isDone = actionHelper.performSwitchOn(action)
                actionSwitchOnResult(count, isDone)
            }
            is Action.SwitchOFF -> {
                Log.e(TAG, "Switch Off")
                val isDone = actionHelper.performSwitchOff(action)
                actionSwitchOffResult(count, isDone)
            }
            is Action.Delay -> {
                Log.e(TAG, "Delay  ${action.time}")
                Thread.sleep(action.time)
            }
        }


        latch.countDown()
    }


}