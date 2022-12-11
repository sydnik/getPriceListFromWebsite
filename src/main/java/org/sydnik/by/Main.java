package org.sydnik.by;

import org.apache.poi.ss.usermodel.Workbook;
import org.sydnik.by.excel.EdostavkaExсel;
import org.sydnik.by.framework.utils.ExcelUtil;
import org.sydnik.by.framework.utils.SQLUtil;
import org.sydnik.by.sites.e_dostavka.EDostavka;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sql.edostavka.EdostavkaSql;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {

        try {
            Boolean w = true;
            ArrayBlockingQueue<Product> queue = new ArrayBlockingQueue<Product>(100000);
            EdostavkaSql edostavkaSql = new EdostavkaSql(queue);
            edostavkaSql.start();
            EDostavka eDostavka = new EDostavka(queue);
            eDostavka.start();
            ConsoleBeta consoleBeta = new ConsoleBeta(edostavkaSql, eDostavka, w);
            consoleBeta.start();
            eDostavka.join();
            //добавть стоп сюды
            while (w) {
                if (queue.isEmpty() ) {
                    edostavkaSql.setWork(false);
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
