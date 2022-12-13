package org.sydnik.by.sites.e_dostavka.data;

import java.time.LocalDate;
import java.util.Objects;

public class Product {
    private int id;
    private int itemCode;
    private double price;
    private String name;
    private String country;
    private String subdirectory;
    private String directory;
    private LocalDate date;
    private boolean food;
    //Скорее всего нужно через мап сделать цены и даты

    public Product(int itemCode, String name, double price, String country, String subdirectory, String directory, boolean food) {
        this.itemCode = itemCode;
        this.price = price;
        this.name = name;
        this.country = country;
        this.subdirectory = subdirectory;
        this.directory = directory;
        this.date = LocalDate.now();
        this.food = food;
    }

    public Product(int id, int itemCode, String name, double price, String country, String subdirectory, String directory, LocalDate date, boolean food) {
        this.id = id;
        this.itemCode = itemCode;
        this.price = price;
        this.name = name;
        this.country = country;
        this.subdirectory = subdirectory;
        this.directory = directory;
        this.date = date;
        this.food = food;
    }

    public int getId(){
        return id;
    }

    public int getItemCode() {
        return itemCode;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getSubdirectory() {
        return subdirectory;
    }

    public String getDirectory() {
        return directory;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return itemCode == product.itemCode && Double.compare(product.price, price) == 0 && food == product.food && Objects.equals(name, product.name) && Objects.equals(country, product.country) && Objects.equals(subdirectory, product.subdirectory) && Objects.equals(directory, product.directory) && Objects.equals(date, product.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemCode, price, name, country, subdirectory, directory, date, food);
    }

    public boolean isFood() {
        return food;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                "itemCode=" + itemCode +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", subdirectory='" + subdirectory + '\'' +
                ", directory='" + directory + '\'' +
                ", date=" + date +
                ", food=" + food +
                '}';
    }

}
