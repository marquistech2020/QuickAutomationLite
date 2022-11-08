package com.marquistech.quickautomationlite.data.reports

data class Report(val iteration: Int,private val colCount:Int,private val steps: MutableMap<String, Any> = mutableMapOf(),var status:String = "") {

    fun insertStep(stepName: String, result: Any) {
        steps[stepName] = result
    }

    fun getSteps():MutableMap<String,Any>{
        return steps
    }

    fun getColumnCount() = colCount
}