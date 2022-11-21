package org.sydnik.by.sites.e_dostavka.forms;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;

public class HeadForm extends BaseForm {
    private  Button catalogBtn = new Button(By.xpath("//div[@class='main_menu']//a[contains(@href,'catalog')]"), "catalog button");


    public HeadForm() {
        super(new Label(By.className("main_menu"), "main menu label"), "head form");
    }

    public void openCatalog(){
        catalogBtn.click();
    }

    public void openDirectory(String name){
        new Button(By.xpath("//span[text()='" + name + "']"), "directory button. Name: " + name).click();
    }
}
