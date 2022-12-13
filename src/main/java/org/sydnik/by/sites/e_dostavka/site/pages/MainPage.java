package org.sydnik.by.sites.e_dostavka.site.pages;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;

public class MainPage extends BaseForm {
    private final Button closeAddressBtn = new Button(By.className("react-responsive-modal-closeButton"), "close address button");
    private final Button closeCookiesBtn = new Button(By.xpath("//*[contains(@class,'accept-cookies_close')]"), "close cookies button");

    public MainPage() {
        super(new Label(By.id("header"), "header label"), "main page");
    }

    public void closeCookies(){
        closeCookiesBtn.click();
    }

    public void closeAddress(){
        closeAddressBtn.click();
    }
}
