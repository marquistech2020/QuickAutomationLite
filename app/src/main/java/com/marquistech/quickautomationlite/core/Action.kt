package com.marquistech.quickautomationlite.core

sealed class Action {
    data class SendEvent(val type: EventType, val stepName: String = "") : Action()
    data class Delay(var second: Int = 0, var milli: Int = 0) : Action()
    data class Click(
        val selector: Selector,
        val position: Int = 0,
        var isLongClick: Boolean = false,
        var stepName: String = ""
    ) : Action()

    data class Swipe(var coordinate: Coordinate? = null,val steps: Int,var stepName: String = "",var selector: Selector? = null) :
        Action()

    data class Drag(val coordinate: Coordinate, val steps: Int, var stepName: String = "") :
        Action()

    data class SetText(val selector: Selector, val text: String, var stepName: String = "") :
        Action()

    data class GetText(val selector: Selector, val position: Int = 0, var stepName: String = "") :
        Action()

    data class LaunchApp(val appSelector: AppSelector, val stepName: String = "") : Action()
    data class CloseApp(val packageName: String, var stepName: String = "") : Action()
    data class Switch(val selector: Selector, var stepName: String = "") : Action()
    data class ClickListItem(
        val selector: Selector,
        val position: Int = 0,
        val itemClassname: String,
        val itemSearch: String,
        var stepName: String = "",
        val testFlag: String = ""
    ) : Action()

    data class ClickListItemByIndex(
        val selector: Selector,
        val position: Int = 0,
        val itemClassname: String,
        val itemSearchIndex: Int,
        var stepName: String = "",
        val testFlag: String = ""
    ) : Action()

    data class GetTextListItemByIndex(
        val selector: Selector,
        val position: Int = 0,
        val itemClassname: String,
        val itemSearchIndex: Int,
        var stepName: String = "",
        var testFlag: String = ""
    ) : Action()

    data class ClearRecentApps(val stepName: String = "") : Action()
    data class ClickBYCordinate(val panelArea: PanelArea, var stepName: String = "") : Action()
    data class SendAdbCommand(val command: String, var stepName: String = "") : Action()
    data class SetEnable(val type: Type, val enable: Boolean, var stepName: String = "") : Action()
    data class SwitchToEachApp(
        var loop: Int = 1,
        var stepName: String = "",
        var endToPackage: String = ""
    ) : Action()

    data class Scroll(val scrollDirection: ScrollDirection, var stepName: String = "") : Action()
}

sealed class Selector {
    data class ByRes(val resName: String) : Selector()
    data class ByPkg(val pkgName: String) : Selector()
    data class ByCls(val clsName: String) : Selector()
    data class ByText(val text: String) : Selector()
    data class ByContentDesc(val contentDesc: String) : Selector()
}

sealed class AppSelector {
    data class ByPkg(val pkgName: String) : AppSelector()
    data class ByAction(val actionName: String) : AppSelector()
    data class ByUri(val uriName: String) : AppSelector()
}


data class Coordinate(
    val startX: Int,
    val startY: Int,
    val endX: Int,
    val endY: Int
)

enum class EventType {
    HOME, BACK, RECENT_APP, ENTER, RECEIVE_CALL, SPACE
}

enum class ListItemEvent {
    Click, DRAG
}

enum class Type {
    WIFI,LISTEN_CALL
}

enum class PanelArea {
    RECEIVE_CALL
}

enum class ScrollDirection {
    UP, LEFT, RIGHT, DOWN
}




