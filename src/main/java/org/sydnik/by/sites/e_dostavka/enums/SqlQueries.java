package org.sydnik.by.sites.e_dostavka.enums;

public enum SqlQueries {
    COUNTRY("SELECT id FROM countries WHERE name = ?", "INSERT INTO countries (name)  values (?)"),
    DIRECTORY("SELECT id FROM directories WHERE name = ?", "INSERT INTO directories (name)  values (?)"),
    SUBDIRECTORY("SELECT id FROM subdirectories WHERE name = ?", "INSERT INTO subdirectories (name)  values (?)"),
    PRODUCTS("SELECT  products.id, products.itemcode, products.name, countries.name,\n" +
            "       directories.name, subdirectories.name, products.food, prices.`PRICE_DATE`\n" +
            "FROM  products LEFT OUTER JOIN directories  ON directory = directories.id\n" +
            "               LEFT OUTER JOIN countries ON country = countries.id\n" +
            "               LEFT OUTER JOIN subdirectories on products.subdirectory = subdirectories.id\n" +
            "               LEFT OUTER JOIN prices on products.id = prices.idProduct\n" +
            "WHERE prices.`PRICE_DATE` IS NOT NULL;");

    private String get;
    private String add;

    SqlQueries(String get, String add) {
        this.get = get;
        this.add = add;
    }

    SqlQueries(String get){
        this.get = get;
    }


    public String getGet() {
        return get;
    }

    public String getAdd() {
        return add;
    }
}
