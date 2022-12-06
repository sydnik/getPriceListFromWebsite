package org.sydnik.by.framework.utils;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static Workbook createWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        return workbook;
    }

    public static void saveWorkBook(String path, Workbook workbook){
        try (OutputStream fileOut = new FileOutputStream(path)) {
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Sheet createSheet(Workbook workbook){
        return workbook.createSheet();
    }

    public static Sheet createSheet(Workbook workbook, String name){
        return workbook.createSheet(name);
    }

    public static Row createRow(Sheet sheet, int row){
        return sheet.createRow(row);
    }

    public static void setCellValue(Row row, int columnInt, String value){
        Cell cell = row.createCell(columnInt);
        cell.setCellValue(String.valueOf(value));
    }

    public static Cell setCellInteger(Row row, int columnInt, int value){
        Cell cell = row.createCell(columnInt);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);
        return cell;
    }

    public static Cell setCellDouble(Workbook workbook, Row row, int columnInt, double value, String format){
        Cell cell = row.createCell(columnInt);
        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat(format));
        cell.setCellStyle(dateStyle);
        cell.setCellValue(value);
        return cell;
    }

    public static Cell setCellDate (Workbook workbook, Row row, int columnInt, LocalDate value, String format){
        Cell cell = row.createCell(columnInt);
        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat(format));
        cell.setCellStyle(dateStyle);
        cell.setCellValue(value);
        return cell;
    }

    public static Row addHeadOnTheTop(Workbook workbook, Sheet sheet, List<String> strings){
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        Row row = sheet.createRow(0);
        for (int i = 0; i < strings.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(strings.get(i));
        }
        return row;
    }

    public static void setColumnsWidth(Sheet sheet, ArrayList<String> columnsWidth){
        for (int i = 0; i < columnsWidth.size(); i++) {
            sheet.setColumnWidth(i, Integer.parseInt(columnsWidth.get(i)));
        }
    }

    public static void setColumnWidth(Sheet sheet, int column, int width){
            sheet.setColumnWidth(column, width);
    }

//    public static Row setCellDate()
}
