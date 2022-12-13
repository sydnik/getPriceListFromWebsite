package org.sydnik.by.sites.e_dostavka;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.sydnik.by.IStopThread;
import org.sydnik.by.framework.utils.ExcelUtil;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sites.e_dostavka.enums.OperationMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EdostavkaExсel extends Thread implements IStopThread {
    private Workbook workbook;
    private Sheet sheet;
    private boolean work = true;
    private OperationMode operationMode;
    private CellStyle dateCellStyle;
    private CellStyle priceCellStyle;

    public EdostavkaExсel(Workbook workbook, OperationMode operationMode) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet();
        this.operationMode = operationMode;
        this.dateCellStyle = ExcelUtil.getDateStyle(workbook, JsonUtil.getDataString("dateFormat"));
        this.priceCellStyle = ExcelUtil.getPriceStyle(workbook, JsonUtil.getDataString("priceFormat"));
    }

    public EdostavkaExсel(Workbook workbook, Sheet sheet, OperationMode operationMode) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.operationMode = operationMode;
        this.dateCellStyle = ExcelUtil.getDateStyle(workbook, JsonUtil.getDataString("dateFormat"));
        this.priceCellStyle = ExcelUtil.getPriceStyle(workbook, JsonUtil.getDataString("priceFormat"));
    }


    @Override
    public void run() {
        try {
            addHeads();
            int row = 1;
            while (work || !QueueUtil.isEmpty()) {
                if (QueueUtil.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Product product = QueueUtil.take();
                    addRowWithValue(product, row);
                    row++;
                }
            }
        } finally {
            setColumnWidth();
            ExcelUtil.saveWorkBook(LocalDate.now() + "edostavka" + ".xlsx", workbook);
        }
    }

    public void setColumnWidth() {
        if (operationMode == OperationMode.SITE_TO_EXCEL) {
            ExcelUtil.setColumnsWidth(sheet, (ArrayList<String>) JsonUtil.getDataList("columnsWidthFromSite"));
        } else if (operationMode == OperationMode.SQL_TO_EXCEL) {
            ExcelUtil.setColumnsWidth(sheet, (ArrayList<String>) JsonUtil.getDataList("columnsWidthFromSql"));
        }
    }

    public void addRowsWithProduct(List<Product> list) {
        for (int i = 0; i < list.size(); i++) {
            addRowWithValue(list.get(i), i + 1);
        }
    }

    public void addRowWithValue(Product product, int numberOfRow) {
        int column = 0;
        Row row = ExcelUtil.createRow(sheet, numberOfRow);
        if (operationMode == OperationMode.SQL_TO_EXCEL) {
            ExcelUtil.setCellInteger(row, column++, product.getId());
        }
        ExcelUtil.setCellInteger(row, column++, product.getItemCode());
        ExcelUtil.setCellValue(row, column++, product.getName());
        ExcelUtil.setCellDouble(row, column++, product.getPrice(), priceCellStyle);
        ExcelUtil.setCellValue(row, column++, product.getCountry());
        ExcelUtil.setCellValue(row, column++, product.getSubdirectory());
        ExcelUtil.setCellValue(row, column++, product.getDirectory());
        ExcelUtil.setCellDate(row, column++, product.getDate(), dateCellStyle);
        ExcelUtil.setCellValue(row, column++, String.valueOf(product.isFood()));
    }

    public void addHeads() {
        if (operationMode == OperationMode.SITE_TO_EXCEL) {
            ExcelUtil.addHeadOnTheTop(workbook, sheet, JsonUtil.getDataList("headsForExcelFromSite"));
        } else if (operationMode == OperationMode.SQL_TO_EXCEL) {
            ExcelUtil.addHeadOnTheTop(workbook, sheet, JsonUtil.getDataList("headsForExcelFromSql"));
        }
    }

    @Override
    public void stopThead() {
        work = false;
    }
}
