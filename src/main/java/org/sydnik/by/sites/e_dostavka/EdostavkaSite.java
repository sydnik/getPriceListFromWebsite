package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.ConsoleBeta;
import org.sydnik.by.IStopThread;
import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sites.e_dostavka.site.forms.CatalogForm;
import org.sydnik.by.sites.e_dostavka.site.pages.MainPage;
import org.sydnik.by.sites.e_dostavka.site.pages.ProductsPage;

import java.util.List;

public class EdostavkaSite extends Thread implements IStopThread {
    private int menuItem;
    private int directory;
    private int subDirectory;

    public EdostavkaSite() {
        menuItem = 1;
        directory = 1;
        subDirectory = 1;
    }

    public EdostavkaSite(int menuItem, int directory, int subDirectory) {
        this.menuItem = menuItem;
        this.directory = directory;
        this.subDirectory = subDirectory;
    }

    public void run() {
        long time = System.currentTimeMillis();
        try {
            DriverUtil.getInstance();
            MainPage mainPage = new MainPage();
            DriverUtil.openURL("https://edostavka.by/");
            mainPage.closeAddress();
            mainPage.closeCookies();
            getPricesFromMenuItems();
        } catch (Exception e) {
            Logger.error(this.getClass(), "Server fell off\n" +
                    e.getMessage() + e.getLocalizedMessage());
            Logger.info(this.getClass(), "menuItem: " + menuItem + ", directory: " + directory + ", subdirectory: " + subDirectory);
            ConsoleBeta.getInstance().stopThead();
        } finally {
            Logger.info(this.getClass(), "App ran for " + (System.currentTimeMillis() - time) + " ms");
            Logger.info(this.getClass(), DriverUtil.getCurrentUrl());
            DriverUtil.close();
        }
    }

    public void getPricesFromMenuItems() {
        CatalogForm catalogForm = new CatalogForm();
        catalogForm.open();
        int countItem = catalogForm.getMenuItemCount();
        for (; menuItem <= countItem; menuItem++) {
            DriverUtil.refresh();
            catalogForm.open();
            catalogForm.closeMenuItem();
            Logger.info(this.getClass(), "Item: " + catalogForm.getMenuItemName(menuItem));
            getPricesFromDirectories(menuItem);
        }
    }

    public void getPricesFromDirectories(int numberOfMenuItem) {
        CatalogForm catalogForm = new CatalogForm();
        catalogForm.open();
        catalogForm.openMenuItem(numberOfMenuItem);
        int countDirectory = catalogForm.getDirectoryCount();
        for (; directory <= countDirectory; directory++) {
            Logger.info(this.getClass(), "Directory: " + catalogForm.getDirectoryName(directory));
            getPricesFromSubcategories(numberOfMenuItem, directory);
        }
        directory = 1;
    }

    public void getPricesFromSubcategories(int numberOfMenuItem, int numberOfDirectory) {
        String directoryNow = null, subdirectoryNow = null;
        CatalogForm catalogForm = new CatalogForm();
        ProductsPage page = new ProductsPage();
        catalogForm.open();
        boolean food = JsonUtil.getDataList("foodTrue").contains(catalogForm.getMenuItemName(numberOfMenuItem));
        DriverUtil.refresh();
        catalogForm.open();
        catalogForm.openMenuItem(numberOfMenuItem);
        catalogForm.openDirectory(numberOfDirectory);
        int countSubdirectory = catalogForm.getSubdirectoryCount();
        for (; subDirectory <= countSubdirectory; subDirectory++) {
            DriverUtil.refresh();
            catalogForm.open();
            catalogForm.closeMenuItem();
            catalogForm.openMenuItem(numberOfMenuItem);
            catalogForm.openDirectory(numberOfDirectory);
            directoryNow = catalogForm.getDirectoryName(numberOfDirectory);
            if (skipDirectories(directoryNow)) {
                continue;
            }
            subdirectoryNow = catalogForm.getSubdirectoryName(countSubdirectory);
            catalogForm.clickSubdirectory(subDirectory);
            Logger.info(this.getClass(), "Directory: " + directoryNow + " Subdirectory: " + subdirectoryNow);
            DriverUtil.scrollUpPage();
            addProductsToQueue(page.getAllProducts(directoryNow, food));
        }
        subDirectory = 1;
    }

    public void addProductsToQueue(List<Product> list) {
        QueueUtil.addList(list);
    }

    @Override
    public void stopThead() {
        System.out.println(QueueUtil.size());
        super.interrupt();
        this.stop();
    }

    public String getPointWhereNow(){
        return "ItemMenu:" + menuItem + " Directory:" + directory + " Subdirectory:" + subDirectory;
    }

    public boolean skipDirectories(String directory){
        return JsonUtil.getDataList("DirectoriesForMiss").contains(directory);
    }
}
