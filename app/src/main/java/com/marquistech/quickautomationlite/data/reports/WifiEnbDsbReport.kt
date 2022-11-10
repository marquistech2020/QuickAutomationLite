package com.marquistech.quickautomationlite.data.reports

/**
 * Created by Ashutosh on 09,November,2022,
 */
data class WifiEnbDsbReport(
    var iteration: Int,
    var AddNetwork: String = "",
    var Connected: String = "",
    var RemoveNetwork: String = "",
    var status: String = ""
) {
    override fun toString(): String {
        return "iteration = $iteration" +
                " AddNetwork = $AddNetwork " +
                " Connected = $Connected " +
                " RemoveNetwork = $RemoveNetwork" +
                " status = $status"
    }
}