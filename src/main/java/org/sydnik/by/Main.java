package org.sydnik.by;

import org.sydnik.by.sites.e_dostavka.EdostavkaMain;
import org.sydnik.by.sites.e_dostavka.enums.OperationMode;

public class Main {
    public static void main(String[] args) {
        EdostavkaMain edostavkaMain = new EdostavkaMain(OperationMode.SQL_TO_EXCEL);
        edostavkaMain.start();
        try {
            edostavkaMain.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
