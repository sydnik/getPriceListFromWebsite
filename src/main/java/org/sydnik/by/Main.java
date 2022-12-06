package org.sydnik.by;

import org.apache.poi.ss.usermodel.Workbook;
import org.sydnik.by.excel.EdostavkaExсel;
import org.sydnik.by.framework.utils.ExcelUtil;
import org.sydnik.by.sites.e_dostavka.EDostavka;
import org.sydnik.by.sites.e_dostavka.data.Product;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {

        try {

            ArrayBlockingQueue<Product> queue = new ArrayBlockingQueue<Product>(100000);
            Workbook workbook = ExcelUtil.createWorkbook();
            EdostavkaExсel edostavkaExсel = new EdostavkaExсel(workbook, queue);
            edostavkaExсel.start();
            EDostavka eDostavka = new EDostavka(queue);
            eDostavka.start();
            eDostavka.join();
            while (true) {
                if (queue.isEmpty()) {
                    edostavkaExсel.setWork(false);
                    break;
                }else {
                    Thread.sleep(10000);
                }
            }




        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Завершился полностью");
        }

    }
}
