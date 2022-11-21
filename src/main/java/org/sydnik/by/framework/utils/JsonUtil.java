package org.sydnik.by.framework.utils;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;

import java.util.List;

public class JsonUtil {
    private static ISettingsFile data = new JsonSettingsFile("data.json");
    private static ISettingsFile configuration = new JsonSettingsFile("browserConfiguration.json");

    public static String getDataString(String key){
        return data.getValue("/" + key).toString();
    }

    public static int getDataInt(String key){
        return (int) data.getValue("/" + key);
    }

    public static List<String> getDataList(String key){
        return data.getList("/" + key);
    }

    public static String getConfString(String key){
        return configuration.getValue("/" + key).toString();
    }

    public static int getConfInt(String key){
        return (int) configuration.getValue("/" + key);
    }
}
