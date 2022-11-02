package com.marquistech.quickautomationlite.factory

import com.marquistech.quickautomationlite.helpers.ApnOppoHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.ApnHelper

class ActionFactory {

    fun getHelper(test: String, brand: String): Helper {

        return when (test) {
            "ApnTest" -> getBrandSpecificHelper(brand)
            else -> Helper()
        }

    }


    private fun getBrandSpecificHelper(brand: String): Helper {
        return when (brand) {
            "oppo" -> ApnOppoHelper()
            else -> ApnHelper()
        }
    }
}