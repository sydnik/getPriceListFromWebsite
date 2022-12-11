package org.sydnik.by;

import org.sydnik.by.sites.e_dostavka.EdostavkaMain;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sites.e_dostavka.EdostavkaSql;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {

        try {
            Boolean w = true;
            ArrayBlockingQueue<Product> queue = new ArrayBlockingQueue<Product>(100000);
            EdostavkaSql edostavkaSql = new EdostavkaSql(queue);
            edostavkaSql.start();
            EdostavkaMain edostavkaMain = new EdostavkaMain(queue);
            edostavkaMain.start();
            ConsoleBeta consoleBeta = new ConsoleBeta(edostavkaSql, edostavkaMain, w);
            consoleBeta.start();
            edostavkaMain.join();
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
