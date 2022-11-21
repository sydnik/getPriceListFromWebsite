package org.sydnik.by.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class DriverUtil {
    private WebDriver webDriver;
    private static DriverUtil driverUtils;
    private static Set<String> tabAndWindow;

    private DriverUtil(){
    }

    public static WebDriver getWebDriver() {
        return getInstance().webDriver;
    }

    public static DriverUtil getInstance(String browser){
        if(driverUtils!=null) {
            if(driverUtils.webDriver!=null) {
                return driverUtils;
            }
        }
        driverUtils = new DriverUtil();
        driverUtils.webDriver = BrowserFactory.getBrowser(browser);
        return driverUtils;
    }

    public static DriverUtil getInstance(){
        if(driverUtils!=null) {
            if(driverUtils.webDriver!=null) {
                return driverUtils;
            }
        }
        driverUtils = new DriverUtil();
        driverUtils.webDriver = BrowserFactory.getBrowser(JsonUtil.getConfString("browser"));
        return driverUtils;
    }

    public static int getElementsCount(By by){
        return getInstance().webDriver.findElements(by).size();
    }

    public static String getCurrentUrl(){
        return getInstance().webDriver.getCurrentUrl();
    }

    public static int getNumberOfWindow(){
        return getInstance().webDriver.getWindowHandles().size();
    }

    public static void openURL(String url){
        getInstance().webDriver.get(url);
    }

    public static String getCurrentWindow(){
        return getInstance().webDriver.getWindowHandle();
    }

    public static void switchToFrame(By by){
        getInstance().webDriver.switchTo().frame(WaitElement.waitPresence(by));
    }

    public static void switchToWindow(String window){
        getInstance().webDriver.switchTo().window(window);
    }

    public static void saveCurrentWindows(){
        tabAndWindow = getInstance().webDriver.getWindowHandles();
    }

    public static void openNewWindow(){
        Set<String> tabs =  getInstance().webDriver.getWindowHandles();
        for (String s : tabs) {
            if(!tabAndWindow.contains(s)){
                getInstance().webDriver.switchTo().window(s);
            }
        }
    }

    public static void waitAndOpenNewWindow(){
        WaitElement.waitNewWindow(tabAndWindow.size()+1);
        Set<String> tabs =  getInstance().webDriver.getWindowHandles();
        for (String s : tabs) {
            if(!tabAndWindow.contains(s)){
                getInstance().webDriver.switchTo().window(s);
            }
        }
    }

    public static void openAvailableWindow(){
        for (String s : getInstance().webDriver.getWindowHandles()){
            getInstance().webDriver.switchTo().window(s);
            break;
        }
    }

    public static void closeTab(){
        getInstance().webDriver.close();
    }

    public static void close(){
        getInstance().webDriver.quit();
        driverUtils=null;
    }

    public static void scrollDownPage(){
        JavascriptExecutor js = ((JavascriptExecutor) DriverUtil.getWebDriver());
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollUpPage(){
        JavascriptExecutor js = ((JavascriptExecutor) DriverUtil.getWebDriver());
        js.executeScript("window.scrollTo(0, 0)");
    }
}
