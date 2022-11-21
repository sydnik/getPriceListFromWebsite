package org.sydnik.by.framework.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class WaitElement {
    private WebDriverWait wait;
    private static WaitElement waitElement;

    private WaitElement() {
        this.wait = new WebDriverWait(DriverUtil.getWebDriver(),
                Duration.ofSeconds(JsonUtil.getConfInt("waitSeconds")),
                Duration.ofMillis(JsonUtil.getConfInt("timeoutMillis")));
    }

    public static WaitElement getInstance() {
        if (waitElement != null) {
            return waitElement;
        }
        return new WaitElement();

    }

    public static WebElement waitVisibility(WebElement element) {
        return getInstance().wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static boolean waitInVisibility(By locator) {
        return getInstance().wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitClickable(WebElement element) {
        return getInstance().wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitPresence(By locator) {
        return getInstance().wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> watElements(By locator) {
        return getInstance().wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public static Alert waitPresentAlert() {
        return getInstance().wait.until(ExpectedConditions.alertIsPresent());
    }

    public static Boolean waitUnPresentAlert() {
        return getInstance().wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
    }

    public static Boolean waitUnPresence(By locator) {
        return getInstance().wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(locator)));
    }

    public static Boolean waitNewWindow(int numberOfWindow) {
        return getInstance().wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindow));
    }

    public static Boolean waitToBeAttributeValue(WebElement element, String attribute, String value) {
        return getInstance().wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public static Boolean waitToBeText(WebElement element, String value, int timeOut) {
        WebDriverWait waitCustom = new WebDriverWait(DriverUtil.getWebDriver(),
                Duration.ofSeconds(JsonUtil.getConfInt("waitSeconds")),
                Duration.ofMillis(timeOut));
        return waitCustom.until(ExpectedConditions.textToBePresentInElement(element, value));
    }

    public static Boolean waitToBeTextValues(WebElement element, String[] values, int timeOut) {
        ExpectedCondition<?>[] array = new ExpectedCondition[values.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = ExpectedConditions.textToBePresentInElement(element, values[i]);
        }
        return new WebDriverWait(DriverUtil.getWebDriver(), Duration.ofSeconds(JsonUtil.getConfInt("waitSeconds")), Duration.ofMillis(timeOut)).
                until(ExpectedConditions.or(array));
    }

    public static Boolean waitFileDownLoad(File file) {
        return getInstance().wait.until(a -> file.exists());
    }

    public static void waitScript() {
    }

}
