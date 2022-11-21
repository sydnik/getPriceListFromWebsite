package org.sydnik.by.sites.e_dostavka.pages;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;
import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.JsonUtil;

import java.util.List;

public class CatalogPage extends BaseForm {
    private String groupsXpath = "//div[contains(@class,'gg_clone') and not (@style)]";
    private String groupsXpathForClick = "//div[contains(@class,'gg_clone') and @style][POSITION]/div/a";

    public CatalogPage() {
        super(new Label(By.xpath("//div[contains(@class,'rubrics_table')]"), "element catalog"), "product catalog page");
    }

    public int getDirectoryCount(){
        return DriverUtil.getElementsCount(By.xpath(groupsXpath));
    }

    public boolean openDirectory(int position){
        Button button = new Button(By.xpath(groupsXpathForClick.replace("POSITION", String.valueOf(position))), position + "-directory button");
        List<String> list = JsonUtil.getDataList("groupForMiss");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(button.getText())){
                return false;
            }
        }
        button.click();
        return true;
    }

    public String directoryName(int position){
        return new Button(By.xpath(groupsXpathForClick.replace("POSITION", String.valueOf(position))), "wqe").getText();
    }


}
