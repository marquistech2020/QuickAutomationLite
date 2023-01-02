package com.marquistech.quickautomationlite.helpers.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.telecom.TelecomManager
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.Until
import com.google.android.gms.location.*
import com.marquistech.quickautomationlite.callbacks.ResultCompleteCallback
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.AdbCommand

import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

open class Helper {

    val context: Context
    val uiDevice: UiDevice
    var tag: String
    private val fusedLocationClient: FusedLocationProviderClient


    init {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        uiDevice = UiDevice.getInstance(instrumentation)
        tag = javaClass.simpleName
    }

    private fun changeWifiSetting(enable: Boolean): Boolean {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

        return if (enable) {
            uiDevice.executeShellCommand("svc wifi enable")
            waitFor(2)
            writeLog(tag, "changeWifiSetting $enable")
            wifiManager.isWifiEnabled
        } else {
            uiDevice.executeShellCommand("svc wifi disable")
            waitFor(2)
            writeLog(tag, "changeWifiSetting $enable")
            wifiManager.isWifiEnabled.not()
        }

    }


    fun launchApp(
        appSelector: AppSelector
    ): Boolean {

        try {

            when (appSelector) {
                is AppSelector.ByAction -> {
                    val intent = Intent(appSelector.actionName).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    context.startActivity(intent)
                }
                is AppSelector.ByPkg -> {
                    val intent = Intent()
                    intent.apply {
                        `package` = appSelector.pkgName
                        action = Intent.ACTION_MAIN
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
                is AppSelector.ByUri -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.apply {
                        // `package` = appSelector.pkgName
                        // Comment

                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse(appSelector.uriName)
                    }
                    context.startActivity(intent)
                }
            }

            return true

        } catch (e: ActivityNotFoundException) {
            return false
        }
    }


    open fun waitFor(second: Int) {
        Thread.sleep((second * 1000).toLong())
    }

    open fun waitFor(milli: Long) {
        Thread.sleep(milli)
    }

    open fun waitDeviceForIdle(milli: Long) {
        uiDevice.waitForIdle(milli)
    }

    open fun clearRecentApps(): Boolean {
        return false
    }

    open fun performClick(selector: Selector, position: Int, lgClick: Boolean): Boolean {
        return false
    }

    open fun closeApp(packageName: String): Boolean {
        return false
    }

    open fun drag(coordinate: Coordinate): Boolean {
        return false
    }

    open fun performGetText(selector: Selector, position: Int): String {
        return ""
    }

    open fun performSetText(selector: Selector, text: String): Boolean {
        return false
    }

    fun performSendEvent(type: EventType): Boolean {
        return when (type) {
            EventType.HOME ->
                uiDevice.pressHome()
            EventType.BACK -> uiDevice.pressBack()
            EventType.RECENT_APP -> uiDevice.pressRecentApps()
            EventType.ENTER -> uiDevice.pressEnter()
            EventType.RECEIVE_CALL -> uiDevice.pressKeyCode(KeyEvent.KEYCODE_CALL)
            EventType.SPACE -> uiDevice.pressKeyCode(KeyEvent.KEYCODE_SPACE)
        }
    }

    open fun performSwipe(coordinate: Coordinate, steps: Int): Boolean {
        return uiDevice.swipe(
            coordinate.startX,
            coordinate.startY,
            coordinate.endX,
            coordinate.endY,
            steps
        )
    }

    open fun performSwitch(selector: Selector): Boolean {
        return false
    }
    open fun performListItemClickByText(selector: Selector, position: Int, itemClassname:String, itemSearch:String, testFlagName:String = ""): Boolean {
        return false
    }
    open fun performListItemClickByIndex(selector: Selector, position: Int,itemClassname:String,itemSearchIndex:Int,testFlag:String = ""): Boolean {
        return false
    }
    open fun dateFormate(dateStr:String): Date? {
        val knownPatterns: MutableList<SimpleDateFormat> = ArrayList<SimpleDateFormat>()
        knownPatterns.add(SimpleDateFormat("MM/d/yy, HH:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/dd/yy, HH:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/d/yyyy, HH:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/d/yy, hh:mm a"))
        knownPatterns.add(SimpleDateFormat("MM/d/yy, HH:mm "))
        knownPatterns.add(SimpleDateFormat("d/MM/yyyy, HH:mm"))
        knownPatterns.add(SimpleDateFormat("d/MM/yyyy, HH:mm a"))

        knownPatterns.add(SimpleDateFormat("d/MM/yyyy, hh:mm"))
        knownPatterns.add(SimpleDateFormat("dd/MM/yyyy, hh:mm"))
        knownPatterns.add(SimpleDateFormat("dd/MM/yyyy, hh:mm a"))
        knownPatterns.add(SimpleDateFormat("dd/MM/yyyy, HH:mm"))
        knownPatterns.add(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))
        knownPatterns.add(SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"))
        knownPatterns.add(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"))

        for (pattern in knownPatterns) {
            try {
                // Take a try
                return Date(pattern.parse(dateStr).getTime())
            } catch (pe: ParseException) {
                // Loop on
            }
        }
        System.err.println("No known Date format found: $dateStr")

        return null
    }
    fun performListItemEvent(listItemEvent: ListItemEvent ,uiObject: UiObject,steps: Int): Boolean {
        return when (listItemEvent) {
            ListItemEvent.Click ->
                uiObject.click()
            ListItemEvent.DRAG -> uiObject.dragTo(uiObject,steps)

        }
    }
    open fun performListItemGetTextByIndex(selector: Selector, position: Int, itemClassname:String, itemSearchIndex:Int, testFlagName:String): String {
        return ""
    }
    fun performClickByCordinate(x: Int, y: Int): Boolean {
        return uiDevice.click(x, y)
    }

    @SuppressLint("MissingPermission")
      fun performActionUsingShell(command: String): Boolean {

        var result = false

        val tm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        if (tm.isInCall) {
            uiDevice.executeShellCommand(command)

            if (command == AdbCommand.COMMAND_END_CALL) {
                result = tm.isInCall.not()
            }
        }
        return result
    }

    fun performEnable(type: Type, enable: Boolean): Boolean {
        return when (type) {
            Type.WIFI -> changeWifiSetting(enable)
        }
    }


    fun getCurrentAddress(isLatLng: Boolean, callback: ResultCompleteCallback<String>) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.create().setInterval((5 * 1000).toLong())
            .setFastestInterval((5 * 1000).toLong())
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0.locations.map { loc ->
                    var address = ""
                    if (isLatLng) {
                        address = "${loc.latitude}#${loc.longitude}"
                    } else {
                        val geocoder = Geocoder(context, Locale.getDefault())

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                            geocoder.getFromLocation(
                                loc.latitude,
                                loc.longitude,
                                1
                            ) {

                                address = it.toList()[0]?.let { address ->
                                    address.getAddressLine(0)
                                } ?: ""

                            }
                        } else {

                            val addresses: List<Address> = geocoder.getFromLocation(
                                loc.latitude,
                                loc.longitude,
                                1
                            ) as List<Address> // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            address = addresses[0].getAddressLine(0)


                        }
                    }

                    callback.onComplete(address)

                }
            }

        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            Executors.newSingleThreadExecutor(),
            locationCallback
        )

        //fusedLocationClient.removeLocationUpdates(locationCallback)


    }

    fun waitDevice(time:Long){
        uiDevice.wait(Until.hasObject(By.text("aaaaaaaaaa")),time)
    }

    open fun performSwitchApp(loop: Int, endToPackage: String): Boolean {
        return false
    }

    open fun performScroll(direction: ScrollDirection): Boolean {
        return false
    }

    fun wakeDevice(){
        try {
            uiDevice.wakeUp()
        }catch (e:Exception){

        }
    }


}

