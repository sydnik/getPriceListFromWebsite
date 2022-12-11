package org.sydnik.by.sites.e_dostavka.site.forms;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;
import org.sydnik.by.framework.utils.DriverUtil;

public class SubdirectoryForm extends BaseForm {
    private String itemsXpath = "//ul[@class='catalog_submenu']/li[contains(@class,'catalog_menu-item')]";
    public SubdirectoryForm() {
        super(new Label(By.className("catalog_submenu"), "submenu label" ), "Subdirectory Form");
    }

    public int getSubdirectoryCount(){
        return DriverUtil.getElementsCount(By.xpath(itemsXpath));
    }

    public void openSubdirectory(int position){
        new Button(By.xpath(itemsXpath + "[" + position + "]"), position + "- subdirectory button").click();
    }

}
