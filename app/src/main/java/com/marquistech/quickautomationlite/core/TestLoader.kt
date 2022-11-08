package com.marquistech.quickautomationlite.core

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.marquistech.quickautomationlite.MainActivity
import com.marquistech.quickautomationlite.WatcherActivity
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog

object TestLoader {

    fun initSetup() {
        val mInstrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
        val data = Intent()
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, data)
        val monitor: Instrumentation.ActivityMonitor =
            mInstrumentation.addMonitor(WatcherActivity::class.java.name, null, false)


        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName(mInstrumentation.targetContext, MainActivity::class.java.name)
        mInstrumentation.startActivitySync(intent)

        val currentActivity: Activity =
            InstrumentationRegistry.getInstrumentation().waitForMonitor(monitor)
        val name = currentActivity.intent.getStringExtra("test")
        writeLog("Activity", "data $name")

        try {
            val testClass = Class.forName(name)
            val testObject = testClass.newInstance()
            testClass.getMethod("mainTest").invoke(testObject)
        } catch (ex: ClassNotFoundException) {
            println(ex.toString())
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        mInstrumentation.removeMonitor(monitor)
    }
}