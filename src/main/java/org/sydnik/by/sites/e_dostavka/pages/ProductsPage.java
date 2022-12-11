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
    private Label subdirectoryLbl = new Label(By.xpath("//*[contains(@class,'category_heading')]"), "productGroup label");
    private Label productsCountLbl = new Label(By.xpath("//*[contains(@class,'category_product__count')]"), "products count label");
    private Label selectedCountryLbl = new Label(By.xpath("//*[contains(@class,'filter_country')]//*[contains(@class,'checkbox__label_checked')]"), "product country label");
    private Label filterLbl = new Label(By.xpath("//*[contains(@class,'filters_filters')]"), "filter label");
    private Button nextPageBtn = new Button(By.xpath("//*[contains(@class,'pagination_next')]"), "next page button");
    private Button showMoreProductBtn = new Button(By.id("//a[contains(@class,'show_more')]"), "show more product button");
    private Button clearFilterBtn = new Button(By.xpath("//*[contains(@class,'filters_clear__text')]"), "clear filter button");
    private Button showAllCountryBtn = new Button(By.xpath("//*[contains(@class,'filter_country')]//*[contains(@class,'checkboxes_button')]"), "show all country button");
    private Button plus18ageAgreeBtn = new Button(By.xpath("//*[contains(@class,'confirm-without-close_modal')]//button[contains(@class,'button_type_primary')]"), " 18 + age agree button");

    private By countryCount = By.xpath("//*[contains(@class,'filter_country')]//*[contains(@class,'checkbox__label')]");

    private String mainXpath = "//div[contains(@class,'products_product')][NUM]/div[contains(@class,'products_product_vertical')]";
    private String idXpath = "//a";
    private String priceXpath = "//span[contains(@class,'price_main')]";
    private String nameXpath = "//a[contains(@class,'vertical_title')]";
    private String countryFilter = "//*[contains(@class,'filter_country')]//*[contains(@class,'checkbox__label')][NUM]";

    public ProductsPage() {
        super(new Label(By.xpath("//div[contains(@class,'products_block__wrapper')]"), "Block with products label"),
                "Page with products");
    }

    public Product getProduct(int position, String directory, boolean food){
        Logger.info(this.getClass(), "Start to find product №" + position + mainXpath + idXpath);
        String posString = String.valueOf(position);
        String href = new Label(By.xpath(mainXpath.replace("NUM", posString)  + idXpath),"product id label").getAttribute("href");
        int id = Integer.parseInt(href.substring(href.lastIndexOf("/product/")+9));
        String priceText = new Label(By.xpath(mainXpath.replace("NUM", posString) + priceXpath), "Product price label").getText();
        double price = Double.parseDouble(priceText.substring(0,priceText.indexOf(" ")).replace(",", "."));
        String name = new Label(By.xpath(mainXpath.replace("NUM", posString) + nameXpath), "product name label").getAttribute("title");
        String country = selectedCountryLbl.getText();
        String subdirectory = subdirectoryLbl.getText();

        return new Product(id, name, price, country, subdirectory, directory, food);
    }

    public List<Product> getAllProducts(String directory, boolean food){
        List<Product> list = new ArrayList<>();
        check18PlusAndClose();
        for (int j = 1; j <= getCountryCount(); j++) {
            selectCountry(j);
            list.addAll(getOneCountryProducts(directory, food));
            clearFilter();
        }
        return list;
    }

    public List<Product> getOneCountryProducts(String directory, boolean food){
        List<Product> list = new ArrayList<>();
        int productCount;
        int i = 1;
        int fail = 0;
        try {
            productCount = Integer.parseInt(productsCountLbl.getText().replaceAll("[^0-9]", ""));
            Logger.info(this.getClass(), productCount + " found products");
        } catch (TimeoutException e){
            Logger.error(this.getClass(), e.getMessage());
            return list;
        }
        while (true) {
            try {
                list.add(getProduct(i, directory, food));
                i++;
                fail=0;
            } catch (Exception e){
                Logger.error(this.getClass(), e.getMessage());
                fail++;
                if(nextPageBtn.isVisibility()){
                    nextPageBtn.click();
                    productCount = productCount - i;
                    i = 1;
                }else if (new Button(By.xpath(mainXpath.replace("NUM", String.valueOf(i-1))), "number product "+ (i-1) + " button").exist()) {
                    new Button(By.xpath(mainXpath.replace("NUM", String.valueOf(i-1))), "number product "+ (i-1) + " button").scrollToElement();
                }
                else
                {
                    Logger.error(this.getClass(),"fail:" + fail +" pos:" + i);
                    if(productCount <= i || fail > JsonUtil.getDataInt("maxNumberOfError")){
                        try {
                            Logger.error(this.getClass(), "countryFilter: " + countryFilter + " countryCount:" + countryCount +
                                    " countProduct:" + productCount + " i:" + i + " list.size:" + list.size() +
                                    " last product:" + list.get(list.size() - 1).toString());
                        }catch (Exception a){
                            Logger.error(this.getClass(), e.getMessage());
                            Logger.error(this.getClass(), "Didn't write error log im method getOneCountryProducts");
                        }
                        break;
                    }
                    continue;
                }
            }
            if(productCount <= i || fail > JsonUtil.getDataInt("maxNumberOfError")){
                break;
            }

        }
        return list;
    }

    public void selectCountry(int position){
        showAllCountry();
        Button button = new Button(By.xpath(countryFilter.replace("NUM", String.valueOf(position))), position + " filter country button");
        button.click();
        filterLbl.isVisibility();
    }

    public void clearFilter(){
        if (clearFilterBtn.isVisibility()){
            clearFilterBtn.click();
        }
    }

    public void showAllCountry(){
        if(showAllCountryBtn.isVisibility()){
            showAllCountryBtn.click();
        }
    }

    public int getCountryCount(){
        showAllCountry();
        int count = DriverUtil.getElementsCount(countryCount);
        Logger.info(this.getClass(), count + "- counties count");
        return  DriverUtil.getElementsCount(countryCount);
    }

    public void check18PlusAndClose(){
        if(plus18ageAgreeBtn.exist()){
            plus18ageAgreeBtn.click();
        }
    }

}
