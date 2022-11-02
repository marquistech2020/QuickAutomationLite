package com.marquistech.quickautomationlite.core

open class Flow {

    var tag:String = ""
    open fun actionHomeResult(count: Int, result: Boolean) {}
    open fun actionClearRecentResult(count: Int, result: Boolean) {}
    open fun actionLaunchPackageResult(count: Int, result: Boolean) {}
    open fun actionClickResult(count: Int, result: Boolean) {}
    open fun actionSwitchOnResult(count: Int, result: Boolean) {}
    open fun actionSwitchOffResult(count: Int, result: Boolean) {}
}

