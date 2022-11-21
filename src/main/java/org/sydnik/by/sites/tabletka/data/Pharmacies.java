package org.sydnik.by.sites.tabletka.data;

import org.sydnik.by.framework.utils.JsonUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Pharmacies {
    private List<String> pharmacies;

    public Pharmacies() {
        this.pharmacies = downloadPharmacies();
    }

    private List<String> downloadPharmacies(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(JsonUtil.getDataString("pathPharmaciesFile"))))) {
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

    public List<String> getPharmacies() {
        return pharmacies;
    }

    public void deletePharmacy(String name){
        pharmacies.remove(name);
    }
}
