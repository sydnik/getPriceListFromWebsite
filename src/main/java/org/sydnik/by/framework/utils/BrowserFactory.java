package org.sydnik.by.framework.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;

public class BrowserFactory {
    public static WebDriver getBrowser (String browserName){
        switch (browserName) {
            case "FireFox": {
                return startFireFox();
            }
            case "Chrome": {
                return startChrome();
            } default: {
                Logger.error(BrowserFactory.class, browserName + " - invalid browser name");
                throw new IllegalArgumentException(browserName + " - invalid browser name");
            }
        }
    }

    private static WebDriver startChrome(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        HashMap<String,Object> map = new HashMap<>();
        map.put("profile.default_content_settings.popups", 0);
//        map.put("download.default_directory", FilesUtil.getAbsolutPathDirectory(ConfigUtil.getConfProperty("pathForDownload")));
        options.setExperimentalOption("prefs",map);
        options.addArguments("--window-size=" +
                JsonUtil.getConfString("windowWidth") +","+
                JsonUtil.getConfString("windowHeight"));
        options.addArguments("--" + JsonUtil.getConfString("smoothScrollChrome")+"-smooth-scrolling");
        options.addArguments("--lang=" + JsonUtil.getConfString("language"));
        options.setCapability("gsg:customcap", true);
        options.setCapability("networkname:applicationName", "node_");
        options.setCapability("nodename:applicationName", "app_2");
            return new ChromeDriver(options);
    }

    private static WebDriver startFireFox(){
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        FirefoxProfile profile = new FirefoxProfile();
//        profile.setPreference("intl.accept_languages", ConfigUtil.getConfProperty("language"));
//        profile.setPreference("general.smoothScroll",ConfigUtil.getConfProperty("smoothScrollFireFox"));
//        profile.setPreference("browser.download.folderList", 2);
//        profile.setPreference("browser.download.dir", FilesUtil.getAbsolutPathDirectory(ConfigUtil.getConfProperty("pathForDownload")));
//        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", ConfigUtil.getConfProperty("filesDownLoadFireFox"));
//        firefoxOptions.addArguments("--width=" + ConfigUtil.getConfIntProperty("windowWidth"));
//        firefoxOptions.addArguments("--height=" + ConfigUtil.getConfIntProperty("windowHeight"));
//        firefoxOptions.setProfile(profile);
        //какой то параметр не дает запустится*
        return  new FirefoxDriver(firefoxOptions);
    }
}
