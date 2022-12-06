package org.sydnik.by.sites.e_dostavka.forms;

import org.openqa.selenium.By;
import org.sydnik.by.framework.BaseForm;
import org.sydnik.by.framework.elements.Button;
import org.sydnik.by.framework.utils.DriverUtil;

public class CatalogForm extends BaseForm {
    private Button catalogOpenedBtn = new Button(By.xpath("//*[contains(@class,'navigation_wrapper_opened')]"),"Catalog Button");
    private Button catalogButton = new Button(By.xpath("//button[contains(@class,'catalog_button')]"),"Catalog Button");

    private By directoryCount = By.xpath("//*[contains(@class,'navigation_categories__item_children')]");
    private By menuItemCount = By.xpath("//*[@id='navigation_left']/li[@role='menuitem']");
    private By subdirectory = By.xpath("//*[contains(@class,'desktop_subcategory__title')]");

    private String openedMenuItem = "navigation_categories__item_expanded";
    private String numberMenuItem = "//*[@role='menuitem'][NUMBER]";
    private String nameMenuItem = "//*[@role='menuitem']//*[text()='NAME']/..";
    private String numberDirectory = "//*[contains(@class,'navigation_categories__item_children')][NUM]";
    private String nameDirectory = "//*[contains(@class,'navigation_categories_children')]//*[text()='NAME']";
    private String numberSubdirectory = "//*[contains(@class,'desktop_subcategory')][NUM]/a[contains(@class,'title')]";
    private String nameSubdirectory = "//*[contains(@class,'desktop_subcategory__title')][text()='NAME']";

    public CatalogForm() {
        super(new Button(By.xpath("//*[contains(@class,'navigation_wrapper')]"),"Catalog Button"), "Catalog Form");
    }

    public void open(){
        if (!catalogOpenedBtn.exist()){
            try {
                catalogButton.click();
            } catch (Exception e){
                DriverUtil.refresh();
                catalogButton.click();
            }

        }
    }

    public void openMenuItem(int num){
        Button itemBtn = new Button(By.xpath(numberMenuItem.replace("NUMBER", String.valueOf(num))), num + " item button");
        if(!itemBtn.getClasses().contains(openedMenuItem)){
            itemBtn.click();
        }
    }

    public void openMenuItem(String name){
        Button itemBtn = new Button(By.xpath(nameMenuItem.replace("NAME", name)), name + " item Button");
        if(!itemBtn.getClasses().contains(openedMenuItem)){
            itemBtn.click();
        }
    }

    public String openDirectory(int num){
        Button itemBtn = new Button(By.xpath(numberDirectory.replace("NUM", String.valueOf(num))), num + " directory Button");
        itemBtn.moveToElement();
        return itemBtn.getText();
    }

    public void openDirectory(String name){
        Button itemBtn = new Button(By.xpath(nameDirectory.replace("NAME", name)), name + " directory Button");
        itemBtn.moveToElement();
    }

    public void clickSubdirectory(int num){
        Button button = new Button(By.xpath(numberSubdirectory.replace("NUM", String.valueOf(num))), num + " subdirectory btn");
        button.click();
    }

    public void clickSubdirectory(String name){
        Button button = new Button(By.xpath(nameSubdirectory.replace("NAME", name)), name + " subdirectory btn");
        button.click();
    }

    public int getDirectoryCount(){
        return DriverUtil.getElementsCount(directoryCount);
    }

    public int getMenuItemCount(){
        return DriverUtil.getElementsCount(menuItemCount);
    }

    public int getSubdirectoryCount(){
        return DriverUtil.getElementsCount(subdirectory);
    }
}
