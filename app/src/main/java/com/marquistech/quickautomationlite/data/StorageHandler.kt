package com.marquistech.quickautomationlite.data

import android.os.Build
import android.os.Environment
import android.util.Log
import com.marquistech.quickautomationlite.BuildConfig
import com.marquistech.quickautomationlite.data.reports.Report
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object StorageHandler {

    fun writeLog(tag: String, msg: String) {

        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun writeXLSFile(reportList: List<Report>, testName: String) {

        getLogsDirectory()?.let { dir ->
            val excelFileName =
                dir.absolutePath + "/" + testName + "_" + System.currentTimeMillis() + ".xls" //name of excel file
            val sheetName = "Sheet1" //name of sheet
            val wb = HSSFWorkbook()
            val sheet: HSSFSheet = wb.createSheet(sheetName)

            if (reportList.isNotEmpty()) {

                val row: HSSFRow = sheet.createRow(0)  // Header Row

                val cellFirst: HSSFCell = row.createCell(0)
                cellFirst.setCellValue("ITERATION")

                val stepColNo = reportList[0].getColumnCount() + 1

                (1..stepColNo).forEach {
                    val cell: HSSFCell = row.createCell(it)
                    cell.setCellValue("STEP $it")
                }
                val cellLast: HSSFCell = row.createCell(stepColNo)
                cellLast.setCellValue("STATUS")



                reportList.forEachIndexed { rowNo, report ->
                    val row: HSSFRow = sheet.createRow(rowNo + 1)
                    val cellFirst: HSSFCell = row.createCell(0)
                    cellFirst.setCellValue("" + report.iteration)
                    val columnValues: List<*> = report.getSteps().values.toList()
                    columnValues.forEachIndexed { colNo, any ->
                        val cell: HSSFCell = row.createCell(colNo + 1)
                        cell.setCellValue("" + any)
                    }
                    val cellLast: HSSFCell = row.createCell(columnValues.size + 1)
                    cellLast.setCellValue("" + report.status)
                }

            }

            val fileOut = FileOutputStream(excelFileName)

            wb.write(fileOut)
            fileOut.flush()
            fileOut.close()
        }

    }

    private fun getLogsDirectory(): File? {
        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/QLite")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return if (dir.exists()) {
            dir
        } else {
            null
        }
    }

}