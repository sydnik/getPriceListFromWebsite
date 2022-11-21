package org.sydnik.by.sites.tabletka.enums;

public enum ItemAmount {
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);

    private int amount;

    ItemAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
