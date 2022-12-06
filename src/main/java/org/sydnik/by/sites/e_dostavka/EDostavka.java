package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sites.e_dostavka.forms.CatalogForm;
import org.sydnik.by.sites.e_dostavka.pages.MainPage;
import org.sydnik.by.sites.e_dostavka.pages.ProductsPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class EDostavka extends Thread {
    ArrayBlockingQueue<Product> queue;

    public EDostavka(ArrayBlockingQueue<Product> queue) {
        this.queue = queue;
    }

    public void run(){
        long time = System.currentTimeMillis();
        try {
            DriverUtil.getInstance();
            List<Product> list = new ArrayList<>();
            CatalogForm catalogForm = new CatalogForm();
            MainPage mainPage = new MainPage();
            DriverUtil.openURL("https://edostavka.by/");
            mainPage.closeAddress();
            mainPage.closeCookies();
            DriverUtil.scrollUpPage();
            catalogForm.open();
            catalogForm.openMenuItem("Продукты");
            int dirCount = catalogForm.getDirectoryCount();
            for (int j = 7; j <= 7; j++) {
                System.out.println(j + " dir");
                getPricesWithSubDirectories(list, j);
                queue.addAll(list);
                list = new ArrayList<>();
            }
        }
        finally {
            Logger.info(this.getClass(), DriverUtil.getCurrentUrl());
            Logger.info(this.getClass(),"App ran for " + (System.currentTimeMillis()-time) + " ms");
            DriverUtil.close();
        }
    }

    public void getPricesWithSubDirectories(List<Product> list, int directoryNum){
        ProductsPage productsPage = new ProductsPage();
        CatalogForm catalogForm = new CatalogForm();
        String directoryNow;
        catalogForm.open();
        catalogForm.openMenuItem("Продукты");
        catalogForm.openDirectory(directoryNum);
        int subCount = catalogForm.getSubdirectoryCount();
        for (int i = 1; i <= subCount; i++) {
            System.out.println(i + "sub");
            catalogForm.open();
            catalogForm.openMenuItem("Продукты");
            directoryNow = catalogForm.openDirectory(directoryNum);
            catalogForm.clickSubdirectory(i);
            DriverUtil.scrollUpPage();
            list.addAll(productsPage.getAllProducts(directoryNow));
        }
    }
}
