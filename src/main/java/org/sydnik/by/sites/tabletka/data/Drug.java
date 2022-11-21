package org.sydnik.by.sites.tabletka.data;

import java.util.List;

public class Drug {
    private int id;
    private String name;
    private String producer;
    private String producerCountry;
    private List<Pharmacy> pharmacies;
    private int minPrice;
    private int maxPrice;

    public Drug(int id, String name, String producer, String producerCountry) {
        this.id = id;
        this.name = name;
        this.producer = producer;
        this.producerCountry = producerCountry;
    }

    public void setPharmacyAndPrice(List<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public void setMinPrice() {
        //Реализовать поиск
    }

    public void setMaxPrice(int maxPrice) {
        //Реализовать поиск
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public String getProducerCountry() {
        return producerCountry;
    }

    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }


    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", producerCountry='" + producerCountry + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}
