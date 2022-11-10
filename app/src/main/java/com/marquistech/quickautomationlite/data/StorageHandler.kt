package com.marquistech.quickautomationlite.data

import android.os.Build
import android.os.Environment
import android.util.Log
import com.marquistech.quickautomationlite.BuildConfig
import com.marquistech.quickautomationlite.data.reports.Report
import org.apache.poi.hssf.usermodel.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import java.io.FileOutputStream
import java.util.*


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

            val cs = wb.createCellStyle()
            cs.alignment = HorizontalAlignment.CENTER


            val titleRow: HSSFRow = sheet.createRow(0)
            val titleCell = titleRow.createCell(3)
            titleCell.setCellValue("${testName.replace("_"," ").uppercase()} TEST REPORT")
            titleCell.setCellStyle(getHeaderStyle(wb))
            titleRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
            sheet.addMergedRegion(CellRangeAddress(0, 0, 3, 7))

            val desc =
                " Place : Noida \n Brand : ${Build.BRAND} \n Date : ${Date(System.currentTimeMillis())}"

            val descRow: HSSFRow = sheet.createRow(1)
            val descCell = descRow.createCell(3)
            descCell.setCellValue(desc)
            descCell.setCellStyle(getHeaderDescStyle(wb))

            // increase row height to accommodate three lines of text
            descRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
            sheet.addMergedRegion(CellRangeAddress(1, 1, 3, 7))


            var rowNo = 2


            if (reportList.isNotEmpty()) {

                var cellNo = 0
                val row: HSSFRow = sheet.createRow(rowNo)  // Header Row

                val cellFirst: HSSFCell = row.createCell(cellNo)
                cellFirst.setCellValue("ITERATION")
                cellFirst.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)

                val stepColNo = reportList[0].getColumnCount()


                cellNo += 1

                (0..stepColNo).forEach { index ->
                    val cell: HSSFCell = row.createCell(cellNo + index)
                    cell.setCellValue("STEP ${index + 1}")
                    cell.setCellStyle(getHeaderCellStyle(wb))
                    sheet.setColumnWidth(cellNo + index, 25 * 256)
                }

                cellNo += stepColNo

                val cellLast: HSSFCell = row.createCell(cellNo)
                cellLast.setCellValue("STATUS")
                cellLast.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(stepColNo, 25 * 256)


                rowNo += 1

                var totalPass = 0
                var totalFail = 0

                reportList.forEachIndexed { index, report ->
                    val hssRow: HSSFRow = sheet.createRow(rowNo + index)
                    val hssCellFirst: HSSFCell = hssRow.createCell(0)
                    hssCellFirst.setCellStyle(cs)
                    hssCellFirst.setCellValue("" + report.iteration)
                    val columnValues: List<*> = report.getSteps().values.toList()
                    columnValues.forEachIndexed { colNoIndex, any ->
                        val cell: HSSFCell = hssRow.createCell(colNoIndex + 1)
                        cell.setCellStyle(cs)
                        cell.setCellValue("" + any)
                    }

                    val hssCellLast: HSSFCell = hssRow.createCell(columnValues.size + 1)
                    hssCellLast.setCellStyle(cs)
                    hssCellLast.setCellValue("" + report.status)

                    if (report.status.contains("Pass")) totalPass++ else totalFail++

                    rowNo += 1
                }

                rowNo += 3

                val totalPassRow: HSSFRow = sheet.createRow(rowNo)
                val totalPassCell: HSSFCell = totalPassRow.createCell(0)
                totalPassCell.setCellValue("Total Pass : $totalPass")

                rowNo += 1

                val totalFailRow: HSSFRow = sheet.createRow(rowNo)
                val totalFailCell: HSSFCell = totalFailRow.createCell(0)
                totalFailCell.setCellValue("Total Fail : $totalFail")

                val stepNameList: List<*> = reportList[0].getSteps().keys.toList()

                rowNo += 3

                stepNameList.forEachIndexed { index, any ->
                    val stepRow: HSSFRow = sheet.createRow(rowNo + index)
                    val stepCell: HSSFCell = stepRow.createCell(0)
                    stepCell.setCellValue("STEP ${index + 1} : " + any)
                    sheet.addMergedRegion(CellRangeAddress(rowNo + index, rowNo + index, 0, 1))
                }


            }

            val fileOut = FileOutputStream(excelFileName)

            wb.write(fileOut)
            fileOut.flush()
            fileOut.close()
        }

    }

    private fun getHeaderStyle(wb: HSSFWorkbook): HSSFCellStyle {
        val style: HSSFCellStyle = wb.createCellStyle()
        val font: HSSFFont = wb.createFont()
        font.bold = true
        font.fontHeight = 400
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.bottomBorderColor = IndexedColors.BLACK.index
        style.setFont(font)
        return style
    }

    private fun getHeaderDescStyle(wb: HSSFWorkbook): HSSFCellStyle {
        val style: HSSFCellStyle = wb.createCellStyle()
        val font: HSSFFont = wb.createFont()
        font.bold = true
        font.fontHeight = 200
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.bottomBorderColor = IndexedColors.BLACK.index
        style.setFont(font)
        style.wrapText = true
        return style
    }

    private fun getHeaderCellStyle(wb: HSSFWorkbook): HSSFCellStyle {
        val style: HSSFCellStyle = wb.createCellStyle()
        val font: HSSFFont = wb.createFont()
        font.bold = true
        font.fontHeight = 200
        style.fillForegroundColor = IndexedColors.YELLOW.getIndex()
        style.fillPattern = FillPatternType.SOLID_FOREGROUND
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.setFont(font)
        style.wrapText = true
        return style
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