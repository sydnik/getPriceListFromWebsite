package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.sites.e_dostavka.forms.HeadForm;
import org.sydnik.by.sites.e_dostavka.forms.SubdirectoryForm;
import org.sydnik.by.sites.e_dostavka.pages.CatalogPage;
import org.sydnik.by.sites.e_dostavka.pages.ProductsPage;

import java.util.ArrayList;
import java.util.List;

public class EDostavka {

    public static List<Product> getAllPrices(){
        List<Product> list = new ArrayList<>();
        HeadForm headForm = new HeadForm();
        CatalogPage catalogPage = new CatalogPage();
        ProductsPage productsPage = new ProductsPage();
        SubdirectoryForm subdirectoryForm = new SubdirectoryForm();
        String directoryNow;
        DriverUtil.openURL("https://e-dostavka.by");
        for (int i = 9; i <= 9; i++) {
            DriverUtil.scrollUpPage();
            System.out.println(i);
            headForm.openCatalog();
            directoryNow = catalogPage.directoryName(i);
            if (!catalogPage.openDirectory(i)) continue;
            int count = subdirectoryForm.getSubdirectoryCount();
            for (int j = 1; j <= count; j++) {
                headForm.openDirectory(directoryNow);
                subdirectoryForm.openSubdirectory(j);
                list.addAll(productsPage.getAllProducts(directoryNow));
            }
        }
        return list;
    }
}
