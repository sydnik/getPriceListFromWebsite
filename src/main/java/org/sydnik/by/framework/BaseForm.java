package org.sydnik.by.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.sydnik.by.framework.elements.BaseElement;
import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.Logger;

public abstract class BaseForm {
    private String name;
    private BaseElement uniqueElement;
    private String savedWindow;

    public BaseForm(BaseElement uniqueElement, String name) {
        this.uniqueElement = uniqueElement;
        this.name = name;
    }

    public void switchWindow(By locator){
        try {
            DriverUtil.switchToFrame(locator);
            Logger.info(this.getClass(),"switch frame to this " + locator);
        }catch (NoSuchFrameException e){
            Logger.error(this.getClass(),"Didn't switch frame to this " + locator + "\n" + e.getMessage());
            throw e;
        }

    }

    public void saveWindowKey(){
        savedWindow = DriverUtil.getCurrentWindow();
        Logger.info(this.getClass(),"save window key");
    }

    public void saveCurrentWindow(){
        try {
            DriverUtil.switchToWindow(savedWindow);
            Logger.info(this.getClass(),"save window key");
        }
        catch (NoSuchWindowException e){
            Logger.error(this.getClass(),"Didn't switch to window " + "\n" + e.getMessage());
            throw e;
        }

    }

    public boolean isPageOpened(){
        try {
            uniqueElement.exist();
            Logger.info(this.getClass(),name + " is open");
        }catch (Exception e){
            Logger.error(this.getClass(),name + " isn't open" + "\n" + e.getMessage());
            throw e;
        }
        return true;
    }

    public boolean isPageClosed(){
        try {
            uniqueElement.isInVisibility();
            Logger.info(this.getClass(),name + " is closed");
        }catch (Exception e){
            Logger.error(this.getClass(),name + " isn't closed" + "\n" + e.getMessage());
            throw e;
        }
        return true;
    }
}
