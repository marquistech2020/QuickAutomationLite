package com.marquistech.quickautomationlite

import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.helpers.ApnOppoHelper

class ApnTest : TestFlow<ApnOppoHelper>() {

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Click(Selector.ByText("abc"), position = 1))
        return actions
    }

    override fun onStartIteration(testName: String, count: Int) {
        TODO("Not yet implemented")
    }

    override fun onEndIteration(testName: String, count: Int) {
        TODO("Not yet implemented")
    }
}