package org.sydnik.by.sites.tabletka.pages;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;
import org.sydnik.by.framework.elements.TextField;
import org.sydnik.by.framework.utils.DriverUtil;

import java.util.ArrayList;
import java.util.List;

public class PharmaciesPage extends BaseForm {
    private final TextField regionField = new TextField(By.xpath("//input[contains(@class,'region-select-input-pharm')]"),"Region text field");
    private final  By table = By.xpath("//*[contains(@class,'inner-page')]//div[@class='tabs-body']//tbody//td[@class='pharm-name']");
    private final Button nextBtn = new Button(By.xpath("//div[contains(@class,'table-pagination-next')]"), "next button");


    public PharmaciesPage() {
        super(new Label(By.xpath("//*[@value='pharmacy-all']"),"UniqueElementOfBrowserWindowPage"), "PharmaciesPage");
    }

    public List<String> getAllPharmacies(){
        List<String> list = new ArrayList<>();
        while (nextBtn.isVisibilityNow()){
            list.addAll(getPharmaciesFromPage());
            nextBtn.click();
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        return list;
    }

    public List<String> getPharmaciesFromPage(){
        List<String> list = new ArrayList<>();
        int size = DriverUtil.getElementsCount(table);
        for (int i = 1; i < size+1; i++) {
            list.add(new Label(By.xpath("//*[contains(@class,'inner-page')]//div[@class='tabs-body']//tbody//tr[" + i
                    + "]//td[@class='pharm-name']//a"),"Rows: " + i).getText());
        }
        return list;
    }
}
