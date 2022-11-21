package org.sydnik.by.framework.elements;

import org.openqa.selenium.By;
import org.sydnik.by.framework.utils.Logger;

public class TextField extends BaseElement{
    public TextField(By locator, String name) {
        super(locator, name);
    }

    public void sendKeys(String s){
        try {
            findElement().sendKeys(s);
            Logger.info(this.getClass(),name + " write text: "+s);
        }catch (Exception e){
            Logger.error(this.getClass(),name + " Didn't sendKeys" + "\n" + e.getMessage());
            throw e;
        }

    }
}
