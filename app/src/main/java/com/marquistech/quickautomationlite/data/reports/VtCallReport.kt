package com.marquistech.quickautomationlite.data.reports

data class VtCallReport(
    var iteration: Int,
    var callTime: Long = 0L,
    var callDuration: String = "00:00",
    var callType: String = "",
    var callToContact: String = "",
    var callToNo: String = "",
    var Sim: String = "",
    var status: String = ""
) {
    override fun toString(): String {
        return "iteration = $iteration" +
                " callTime = $callTime " +
                " callDuration = $callDuration " +
                " callType = $callType" +
                " callToConnect = $callToContact " +
                " callToNo = $callToNo " +
                " Sim = $Sim " +
                " status = $status"
    }
}