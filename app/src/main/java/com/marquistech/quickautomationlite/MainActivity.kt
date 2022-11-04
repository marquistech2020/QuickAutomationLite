package com.marquistech.quickautomationlite

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
/*
            val componentName = ComponentName(
                "com.marquistech.quickautomationlite.test",
                "androidx.test.runner.AndroidJUnitRunner"
            )
            val arguments = Bundle()
            arguments.putString("class", "com.marquistech.quickautomationlite.MainInstrumentedTest")


            val isStarted = startInstrumentation(
                componentName, null, arguments
            )


            Log.e("MA", " started $isStarted")
*/

            val intent = Intent()
            intent.apply {
                `package` = "com.google.android.contacts"
                action = Intent.ACTION_MAIN
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            /*val intent =
                baseContext.packageManager.getLaunchIntentForPackage("com.snehitech.browseme")
            intent!!.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
*/
            baseContext.startActivity(intent)
        }
    }
}