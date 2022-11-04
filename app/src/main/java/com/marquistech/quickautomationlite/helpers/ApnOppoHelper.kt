package com.marquistech.quickautomationlite.helpers

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiSelector
import com.marquistech.quickautomationlite.core.Coordinate
import com.marquistech.quickautomationlite.core.Selector
import com.marquistech.quickautomationlite.helpers.core.Helper

class ApnOppoHelper: Helper() {

    override fun performClick(selector: Selector, position: Int): Boolean {

        if (selector is Selector.ByText){
            val bySelector = By.text(selector.text)
            val list = uiDevice.findObjects(bySelector)

            list[position].click()

            val ui = UiSelector().text(selector.text)
            uiDevice.findObject(ui).getChild(UiSelector().clickable(true).index(position)).click()
            return true
        }

        return false
    }

    override fun performSwipe(coordinate: Coordinate, steps: Int): Boolean {
        TODO("Not yet implemented")
    }
}