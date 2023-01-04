package com.marquistech

import android.Manifest
import android.os.Build


const val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"


fun getStoragePermissionName(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        arrayOf(MANAGE_EXTERNAL_STORAGE_PERMISSION)
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}


fun getContactsPermissionName(): Array<String> {
    return arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )
}

fun getLocationPermissionName(): Array<String> {
    return arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

fun getCallPermissionName(): Array<String> {
    return arrayOf(
        Manifest.permission.READ_PHONE_STATE,
    )
}



