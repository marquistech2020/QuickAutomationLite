
import android.provider.Settings.ACTION_WIFI_SETTINGS
import android.util.Log
import com.marquistech.quickautomationlite.core.*
import com.marquistech.quickautomationlite.data.StorageHandler
import com.marquistech.quickautomationlite.data.StorageHandler.writeLog
import com.marquistech.quickautomationlite.data.reports.Report
import com.marquistech.quickautomationlite.helpers.core.CordinateHelper
import com.marquistech.quickautomationlite.helpers.core.Helper
import com.marquistech.quickautomationlite.helpers.core.WifiEnbDsbHelper
import com.marquistech.quickautomationlite.testcases.SendEmail

/**
 * Created by Ashutosh on 09,November,2022,
 */
class WifiEnbDsb : TestFlow() {


    override fun onCreateHelper(): Helper {
        return WifiEnbDsbHelper()
    }
    companion object {
        private const val WIFI_CONNECTED_SUCESSFULLY = "Android-Wifi"
    }
    override fun onInitTestLoop(): Int {
        return 10
    }

    override fun onCreateScript(): List<Action> {
        val actions = mutableListOf<Action>()
        actions.add(Action.SendEvent(EventType.HOME))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.SendEvent(EventType.RECENT_APP))
        actions.add(Action.Delay(milli = 500))
        actions.add(Action.ClearRecentApps("Clear all Apps from Recent"))
        actions.add(Action.Delay(second = 1))
        /*
        actions.add(
            Action.LaunchApp(
                AppSelector.ByAction(ACTION_WIFI_SETTINGS),
                stepName = "Launch WIfi App"
            )
        )

         */
        actions.add(
            Action.LaunchApp(
                AppSelector.ByPkg("com.oplus.wirelesssettings"),
                stepName = "Launch WIfi App"
            ))

        // actions.add(getItemAddNetwork())
        actions.add(Action.Swipe(CordinateHelper.SWIPE_DW,5))
        actions.add(Action.Click(Selector.ByText("Add network")))
        actions.add(Action.Delay(second = 2))
        actions.add(
            Action.SetText(
                Selector.ByText("Hotspot name(required)"),
                "Android-Wifi"

            )


        )
        actions.add(Action.SendEvent(EventType.ENTER))
        actions.add(Action.Delay(second = 2))
        actions.add(Action.Click(Selector.ByText("Security")))
        actions.add(Action.Click(Selector.ByText("None")))
        actions.add(Action.Click(Selector.ByRes("com.oplus.wirelesssettings:id/menu_save")))
        actions.add(Action.Delay(second = 10))

        actions.add(
            Action.Click(
                Selector.ByRes("android:id/summary"),

            )
        )
        actions.add(
            Action.GetText(
                Selector.ByText(WifiEnbDsb.WIFI_CONNECTED_SUCESSFULLY),
                stepName = "Wifi has connected Sucessfully"
            ),
        )
        Log.e(tag, "Add summary")
        actions.add(Action.Delay(second = 2))
        actions.add(Action.Swipe(CordinateHelper.SWIPE_UP,4))
        actions.add((Action.Click(Selector.ByText("Android-Wifi"))))

        actions.add((Action.Click(Selector.ByText("Remove this network"))))
        actions.add(
            Action.Click(
                Selector.ByRes("android:id/button1"),
                stepName = " WIFI network has removed successfully"
            )
        )
        return actions
    }

    private val reportList = mutableListOf<Report>()
    private var report: Report? = null
    override fun onStartIteration(testName: String, count: Int) {
        report = Report(count,4)


    }

    override fun actionClearRecentResult(count: Int, result: Boolean, stepName: String) {
        super.actionClearRecentResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionClearRecentResult  result $result")
    }

    override fun actionLaunchAppResult(count: Int, result: Boolean, stepName: String) {
        super.actionLaunchAppResult(count, result, stepName)
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionLaunchAppResult  result $result")
    }

    override fun actionClickResult(
        count: Int,
        reqSelector: Selector,
        result: Boolean,
        stepName: String
    ) {
        if (stepName.isNotEmpty()) {
            report?.insertStep(stepName, if (result) "Pass" else "Fail")
        }
        writeLog(tag, "actionClickResult result $result")
    }
    override fun actionGetTextResult(
        count: Int,
        result: String,
        stepName: String
    ) {
        val requestText = result.split("#").first()
        val resultText = result.split("#").last()
        if (stepName.isNotEmpty() &&requestText == WifiEnbDsb.WIFI_CONNECTED_SUCESSFULLY) {
            report?.insertStep(stepName, if (resultText.isNotEmpty()) "Pass" else "Fail")
        }
        writeLog(tag, "actionGetTextResult  result $result")

    }


    override fun onEndIteration(testName: String, count: Int) {
        val isFailed = report?.getSteps()?.values?.contains("Fail") ?: false
        report?.status = if (isFailed) "Fail" else "Pass"
        writeLog(tag, "onEndIteration  report $report")
        report?.let {
            reportList.add(it)
        }

    }

    override fun onTestStart(testName: String) {
        reportList.clear()
    }

    override fun onTestEnd(testName: String) {
        StorageHandler.writeXLSFile(reportList, "Wifi_Enb_dsb")
    }
}