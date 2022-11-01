package com.marquistech.quickautomationlite.actions

import androidx.test.uiautomator.BySelector
import java.nio.channels.Selector

sealed class Action {
    data class Delay(val time: Long) : Action()
    data class LaunchPackage(val packageName: String, val isLauncher: Boolean) : Action()
    object Home : Action()
    object ClearRecentApps : Action()

    data class SwitchOn(val bySelector: BySelector, val position: Int) : Action()
    data class SwitchOFF(val bySelector: BySelector, val position: Int) : Action()
    data class Click(val bySelector: BySelector) : Action()


}


