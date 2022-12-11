package org.sydnik.by.framework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sydnik.by.framework.utils.DriverUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.framework.utils.WaitElement;

public abstract class BaseElement {
    protected  String name;
    protected By locator;

    public BaseElement(By locator, String name) {
        this.name = name;
        this.locator = locator;
    }

    public By getLocator() {
        return locator;
    }

    public boolean isInVisibility(){
        try {
            boolean result = WaitElement.waitInVisibility(locator);
            Logger.info(this.getClass(),name + " is invisible");
            return result;
        }catch (Exception e){
            Logger.error(this.getClass(),name + " is visible\n" + e.getMessage());
            throw e;
        }
    }

    public boolean isVisibilityNow(){
        Logger.info(this.getClass()," - visibility now " + name);
        return findElement().isDisplayed();
    }

    public boolean isVisibility(){
        Logger.info(this.getClass()," - visibility " + name);
        try {
            WaitElement.waitVisibility(findElement());
            return true;
        } catch (Exception e){
            Logger.info(this.getClass()," - visibility " + name + e.getMessage());
            return false;
        }
    }

    public boolean exist(){
        try {
            findElement();
            Logger.info(this.getClass(),"check exist "+ name );
            return true;
        }catch (Exception e){
            Logger.error(this.getClass(),  "there is no element "+ name + "\n" + e.getMessage());
            return false;
        }
    }

    public boolean unExist(){
        try {
            Logger.error(this.getClass(),"check un exist "+ name);
            return WaitElement.waitUnPresence(locator);
        }catch (Exception e){
            Logger.error(this.getClass(),  "there is "+ name + "\n" + e.getMessage());
            return false;
        }
    }

    public String getAttribute(String attribute){
        try {
            String result = findElement().getAttribute(attribute);
            Logger.info(this.getClass(),name  + "found the" + attribute);
            return result;
        }catch (Exception e){
            Logger.error(this.getClass(), name+ " didn't find the " + attribute);
            throw  e;
        }
    }

    public String getText(){
        try {
            String result = findElement().getText();
            Logger.info(this.getClass(),  "got text of the" + name );
            return result;
        }catch (Exception e) {
            Logger.error(this.getClass(),"Didn't get text of the"+ name + "\n" + e.getMessage());
            throw e;
        }
    }

    public int getWidth(){
        Logger.info(this.getClass(),"get width element");
        return findElement().getSize().width;
    }

    public int getHeight(){
        Logger.info(this.getClass(),"get height element");
        return findElement().getSize().height;
    }

    public void click(){
        try {
            WaitElement.waitClickable(findElement()).click();
            Logger.info(this.getClass(),"Clicked"+name);
        }
        catch (ElementClickInterceptedException e) {
            scrollAndClickElement();
        }
        catch (Exception e){
            Logger.error(this.getClass(),"Didn't click " + name +"\n" + e.getMessage());
            throw e;
        }
    }

    public void scrollToElement(){
        try {
            JavascriptExecutor js = ((JavascriptExecutor) DriverUtil.getWebDriver());
            js.executeScript("arguments[0].scrollIntoView({block: \"center\"});", findElement());
            Logger.info(this.getClass(),name + " used js scroll");
        }catch (Exception e){
            Logger.error(this.getClass(),name+" can't use scroll");
        }

    }
    protected WebElement findElement(){
        try {
            WebElement element = WaitElement.waitPresence(locator);
            Logger.info(( this.getClass()),name + " found");
            return element;
        } catch (Exception e){
            Logger.error(this.getClass(),"Didn't find "+ name +"\n" + e.getMessage());
            throw e;
        }
    }
    protected void scrollAndClickElement(){
        try {
            scrollToElement();
            WaitElement.waitClickable(findElement()).click();
            Logger.info(this.getClass(),name + "Used scrollAndClickElement successfully");
        } catch (Exception e){
            Logger.error(this.getClass(),"Didn't click " + name + " after scroll "+"\n" + e.getMessage());
            throw e;
        }
    }

    public void moveToElement(){
        new Actions(DriverUtil.getWebDriver()).moveToElement(findElement()).perform();
        Logger.info(this.getClass(),name + "move cursor to element");
    }

    public String getClasses(){
        String result = findElement().getAttribute("class");
        Logger.info(this.getClass(),result + " got element classes");
        return result;
    }
}
