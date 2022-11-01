package com.marquistech.quickautomationlite.reports

import java.util.*

data class WifiOnOffReport(val count:Int,var onTime:Long = 0L,var onStatus:String = "false",var offTime:Long = 0L,var offStatus:String = "false"){
    override fun toString(): String {
        return "count = $count,onTime = ${Date(onTime)},onStatus = $onStatus offTime = ${Date(offTime)}, OffStatus= $offStatus"
    }
}
