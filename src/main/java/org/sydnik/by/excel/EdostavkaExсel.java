package org.sydnik.by.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.sydnik.by.framework.utils.ExcelUtil;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.sites.e_dostavka.data.Product;

import java.util.List;

public class EdostavkaExсel {
    private Workbook workbook;
    private Sheet sheet;

    public EdostavkaExсel(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet();
    }

    public EdostavkaExсel(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
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
}
