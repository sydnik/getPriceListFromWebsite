package org.sydnik.by.sites.e_dostavka.data;

import java.time.LocalDate;
import java.util.Objects;

public class Product {
    private int id;
    private double price;
    private String name;
    private String country;
    private String subdirectory;
    private String directory;
    private LocalDate date;

    public Product(int id, String name, double price, String country, String subdirectory, String directory) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.country = country;
        this.subdirectory = subdirectory;
        this.directory = directory;
        this.date = LocalDate.now();
    }

    public Product(int id, String name, double price, String country, String subdirectory, String directory, LocalDate date) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.country = country;
        this.subdirectory = subdirectory;
        this.directory = directory;
        this.date = date;
    }

    public int getId() {
        return id;
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
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", subdirectory='" + subdirectory + '\'' +
                ", directory='" + directory + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(country, product.country) && Objects.equals(subdirectory, product.subdirectory) && Objects.equals(directory, product.directory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, country, subdirectory, directory);
    }
}
