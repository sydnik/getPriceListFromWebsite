package org.sydnik.by.sites.tabletka.enums;

public enum SortType {
    NAME_UP( "name","Наименованию","up"),
    NAME_DOWN( "name","Наименованию","down"),
    ADDRESS_UP("address", "Адресу", "up"),
    ADDRESS_DOWN("address", "Адресу", "down"),
    DATE_UP("date", "Дате обновления", "up"),
    DATE_DOWN("date", "Дате обновления", "down"),
    PRICE_UP("price", "Цене", "up"),
    PRICE_DOWN("price", "Цене", "down"),
    AMOUNT_UP("amount", "Количеству упаковок", "up"),
    AMOUNT_DOWN("amount", "Количеству упаковок", "down");

    private String sort;
    private String name;
    private String upOrDown;

    SortType(String sort, String name, String upOrDown) {
        this.sort = sort;
        this.name = name;
        this.upOrDown = upOrDown;
    }

    public String getSort() {
        return sort;
    }

    public String getName() {
        return name;
    }

    public String getUpOrDown() {
        return upOrDown;
    }
}
