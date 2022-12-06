package org.sydnik.by.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.sydnik.by.framework.utils.ExcelUtil;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.sites.e_dostavka.data.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class EdostavkaExсel extends Thread {
    private Workbook workbook;
    private Sheet sheet;
    private ArrayBlockingQueue<Product> queue;
    private boolean work = true;

    public EdostavkaExсel(Workbook workbook, ArrayBlockingQueue<Product> queue) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet();
        this.queue = queue;
    }

    public EdostavkaExсel(Workbook workbook, Sheet sheet, ArrayBlockingQueue<Product> queue) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet();
        this.queue = queue;
    }

    public void addRowsWithProduct(List<Product> list){
        for (int i = 0; i < list.size(); i++) {
            addRowWithValue(list.get(i), i+1);
        }
    }

    public void addRowWithValue(Product product, int numberOfRow){
        Row row = ExcelUtil.createRow(sheet, numberOfRow);
        ExcelUtil.setCellInteger(row,0, product.getId());
        ExcelUtil.setCellValue(row,1, product.getName());
        ExcelUtil.setCellDouble(workbook, row,2, product.getPrice(), JsonUtil.getDataString("formatPrice"));
        ExcelUtil.setCellValue(row,3, product.getCountry());
        ExcelUtil.setCellValue(row,4, product.getSubdirectory());
        ExcelUtil.setCellValue(row,5, product.getDirectory());
        ExcelUtil.setCellDate(workbook, row, 6, product.getDate(), JsonUtil.getDataString("formatDate"));
    }

    @Override
    public void run() {
        try {
            ExcelUtil.addHeadOnTheTop(workbook, sheet, JsonUtil.getDataList("headsForExcel"));
            int rows = 1;
            while (work) {
                if (queue.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Product product = queue.take();
                        addRowWithValue(product, rows);
                        rows++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            ExcelUtil.setColumnsWidth(sheet, (ArrayList<String>) JsonUtil.getDataList("columnsWidth"));
            ExcelUtil.saveWorkBook( LocalDate.now().toString() + ".xlsx", workbook);
        }
    }

    public void setColumnWidth(){
        ExcelUtil.setColumnsWidth(sheet, (ArrayList<String>) JsonUtil.getDataList("columnsWidth"));
    }

    public void addHeads(){
        ExcelUtil.addHeadOnTheTop(workbook, sheet, JsonUtil.getDataList("headsForExcel"));
    }

    public void setWork(boolean work){
        this.work = work;
    }

}
