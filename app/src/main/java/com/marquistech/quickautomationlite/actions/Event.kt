package com.marquistech.quickautomationlite.actions

import androidx.test.uiautomator.BySelector

sealed class Action {
    data class Delay(val time: Long) : Action()
    data class LaunchPackage(val packageName: String, val isLauncher: Boolean) : Action()
    object Home : Action()
    object ClearRecentApps : Action()

    data class SwitchOn(
        var byClass: String = "",
        var byPackage: String = "",
        var byText: String = "",
        var isUiSelector: Boolean = false,
        val position: Int
    ) : Action()

    data class SwitchOFF(
        var byClass: String = "",
        var byPackage: String = "",
        var byText: String = "",
        var isUiSelector: Boolean = false,
        val position: Int
    ) : Action()

    data class Click(val bySelector: BySelector) : Action()


}


