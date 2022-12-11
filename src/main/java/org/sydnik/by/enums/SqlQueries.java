package org.sydnik.by.enums;

public enum SqlQueries {
    COUNTRY("SELECT id FROM countries WHERE name = ?", "INSERT INTO countries (name)  values (?)"),
    DIRECTORY("SELECT id FROM directories WHERE name = ?", "INSERT INTO directories (name)  values (?)"),
    SUBDIRECTORY("SELECT id FROM subdirectories WHERE name = ?", "INSERT INTO subdirectories (name)  values (?)");

    private String get;
    private String add;

    SqlQueries(String get, String add) {
        this.get = get;
        this.add = add;
    }


    public String getGet() {
        return get;
    }

    public String getAdd() {
        return add;
    }
}
