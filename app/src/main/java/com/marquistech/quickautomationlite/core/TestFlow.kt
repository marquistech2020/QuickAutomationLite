package com.marquistech.quickautomationlite.core

import android.os.Build
import android.util.Log
import com.marquistech.quickautomationlite.factory.ActionFactory
import com.marquistech.quickautomationlite.helpers.core.Helper
import org.junit.Test
import java.util.concurrent.CountDownLatch

abstract class TestFlow : Flow() {

    private val helper: Helper

    init {
        tag = javaClass.simpleName
        val factory = ActionFactory()
        helper = factory.getHelper(tag, Build.BRAND)
    }

    abstract fun onCreateScript(): List<Action>
    abstract fun onStartIteration(testName: String, count: Int)
    abstract fun onEndIteration(testName: String, count: Int)


    @Test
    fun mainTest() {
        val actions = onCreateScript()

        (1..10).forEach { count ->
            onStartIteration("Wifi", count)
            Log.e(tag, "################ Start Iteration $count  ################ ")
            actions.forEach {
                val latch = CountDownLatch(1)
                executeStepAndLatch(count, it, latch)
                latch.await()
            }
            Log.e(tag, "################ End Iteration $count  ################ ")
            //Log.e(tag, "Report == ${getReport()}")
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
                Log.e(tag, "Home")
                actionHomeResult(count, helper.pressHome())

            }
            is Action.ClearRecentApps -> {
                Log.e(tag, "ClearRecentApps")
                actionClearRecentResult(count, helper.pressRecentApps())
            }
            is Action.LaunchPackage -> {
                Log.e(tag, "LAUNCH ${action.packageName}")
                val isDone = helper.performLaunchPackage(
                    action.packageName,
                    action.isLauncher
                )
                actionLaunchPackageResult(count, isDone)
            }
            is Action.Click -> {
                Log.e(tag, "CLICK  ${action.bySelector}")
                val isDone = helper.performClick(action.bySelector)
                actionClickResult(count, isDone)
            }
            is Action.SwitchOn -> {
                Log.e(tag, "Switch On ")
                val isDone = helper.performSwitchOn(action)
                actionSwitchOnResult(count, isDone)
            }
            is Action.SwitchOFF -> {
                Log.e(tag, "Switch Off")
                val isDone = helper.performSwitchOff(action)
                actionSwitchOffResult(count, isDone)
            }
            is Action.Delay -> {
                Log.e(tag, "Delay  ${action.time}")
                Thread.sleep(action.time)
            }
        }


        latch.countDown()
    }


}