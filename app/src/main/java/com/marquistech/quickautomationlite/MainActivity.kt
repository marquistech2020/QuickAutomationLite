package com.marquistech.quickautomationlite


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.marquistech.quickautomationlite.permission.PermissionActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.button1)

        val button2 = findViewById<Button>(R.id.button2)

        button1.setOnClickListener {
            Log.e("TAG","button")
            val intent = Intent(baseContext, PermissionActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


}