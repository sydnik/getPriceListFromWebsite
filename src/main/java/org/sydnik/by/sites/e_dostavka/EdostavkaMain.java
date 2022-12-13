package org.sydnik.by.sites.e_dostavka;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sydnik.by.ConsoleBeta;
import org.sydnik.by.IStopThread;
import org.sydnik.by.framework.utils.SQLUtil;
import org.sydnik.by.sites.e_dostavka.enums.OperationMode;

public class EdostavkaMain extends Thread implements IStopThread {
    private OperationMode operationMode;
    private boolean work;


    public EdostavkaMain(OperationMode operationMode) {
        this.operationMode = operationMode;
        work = true;
    }

    public void run(){
        switch (operationMode){
            case SITE_TO_SQL: {
                getPricesFromSiteToSql();
                break;
            }
            case SITE_TO_EXCEL: {
                getPricesFromSiteToExcel();
                break;
            }
            case SQL_TO_EXCEL: {
                getPricesFromSqlToExcel();
                break;
            }
        }
    }

    private void getPricesFromSqlToExcel(){
        try {
            Workbook workbook = new XSSFWorkbook();
            EdostavkaSql edostavkaSql = new EdostavkaSql(operationMode);
            EdostavkaExсel edostavkaExсel = new EdostavkaExсel(workbook, operationMode);
            edostavkaSql.start();
            edostavkaExсel.start();
            ConsoleBeta.addThread(edostavkaExсel, edostavkaSql);
            edostavkaSql.join();
            while (work) {
                if (QueueUtil.isEmpty() ) {
                    ConsoleBeta.getInstance().stopThead();
                    break;
                }else {
                    Thread.sleep(10000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getPricesFromSiteToSql(){
        try {
            EdostavkaSql edostavkaSql = new EdostavkaSql(operationMode);
            edostavkaSql.start();
            EdostavkaSite edostavkaSite = new EdostavkaSite(4, 1 , 1);
            edostavkaSite.start();
            ConsoleBeta.addThread(edostavkaSql, edostavkaSite);
            edostavkaSite.join();
            while (work) {
                if (QueueUtil.isEmpty() ) {
                    ConsoleBeta.getInstance().stopThead();
                    break;
                }else {
                    Thread.sleep(10000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getPricesFromSiteToExcel(){
        try {
            Workbook workbook = new XSSFWorkbook();
            EdostavkaExсel edostavkaExсel = new EdostavkaExсel(workbook, operationMode);
            edostavkaExсel.start();
            EdostavkaSite edostavkaSite = new EdostavkaSite();
            edostavkaSite.start();
            ConsoleBeta.addThread(edostavkaExсel, edostavkaSite);
            edostavkaSite.join();
            while (work) {
                if (QueueUtil.isEmpty() ) {
                    ConsoleBeta.getInstance().stopThead();
                    break;
                }else {
                    Thread.sleep(1000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stopThead() {
         work = false;
    }
}
