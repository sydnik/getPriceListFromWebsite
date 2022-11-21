package org.sydnik.by.sites.e_dostavka.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.sites.e_dostavka.data.Product;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;
import org.sydnik.by.framework.utils.DriverUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends BaseForm {
    private Label productGroupLbl = new Label(By.xpath("//li[@class='catalog_menu-item selected']/a"), "productGroup label");
    private Button filterSubmitBtn = new Button(By.id("filter_submit"), "filter submit Button");
    private Button nextPageBtn = new Button(By.className("next_page_link"), "next page button");
    private Button showMoreProductBtn = new Button(By.id("//a[contains(@class,'show_more')]"), "show more product button");

    private String mainXpath = "//div[contains(@class,'products_card')  and not(contains(@class,'products_banner_item'))][";
    private String idXpath = "]//input[@name='product_id']";
    private String priceXpath = "]//div[@class='price']";
    private String nameXpath = "]//img";
    private String countryXpath = "]//div[@class='small_country']";




    public ProductsPage() {
        super(new Label(By.xpath("//div[contains(@class,'products_block__wrapper')]"), "Block with products label"),
                "Page with products");
    }

    public Product getProduct(int position, String directory){
        Logger.info(this.getClass(), "Start to find product №" + position);
        int id = Integer.parseInt(new Label(By.xpath(mainXpath + position + idXpath),"product id label").getAttribute("value"));
        String priceText = new Label(By.xpath(mainXpath + position + priceXpath), "Product price label").getText();
        double price = Double.parseDouble(priceText.substring(0,priceText.indexOf("к.")).replace("р.", "."));
        String name = new Label(By.xpath(mainXpath + position + nameXpath), "product name label").getAttribute("alt");
        String country;
        try {
            country = new Label(By.xpath(mainXpath + position + countryXpath), "product country label").getText();
        } catch (TimeoutException e){
            country = "Неизвестно";
        }
        String productGroup = productGroupLbl.getText();

        return new Product(id, name, price, country, productGroup, directory);
    }

    public List<Product> getAllProducts(String directory){
        List<Product> list = new ArrayList<>();
        int countProduct;
        try {
            countProduct = Integer.parseInt(filterSubmitBtn.getText().replaceAll("[^0-9]", ""));
        } catch (TimeoutException e){
            return list;
        }
        int i = 1;
        int fail = 0;
        DriverUtil.scrollDownPage();
        while (true) {
            try {
                list.add(getProduct(i, directory));
                i++;
                fail=0;
            } catch (Exception e){
                fail++;
                if(nextPageBtn.exist()){
                    nextPageBtn.click();
                    countProduct = countProduct - i;
                    i = 1;
                }else if (new Button(By.xpath(mainXpath+ (i-5) + "]"), "number product "+ (i-5) + " button").exist()) {
                    DriverUtil.scrollDownPage();
                }
                else
                {
                    Logger.error(this.getClass(),"fail:" + fail +" pos:" + i);
                    continue;
                }
            }
            if(countProduct == i || fail > JsonUtil.getDataInt("maxNumberOfError")){
                break;
            }
        }
        return list;
    }
}
