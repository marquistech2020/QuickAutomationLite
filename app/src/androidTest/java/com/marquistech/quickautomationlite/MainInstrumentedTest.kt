package com.marquistech.quickautomationlite

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.marquistech.quickautomationlite.actions.Action
import com.marquistech.quickautomationlite.actions.ActionUtil
import com.marquistech.quickautomationlite.actions.AirplaneUtil
import com.marquistech.quickautomationlite.reports.WifiOnOffReport
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {

    private lateinit var mDevice: UiDevice
    private lateinit var actionUtil: ActionUtil
    private var context: Context? = null
    private var loop = 1


    @Before
    fun start() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        mDevice = UiDevice.getInstance(instrumentation)
        actionUtil = ActionUtil(mDevice)


    }

    @Test
    fun mainTest() {

        val bundle = InstrumentationRegistry.getArguments()
        loop = bundle.getString("loop")?.toInt() ?: 3
        Log.e(ActionUtil.TAG, "loop $loop")
        val actions = AirplaneUtil.getScript()


        (1..loop).forEach { count ->
            val report = WifiOnOffReport(count)
            Log.e(ActionUtil.TAG, "################ Start Iteration $count  ################ ")
            actions.forEach {
                val latch = CountDownLatch(1)
                actionUtil.setReport(report)
                executeStepAndLatch(it, latch)
                latch.await()
            }
            Log.e(ActionUtil.TAG, "################ End Iteration $count  ################ ")
            Log.e(ActionUtil.TAG, "Report == ${actionUtil.getReport()}")
        }


    }


    private fun executeStepAndLatch(
        action: Action,
        latch: CountDownLatch
    ) {

        when (action) {

            is Action.Home -> {
                Log.e(ActionUtil.TAG, "Home")
                actionUtil.pressHomeButton()
            }
            is Action.ClearRecentApps -> {
                Log.e(ActionUtil.TAG, "ClearRecentApps")
                actionUtil.clearRecent()
            }
            is Action.LaunchPackage -> {
                Log.e(ActionUtil.TAG, "LAUNCH ${action.packageName}")
                actionUtil.performLaunchPackage(context, action.packageName, action.isLauncher)
            }
            is Action.Click -> {
                Log.e(ActionUtil.TAG, "CLICK  ${action.bySelector}")
                actionUtil.performClick(action.bySelector)
            }
            is Action.SwitchOn -> {
                Log.e(ActionUtil.TAG, "Switch On ")
                actionUtil.performSwitchOn(action)
            }
            is Action.SwitchOFF -> {
                Log.e(ActionUtil.TAG, "Switch Off")
                actionUtil.performSwitchOff(action)
            }
            is Action.Delay -> {
                Log.e(ActionUtil.TAG, "Delay  ${action.time}")
                Thread.sleep(action.time)
            }

            else -> {
                Log.e(ActionUtil.TAG, "Action not implemented yet $action")
            }
        }


        latch.countDown()
    }


}