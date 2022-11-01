package com.marquistech.quickautomationlite

import android.content.ComponentName
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

        }
    }
}