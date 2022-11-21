package org.sydnik.by.sites.tabletka.pages;

import org.openqa.selenium.By;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.sites.tabletka.data.Drug;
import org.sydnik.by.sites.tabletka.data.Pharmacy;
import org.sydnik.by.sites.tabletka.enums.ItemAmount;
import org.sydnik.by.sites.tabletka.enums.SortType;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.elements.Label;
import org.sydnik.by.framework.utils.DownloadUtil;

import java.util.ArrayList;
import java.util.List;

public class DrugPage extends BaseForm {
    private final Button nowItemAmount = new Button(By.id("paging"),"item Amount btn");
    private final Button sortingBtn = new Button(By.xpath("//div[contains(@class,'select-check--sorting')]"),"Sorting Button");
    private final Label drugNum = new Label(By.id("ls_num"), "Drug id label");
    private final Label drugName = new Label(By.id("ls_name"), "Drug name label");
    private final Label countryAndProducer = new Label(By.xpath("//p[@class='caption']//*[@class='recept-icon']//..//.."), "countryAndProducer label");
    private final Button nextBtn = new Button(By.xpath("//div[contains(@class,'table-pagination-next')]"), "next button");

    public DrugPage() {
        super(new Label(By.id("ls_num"), "Drug Number Label"), new Label(By.id("ls_num"),"wew").getAttribute("value") + "Page");
    }

    public void showShops(ItemAmount amount){
        if (!nowItemAmount.getText().contains(String.valueOf(amount))){
            nowItemAmount.click();
            Button itemAmount = new Button(By.xpath("//*[@id='paging']//input[@value='"+ amount.getAmount() + "']/.."),amount.getAmount() + "item Amount btn");
            itemAmount.click();
        }
    }

    public void selectSorting(SortType type){
        if(sortingBtn.getText().equals(type.getName())){
            sortingBtn.click();
            if(new Label(By.xpath("//*[contains(@class,'select-check--sorting-" + type.getUpOrDown() + "')]"),"Sort Down Label" ).unExist()){
                new Button(By.xpath("//input[@value='" + type.getUpOrDown() + "']"), type.getUpOrDown() + " Button").click();
            }
            else {
                sortingBtn.click();
            }
        }
        else {
            sortingBtn.click();
            if(new Label(By.xpath("//*[contains(@class,'select-check--sorting-" + type.getUpOrDown() + "')]"),"Sort Down Label" ).unExist()){
                new Button(By.xpath("//input[@value='" + type.getUpOrDown() + "']"), type.getUpOrDown() + " Button").click();
                sortingBtn.click();
            }
            new Button(By.xpath("//input[@sort='" + type.getSort() + "']//.."), type.getSort() + " button").click();
        }
    }

    public Drug getDrugWithPrice(){
        List<String> pharmacyNames = DownloadUtil.getList(JsonUtil.getDataString("pathPharmaciesFile"));
        List<Pharmacy> pharmacies = new ArrayList<>();


        while (true) {
            if (pharmacyNames.isEmpty()) {
                break;
            }
            int size = pharmacyNames.size();
            for (int i = size - 1; i >= 0; i--) {
                try {
                    pharmacies.add(getPharmacyAndPrice(pharmacyNames.get(i)));
                    pharmacyNames.remove(i);
                } catch (Exception e) {

                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(nextBtn.isVisibility()){
                nextBtn.click();
            }else break;

        }
        Drug drug = getDrug();
        drug.setPharmacyAndPrice(pharmacies);
        for (int i = 0;i<drug.getPharmacies().size();i++){
            System.out.println(drug.getPharmacies().get(i).getName() + " - " + drug.getPharmacies().get(i).getPrice());
        }

        return drug;

    }

    public Drug getDrug(){
        int id = Integer.parseInt(drugNum.getAttribute("value"));
        String name = drugName.getAttribute("value");
        String producer;
        String county;
        String temp = countryAndProducer.getText();
        temp = temp.substring(temp.indexOf("/ ")+1,temp.lastIndexOf("/"));
        county = temp.substring(temp.lastIndexOf(",")+1).trim();
        producer = temp.substring(0, temp.lastIndexOf(",")).trim();
        return new Drug(id, name, producer, county);
    }

    public Pharmacy getPharmacyAndPrice(String name){
        Label pharmacyLabel = new Label(By.xpath("//a[contains(@usr_name,'NAME')]".replace("NAME",name)), name + " pharmacy label");
        double price = Double.parseDouble(pharmacyLabel.getAttribute("price"));
        return new Pharmacy(name, price);
    }
}
