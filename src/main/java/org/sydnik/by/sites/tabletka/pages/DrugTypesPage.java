package org.sydnik.by.sites.tabletka.pages;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.BaseElement;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;

public class DrugTypesPage extends BaseForm {
    String drugXpath = "//*[@class='tbody-base-tbl']//tr[NUMBER]//a";

    public DrugTypesPage() {
        super(new Label(By.id("base-select"), "table Label"), "DrugTypesPage");
    }

    public void clickDrug(int number){
        new Button(By.xpath(drugXpath.replace("NUMBER", String.valueOf(number))), "Drug Button").click();
    }
}
