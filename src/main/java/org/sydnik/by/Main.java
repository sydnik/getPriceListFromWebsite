package org.sydnik.by;

import org.sydnik.by.sites.e_dostavka.EDostavka;
import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.Logger;

public class Main {
    private static long time;
    public static void main(String[] args) throws  ClassNotFoundException {
        time = System.currentTimeMillis();
        try {
            DriverUtil.getInstance();
            EDostavka.getAllPrices();

            Thread.sleep(1000);

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            System.out.println(DriverUtil.getCurrentUrl());
            DriverUtil.close();
            Logger.info(Class.forName("org.sydnik.by.Main"),"App ran for " + (System.currentTimeMillis()-time) + " ms");
        }

    }
}
