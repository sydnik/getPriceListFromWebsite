package org.sydnik.by.framework.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DownloadUtil {

    public static List<String> getList(String filePath){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            List<String> list = new ArrayList<>();
            while (reader.ready()) {
                list.add(reader.readLine());
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
