package org.sydnik.by.sites.tabletka.pages;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;

public class DrugsListPage extends BaseForm {
    private String drugXpath = "//*[@class='search-result']/li[NUMBER]";
    private String drugNameXpath = "//*[@class='search-result']//a[text()='NAME']";


    public DrugsListPage() {
        super(new Label(By.xpath("//*[@id='page_name' and @value='drugs']"), "Drugs label"), "Drags Page");
    }

    public String getDrugsName(int number){
        Label drugLbl = new Label(By.xpath(drugXpath.replace("NUMBER", String.valueOf(number))), "Drug name label");
        return drugLbl.getText();
    }

    public void clickDrug(int number){
        new Button(By.xpath(drugXpath.replace("NUMBER", String.valueOf(number))), "Drug Button").click();
    }

    public void clickDrug(String name){
        new Button(By.xpath(drugXpath.replace("NAME", name)), "Drug Button").click();
    }
}
