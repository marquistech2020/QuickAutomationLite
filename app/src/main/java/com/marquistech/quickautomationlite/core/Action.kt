package com.marquistech.quickautomationlite.core

sealed class Action {
    data class SendEvent(val type: EventType) : Action()
    data class Delay(var second: Int = 0,var milli:Int = 0) : Action()
    data class Click(val selector: Selector,val position:Int = 0) : Action()
    data class Swipe(val coordinate: Coordinate, val steps: Int) : Action()
    data class Drag(val coordinate: Coordinate, val steps: Int) : Action()
    data class SetText(val selector: Selector,val text: String) : Action()
    data class GetText(val selector: Selector) : Action()
    data class LaunchApp(val appSelector: AppSelector) : Action()
    data class CloseApp(val packageName: String) : Action()
    data class Switch(val selector: Selector) : Action()
    object ClearRecentApps : Action()
}

sealed class Selector {
    data class ByRes(val resName: String) : Selector()
    data class ByPkg(val pkgName: String) : Selector()
    data class ByCls(val clsName: String) : Selector()
    data class ByText(val text: String) : Selector()
}

sealed class AppSelector {
    data class ByPkg(val pkgName: String) : AppSelector()
    data class ByAction(val actionName: String) : AppSelector()
}


data class Coordinate(
    val startX: Int,
    val startY: Int,
    val endX: Int,
    val endY: Int
)

enum class EventType{
    HOME,BACK,RECENT_APP,ENTER,SPACE
}




