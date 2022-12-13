package org.sydnik.by.sites.e_dostavka.site.forms;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;

public class HeadForm extends BaseForm {
    private final Button catalogBtn = new Button(By.xpath("//div[@class='main_menu']//a[contains(@href,'catalog')]"), "catalog button");
    private final Button mainPageBtn = new Button(By.xpath("//a[contains(@class,'logo_logo')]"), "main page button");

    public HeadForm() {
        super(new Label(By.className("main_menu"), "main menu label"), "head form");
    }

    public void openCatalog(){
        catalogBtn.click();
    }

    public void openDirectory(String name){
        new Button(By.xpath("//span[text()='" + name + "']"), "directory button. Name: " + name).click();
    }

    public void openMainPage(){
        mainPageBtn.click();
    }
}
