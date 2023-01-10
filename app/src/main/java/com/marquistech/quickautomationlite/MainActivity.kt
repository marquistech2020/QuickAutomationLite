package com.marquistech.quickautomationlite


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.button1)

        val button2 = findViewById<Button>(R.id.button2)

        val telecomManager: TelecomManager =
            getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        val slotNo = 0

        button1.setOnClickListener {
            Log.e("TAG", "button")
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.action = Settings.ACTION_NETWORK_OPERATOR_SETTINGS
            startActivity(intent)
        }


    }


}