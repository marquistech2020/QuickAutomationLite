package com.marquistech.quickautomationlite.actions

import android.provider.Settings
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector

object AirplaneUtil {


    private fun getApnSelector(): BySelector {
        return By.clazz("android.widget.Switch")
    }

    fun getScript(): List<Action> {

        val actions = mutableListOf<Action>()

        actions.add(Action.Home)
        actions.add(Action.ClearRecentApps)
        actions.add(Action.LaunchPackage(Settings.ACTION_AIRPLANE_MODE_SETTINGS, false))
        actions.add(Action.SwitchOn(getApnSelector(), 0))
        actions.add(Action.Delay(2000))
        actions.add(Action.SwitchOFF(getApnSelector(), 0))
        actions.add(Action.Delay(2000))

        return actions

    }
}