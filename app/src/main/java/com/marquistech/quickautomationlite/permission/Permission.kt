package com.marquistech.quickautomationlite.permission

import android.app.Activity
import android.app.AppOpsManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.marquistech.*
import com.marquistech.quickautomationlite.MainActivity
import com.marquistech.quickautomationlite.R


const val PERMISSION_EXTRA_REQ = 100
const val PERMISSION_STORAGE_REQ = 101

class PermissionActivity : AppCompatActivity() {

    private val statusMap = mutableMapOf(
        PERMISSION_STORAGE_REQ to false,
        PERMISSION_EXTRA_REQ to false
    )
    private var status: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val permissionStorages = getStoragePermissionName()

    private val permissionExtras = arrayOf(

        *getLocationPermissionName(),
        *getCallPermissionName(),

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        status.observe(this) {

            it?.let {
                if (it) {
                    initPermissionsCheck()
                }
            }


        }

        if (status.value == null) {
            status.postValue(true)
        }

    }

    private fun initPermissionsCheck() {
        if (!statusMap[PERMISSION_STORAGE_REQ]!!) {
            checkStoragePermissionApi(this, permissionStorages)
        } else if (!statusMap[PERMISSION_EXTRA_REQ]!!) {
            checkOtherPermissionApi(this, permissionExtras)
        } else {
           navigateToHome()
        }
    }


    private fun checkStoragePermissionApi(
        activity: Activity,
        permissionStorage: Array<String>
    ) {
        val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appOps = activity.getSystemService(AppOpsManager::class.java)
            val mode = appOps.unsafeCheckOpNoThrow(
                MANAGE_EXTERNAL_STORAGE_PERMISSION,
                activity.applicationInfo.uid,
                activity.packageName
            )

            mode == AppOpsManager.MODE_ALLOWED
        } else {
            hasPermissions(
                activity,
                permissionStorage
            )
        }

        statusMap[PERMISSION_STORAGE_REQ] = isGranted

        if (isGranted) {
            initPermissionsCheck()
        } else {
            requestForStoragePermission(this, permissionStorage)
        }
    }

    private fun requestForStoragePermission(activity: Activity, permissionStorage: Array<String>) {
        Log.e("TAG", "requestForStoragePermission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)

            activity.startActivityForResult(intent, PERMISSION_STORAGE_REQ)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                permissionStorage,
                PERMISSION_STORAGE_REQ
            )
        }
    }

    private fun checkOtherPermissionApi(activity: Activity, permissionExtra: Array<String>) {

        val isAllGranted = hasPermissions(activity, permissionExtra)

        statusMap[PERMISSION_EXTRA_REQ] = isAllGranted

        if (isAllGranted) {
            initPermissionsCheck()
        } else {
            ActivityCompat.requestPermissions(activity, permissionExtra, PERMISSION_EXTRA_REQ)
        }
    }


    private fun hasPermissions(activity: Activity?, permissions: Array<String>?): Boolean {
        if (activity != null && permissions != null) {
            for (i in permissions.indices) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        permissions[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_STORAGE_REQ,
            PERMISSION_EXTRA_REQ -> {
                // If request is cancelled, the result arrays are empty.
                var isStorageGranted = true
                if (grantResults.isNotEmpty()) {

                    var i = 0
                    while (i < permissions.size) {
                        if (permissions[i].equals(
                                permissions[i],
                                ignoreCase = true
                            ) && grantResults[i] != PackageManager.PERMISSION_GRANTED
                        ) {
                            isStorageGranted = false
                            statusMap[requestCode] = false

                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this,
                                    permissions[i]
                                )
                            ) {
                                //Show permission explanation dialog...
                                Log.e("TAG", "")
                                status.value = true
                                break
                            } else {
                                //Never ask again selected, or device policy prohibits the app from having that permission.
                                //So, disable that feature, or fall back to another situation...
                                status.value = false
                                showDialogOK(
                                    this,
                                    "All Permission required for this app "
                                ) { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            val intent = Intent()
                                            intent.action =
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            val uri = Uri.fromParts("package", packageName, null)
                                            intent.data = uri
                                            startActivity(intent)
                                            finish()
                                        }
                                        DialogInterface.BUTTON_NEGATIVE -> finish()
                                    }
                                }
                                break
                            }

                        }
                        i++
                    }


                    if (isStorageGranted) {
                        statusMap[requestCode] = true
                        status.value = true
                    }


                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PERMISSION_STORAGE_REQ -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        statusMap[PERMISSION_STORAGE_REQ] = true
                    }
                }

                status.value = true

            }
        }
    }


    private fun navigateToHome() {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showDialogOK(
        activity: AppCompatActivity?,
        message: String?,
        okListener: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(activity!!)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }



