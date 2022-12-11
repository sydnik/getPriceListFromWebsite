package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sites.e_dostavka.site.forms.CatalogForm;
import org.sydnik.by.sites.e_dostavka.site.pages.MainPage;
import org.sydnik.by.sites.e_dostavka.site.pages.ProductsPage;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class EdostavkaMain extends Thread {
    ArrayBlockingQueue<Product> queue;

    public EdostavkaMain(ArrayBlockingQueue<Product> queue) {
        this.queue = queue;
    }

    public void run(){
        long time = System.currentTimeMillis();
        try {
            DriverUtil.getInstance();
            MainPage mainPage = new MainPage();
            DriverUtil.openURL("https://edostavka.by/");
            mainPage.closeAddress();
            mainPage.closeCookies();
            getPricesFromMenuItems();
        }
        finally {
            Logger.info(this.getClass(),"App ran for " + (System.currentTimeMillis()-time) + " ms");
            Logger.info(this.getClass(), DriverUtil.getCurrentUrl());
            DriverUtil.close();
        }
    }

    public void getPricesFromMenuItems(){
        CatalogForm catalogForm = new CatalogForm();
        catalogForm.open();
        int countItem = catalogForm.getMenuItemCount();
        for (int i = 1; i <= countItem; i++) {
            Logger.info(this.getClass(), "Item: " + catalogForm.getMenuItemName(i));
            getPricesFromDirectories(i);
        }
    }

    public void getPricesFromDirectories(int numberOfMenuItem){
        CatalogForm catalogForm = new CatalogForm();
        catalogForm.open();
        catalogForm.openMenuItem(numberOfMenuItem);
        int countDirectory = catalogForm.getDirectoryCount();
        for (int i = 1; i <= countDirectory; i++) {
            Logger.info(this.getClass(), "Directory: " + catalogForm.getDirectoryName(i));
            getPricesFromSubcategories(numberOfMenuItem, i);
        }
    }

    public void getPricesFromSubcategories(int numberOfMenuItem, int numberOfDirectory){
        CatalogForm catalogForm = new CatalogForm();
        ProductsPage page = new ProductsPage();
        catalogForm.open();
        boolean food = JsonUtil.getDataList("foodTrue").contains(catalogForm.getMenuItemName(numberOfMenuItem));
        DriverUtil.refresh();
        catalogForm.open();
        catalogForm.openMenuItem(numberOfMenuItem);
        catalogForm.openDirectory(numberOfDirectory);
        int countSubdirectory = catalogForm.getSubdirectoryCount();
        String directoryNow, subdirectoryNow;
        for (int i = 1; i <= countSubdirectory; i++) {
            DriverUtil.refresh();
            catalogForm.open();
            catalogForm.openMenuItem(numberOfMenuItem);
            catalogForm.openDirectory(numberOfDirectory);
            directoryNow = catalogForm.getDirectoryName(numberOfDirectory);
            subdirectoryNow = catalogForm.getSubdirectoryName(countSubdirectory);
            catalogForm.clickSubdirectory(i);
            Logger.info(this.getClass(), "Directory: " + directoryNow + " Subdirectory: " + subdirectoryNow);
            DriverUtil.scrollUpPage();
            addProductsToQueue(page.getAllProducts(directoryNow, food));
        }
    }

    public void addProductsToQueue(List<Product> list){
        queue.addAll(list);
    }

    public void interrupt(){
        System.out.println(queue.size());
        super.interrupt();
        this.stop();
    }
}
