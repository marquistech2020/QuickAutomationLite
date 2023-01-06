package com.marquistech.quickautomationlite.data

import android.os.Build
import android.os.Environment
import android.util.Log
import android.util.Xml
import com.marquistech.quickautomationlite.BuildConfig
import com.marquistech.quickautomationlite.data.reports.Report
import org.apache.poi.hssf.usermodel.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import java.io.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.*
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
            titleCell.setCellValue("${testName.replace("_", " ").uppercase()} TEST REPORT")
            titleCell.setCellStyle(getHeaderStyle(wb))
            titleRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
            sheet.addMergedRegion(CellRangeAddress(0, 0, 3, 7))

            val desc =
                " Place : Noida \n Brand : ${Build.BRAND} \n Model : ${Build.MODEL} \n Date : ${
                    Date(
                        System.currentTimeMillis()
                    )
                }"

            val descRow: HSSFRow = sheet.createRow(1)
            val descCell = descRow.createCell(3)
            descCell.setCellValue(desc)
            descCell.setCellStyle(getHeaderDescStyle(wb))

            // increase row height to accommodate three lines of text
            descRow.heightInPoints = 4 * sheet.defaultRowHeightInPoints
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

                rowNo += 3 + reportList.size

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


    fun addRowXLSFileIterator(
        iteration: Int,
        testName: String,
        stepName: String,
        status: String,
        fileName: String
    ) {
        getLogsDirectory()?.let { dir ->
            val excelFileName =
                dir.absolutePath + "/" + fileName + ".xls" //name of excel file
            var file = File(excelFileName)
            if (file.exists()) {
                var wb = HSSFWorkbook(FileInputStream(excelFileName))
                val sheet: HSSFSheet = wb.getSheet("Sheet1")
                var lastRowNo: Int = sheet.lastRowNum
                Log.e("LastRow", "LastRow No" + lastRowNo)


                var cellNo = 0
                val row: HSSFRow = sheet.createRow(lastRowNo + 1)  // Header Row

                val cellFirst: HSSFCell = row.createCell(cellNo)
                cellFirst.setCellValue("" + iteration)
                //cellFirst.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                cellNo += 1
                val cellSecond: HSSFCell = row.createCell(cellNo)
                cellSecond.setCellValue(stepName)
                //cellSecond.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                cellNo += 1
                val cellThird: HSSFCell = row.createCell(cellNo)
                cellThird.setCellValue(status)
                //cellSecond.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                cellNo += 1

                val fileOut = FileOutputStream(excelFileName)

                wb.write(fileOut)
                fileOut.flush()
                fileOut.close()
            } else {
                val sheetName = "Sheet1" //name of sheet
                val wb = HSSFWorkbook()
                val sheet: HSSFSheet = wb.createSheet(sheetName)

                val cs = wb.createCellStyle()
                cs.alignment = HorizontalAlignment.CENTER
                val titleRow: HSSFRow = sheet.createRow(0)
                val titleCell = titleRow.createCell(3)
                titleCell.setCellValue("${testName.replace("_", " ").uppercase()} TEST REPORT")
                titleCell.setCellStyle(getHeaderStyle(wb))
                titleRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(0, 0, 3, 7))

                val desc =
                    " Place : Noida \n Brand : ${Build.BRAND} \n Model : ${Build.MODEL} \n Date : ${
                        Date(
                            System.currentTimeMillis()
                        )
                    }"

                val descRow: HSSFRow = sheet.createRow(1)
                val descCell = descRow.createCell(3)
                descCell.setCellValue(desc)
                //descCell.setCellStyle(getHeaderDescStyle(wb))

                // increase row height to accommodate three lines of text
                descRow.heightInPoints = 4 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(1, 1, 3, 7))

                var cellNo = 0
                val row: HSSFRow = sheet.createRow(3)  // Header Row

                val cellFirst: HSSFCell = row.createCell(cellNo)
                cellFirst.setCellValue("ITERATION")
                cellFirst.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                cellNo += 1
                val cellSecond: HSSFCell = row.createCell(cellNo)
                cellSecond.setCellValue("Step Name")
                cellSecond.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)

                cellNo += 1
                val cellThird: HSSFCell = row.createCell(cellNo)
                cellThird.setCellValue("Status")
                cellThird.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                //cellNo += stepColNo


                val fileOut = FileOutputStream(excelFileName)

                wb.write(fileOut)
                fileOut.flush()
                fileOut.close()
            }
//Do something


        }
    }

    fun writeXLSFileIterator(testName: String, fileName: String) {

        // var createSheet:HSSFSheet=
        getLogsDirectory()?.let { dir ->
            val excelFileName =
                dir.absolutePath + "/" + fileName + ".xls" //name of excel file
            if (File(excelFileName).exists()) {
                val sheetName = "Sheet1" //name of sheet
                val wb = HSSFWorkbook()
                val sheet: HSSFSheet = wb.createSheet(sheetName)

                val cs = wb.createCellStyle()
                cs.alignment = HorizontalAlignment.CENTER
                val titleRow: HSSFRow = sheet.createRow(0)
                val titleCell = titleRow.createCell(3)
                titleCell.setCellValue("${testName.replace("_", " ").uppercase()} TEST REPORT")
                titleCell.setCellStyle(getHeaderStyle(wb))
                titleRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(0, 0, 3, 7))

                val desc =
                    " Place : Noida \n Brand : ${Build.BRAND} \n Model : ${Build.MODEL} \n Date : ${
                        Date(
                            System.currentTimeMillis()
                        )
                    }"

                val descRow: HSSFRow = sheet.createRow(1)
                val descCell = descRow.createCell(3)
                descCell.setCellValue(desc)
                //descCell.setCellStyle(getHeaderDescStyle(wb))

                // increase row height to accommodate three lines of text
                descRow.heightInPoints = 4 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(1, 1, 3, 7))

                var cellNo = 0
                val row: HSSFRow = sheet.createRow(3)  // Header Row

                val cellFirst: HSSFCell = row.createCell(cellNo)
                cellFirst.setCellValue("ITERATION")
                cellFirst.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                cellNo += 1
                val cellSecond: HSSFCell = row.createCell(cellNo)
                cellSecond.setCellValue("Step Name")
                cellSecond.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)

                cellNo += 1
                val cellThird: HSSFCell = row.createCell(cellNo)
                cellThird.setCellValue("Status")
                cellThird.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                //cellNo += stepColNo


                val fileOut = FileOutputStream(excelFileName)

                wb.write(fileOut)
                fileOut.flush()
                fileOut.close()
            } else {

            }
        }


    }

    fun createTestCaseLogFile(
        iteration: Int,
        testName: String,
        stepName: String,
        status: String,
        fileName: String
    ) {
        getLogsDirectory()?.let { dir ->
            val excelFileName =
                dir.absolutePath + "/" + fileName + ".xls" //name of excel file
            var file = File(excelFileName)
            if (file.exists()) {
                var wb = HSSFWorkbook(FileInputStream(excelFileName))
                val sheet: HSSFSheet = wb.getSheet("Sheet1")
                var lastRowNo: Int = sheet.lastRowNum
                Log.e("LastRow", "LastRow No" + lastRowNo)

                if (stepName.isNotBlank()) {
                    var cellNo = 0
                    val row: HSSFRow = sheet.createRow(lastRowNo + 1)  // Header Row

                    val cellFirst: HSSFCell = row.createCell(cellNo)
                    cellFirst.setCellValue("" + iteration)
                    //cellFirst.setCellStyle(getHeaderCellStyle(wb))
                    sheet.setColumnWidth(cellNo, 25 * 256)
                    cellNo += 1
                    val cellSecond: HSSFCell = row.createCell(cellNo)
                    cellSecond.setCellValue(stepName)
                    //cellSecond.setCellStyle(getHeaderCellStyle(wb))
                    sheet.setColumnWidth(cellNo, 25 * 256)
                    cellNo += 1
                    val cellThird: HSSFCell = row.createCell(cellNo)
                    cellThird.setCellValue(status)
                    //cellSecond.setCellStyle(getHeaderCellStyle(wb))
                    sheet.setColumnWidth(cellNo, 25 * 256)
                    cellNo += 1

                    val fileOut = FileOutputStream(excelFileName)

                    wb.write(fileOut)
                    fileOut.flush()
                    fileOut.close()
                }
            } else {
                val sheetName = "Sheet1" //name of sheet
                val wb = HSSFWorkbook()
                val sheet: HSSFSheet = wb.createSheet(sheetName)

                val cs = wb.createCellStyle()
                cs.alignment = HorizontalAlignment.CENTER
                val titleRow: HSSFRow = sheet.createRow(0)
                val titleCell = titleRow.createCell(3)
                titleCell.setCellValue("${testName.replace("_", " ").uppercase()} TEST REPORT")
                titleCell.setCellStyle(getHeaderStyle(wb))
                titleRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(0, 0, 3, 7))

                val desc =
                    " Place : Noida \n Brand : ${Build.BRAND} \n Model : ${Build.MODEL} \n Date : ${
                        Date(
                            System.currentTimeMillis()
                        )
                    }"

                val descRow: HSSFRow = sheet.createRow(1)
                val descCell = descRow.createCell(3)
                descCell.setCellValue(desc)
                //descCell.setCellStyle(getHeaderDescStyle(wb))

                // increase row height to accommodate three lines of text
                descRow.heightInPoints = 4 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(1, 1, 3, 7))

                var cellNo = 0
                val row: HSSFRow = sheet.createRow(3)  // Header Row

                val cellFirst: HSSFCell = row.createCell(cellNo)
                cellFirst.setCellValue("ITERATION")
                cellFirst.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                cellNo += 1
                val cellSecond: HSSFCell = row.createCell(cellNo)
                cellSecond.setCellValue("Step Name")
                cellSecond.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)

                cellNo += 1
                val cellThird: HSSFCell = row.createCell(cellNo)
                cellThird.setCellValue("Status")
                cellThird.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                //cellNo += stepColNo


                val fileOut = FileOutputStream(excelFileName)

                wb.write(fileOut)
                fileOut.flush()
                fileOut.close()
            }
//Do something


        }
    }


    fun createTestCaseLog2File(testName: String, fileName: String, report: Report?) {
        getLogsDirectory()?.let { dir ->
            val excelFileName =
                dir.absolutePath + "/" + fileName + ".xls" //name of excel file
            var file = File(excelFileName)
            if (file.exists()) {

                var wb = HSSFWorkbook(FileInputStream(excelFileName))
                val sheet: HSSFSheet = wb.getSheet("Sheet1")
                var lastRowNo: Int = sheet.lastRowNum
                Log.e("LastRow", "LastRow No" + lastRowNo)

                val hssRow: HSSFRow = sheet.createRow(lastRowNo+1)
                //   for (index in report!!.getColumnCount()){
                val hssCellFirst: HSSFCell = hssRow.createCell(0)
                val cs = wb.createCellStyle()
                cs.alignment = HorizontalAlignment.CENTER
                hssCellFirst.setCellStyle(cs)
                hssCellFirst.setCellValue("" + report!!.iteration)
                val columnValues: List<*> = report!!.getSteps().values.toList()
                columnValues.forEachIndexed { colNoIndex, any ->
                    val cell: HSSFCell = hssRow.createCell(colNoIndex + 1)
                    cell.setCellStyle(cs)
                    cell.setCellValue("" + any)
                }
                val hssCellLast: HSSFCell = hssRow.createCell(columnValues.size + 1)
                hssCellLast.setCellStyle(cs)
                hssCellLast.setCellValue("" + report!!.status)
                val fileOut = FileOutputStream(excelFileName)

                wb.write(fileOut)
                fileOut.flush()
                fileOut.close()
            } else {
                val sheetName = "Sheet1" //name of sheet
                val wb = HSSFWorkbook()
                val sheet: HSSFSheet = wb.createSheet(sheetName)

                val cs = wb.createCellStyle()
                cs.alignment = HorizontalAlignment.CENTER
                val titleRow: HSSFRow = sheet.createRow(0)
                val titleCell = titleRow.createCell(3)
                titleCell.setCellValue("${testName.replace("_", " ").uppercase()} TEST REPORT")
                titleCell.setCellStyle(getHeaderStyle(wb))
                titleRow.heightInPoints = 3 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(0, 0, 3, 7))

                val desc =
                    " Place : Noida \n Brand : ${Build.BRAND} \n Model : ${Build.MODEL} \n Date : ${
                        Date(
                            System.currentTimeMillis()
                        )
                    }"

                val descRow: HSSFRow = sheet.createRow(1)
                val descCell = descRow.createCell(3)
                descCell.setCellValue(desc)
                //descCell.setCellStyle(getHeaderDescStyle(wb))

                // increase row height to accommodate three lines of text
                descRow.heightInPoints = 4 * sheet.defaultRowHeightInPoints
                sheet.addMergedRegion(CellRangeAddress(1, 1, 3, 7))

                val stepNameList: List<*> = report!!.getSteps().keys.toList()

               var  rowNo = 3

                stepNameList.forEachIndexed { index, any ->
                    val stepRow: HSSFRow = sheet.createRow(rowNo + index)
                    val stepCell: HSSFCell = stepRow.createCell(0)
                    stepCell.setCellValue("STEP ${index + 1} : " + any)
                    sheet.addMergedRegion(CellRangeAddress(rowNo + index, rowNo + index, 0, 1))
                }

                var cellNo = 0
                var lastRowNo: Int = sheet.lastRowNum
                val row: HSSFRow = sheet.createRow(lastRowNo+1)  // Header Row


                val cellFirst: HSSFCell = row.createCell(cellNo)
                cellFirst.setCellValue("ITERATION")
                cellFirst.setCellStyle(getHeaderCellStyle(wb))
                sheet.setColumnWidth(cellNo, 25 * 256)
                val stepColNo = report?.getColumnCount()
                cellNo += 1
                (0..stepColNo!!).forEach { index ->
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
//Code Comment for test
                 lastRowNo = sheet.lastRowNum

                val hssRow: HSSFRow = sheet.createRow(lastRowNo+1)
                //   for (index in report!!.getColumnCount()){
                val hssCellFirst: HSSFCell = hssRow.createCell(0)
                //val cs = wb.createCellStyle()

                cs.alignment = HorizontalAlignment.CENTER
                cs.alignment = HorizontalAlignment.CENTER
                hssCellFirst.setCellStyle(cs)
                hssCellFirst.setCellValue("" + report!!.iteration)
                val columnValues: List<*> = report!!.getSteps().values.toList()
                columnValues.forEachIndexed { colNoIndex, any ->
                    val cell: HSSFCell = hssRow.createCell(colNoIndex + 1)
                    cell.setCellStyle(cs)
                    cell.setCellValue("" + any)
                }
                val hssCellLast: HSSFCell = hssRow.createCell(columnValues.size + 1)
                hssCellLast.setCellStyle(cs)
                hssCellLast.setCellValue("" + report!!.status)
//Code Comment for Test
                var totalPass = 0
                var totalFail = 0

                val fileOut = FileOutputStream(excelFileName)

                wb.write(fileOut)
                fileOut.flush()
                fileOut.close()
            }
//Do something


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

    fun getLogsDirectory(): File? {
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

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(): List<*> {
        var inps: InputStream? = null
        val parser: XmlPullParser = Xml.newPullParser()
        getLogsDirectory()?.let { dir ->
            val excelFileName =
                dir.absolutePath + "/Input_file.xml"
            inps = FileInputStream(excelFileName)
            inps?.use { inputStream ->
                //val parser: XmlPullParser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)
                parser.nextTag()

            }
        }
        return readFeed(parser, inps)


    }

    data class Entry(val title: String?, val summary: String?, val link: String?)

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser, inps: InputStream?): List<Entry> {
        val entries = mutableListOf<Entry>()

        try {


            // parser.require(XmlPullParser.START_TAG, "ns", "Input")
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                Log.e("XMLRead", "Name " + parser.name + " Text " + parser.nextText())

                if (parser.name == "mtNumber") {
                    inps?.let {
                        parser.setInput(inps, "09999999999")

                    }

                }
                // Starts by looking for the entry tag
                if (parser.name == "entry") {
                    //   entries.add(readEntry(parser))
                } else {
                    // skip(parser)
                }
            }
        } catch (e: Exception) {
            Log.e("XMLRead", "Error " + e.message)
        }
        return entries

    }


    fun writeCrash(Tag: String, e: Exception) {

        val c = Calendar.getInstance()
        val timestamp =
            c[Calendar.DAY_OF_MONTH].toString() + "-" + (c[Calendar.MONTH] + 1) + "-" + c[Calendar.YEAR] + " " + c[Calendar.HOUR_OF_DAY] + ":" + c[Calendar.MINUTE] + ":" + c[Calendar.SECOND]


        val result: Writer = StringWriter()
        val printWriter = PrintWriter(result)
        e.printStackTrace(printWriter)

        val stacktrace = "\n$timestamp $Tag\n\n$result"

        printWriter.close()

        val logDir = getLogsDirectory()

        logDir?.let {

            try {
                val gpxfile = File(it.absolutePath + "/crashlogs.txt")
                if (!gpxfile.exists()) {
                    gpxfile.createNewFile()
                }
                val bos = BufferedWriter(FileWriter(gpxfile, true))
                bos.append(stacktrace)
                bos.append("============================================================================================================")
                bos.newLine()
                bos.flush()
                bos.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }


    }


}