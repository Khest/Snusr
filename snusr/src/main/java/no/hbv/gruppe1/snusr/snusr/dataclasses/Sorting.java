package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Enumerable class that handles sorting queries
 */
public enum  Sorting {
    //TODO Endre første argument til entries i strings.xml
    ALPHABETICAL("Sortering A-Z", DatabaseHelper.FeedEntry.col_line_name),
    POPULARITY("Popularitet", DatabaseHelper.FeedEntry.col_snus_totalrank),
    NOTBOOKMARKED("Ikke bokmarkert",DatabaseHelper.FeedEntry.col_mylist_bookmark),
    BOOKMARKED("Bokmarkert", DatabaseHelper.FeedEntry.col_mylist_bookmark),
    TASTE("Smak", DatabaseHelper.FeedEntry.col_snus_taste1),
    NICOTINELEVEL("Nikotinnivå", DatabaseHelper.FeedEntry.col_snus_nicotinelevel),
    TYPE("Type", DatabaseHelper.FeedEntry.col_snus_type),
    STRENGTH("Styrke", DatabaseHelper.FeedEntry.col_snus_strength);


    String guiName, columnName;
    Order order = Order.DESC;
    Sorting(String guiName, String columnName) {
        this.guiName = guiName;
        this.columnName = columnName;
    }

    public String getGuiName() {
        return guiName;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getAscending() { return Order.ASC; }

    public Order getDescending() { return Order.DESC; }

    public String getSql() {
        return " ORDER BY " + columnName +" " + this.order.toString();
    }


/** Enum class contains ASC and DESC **/
private enum Order {
    ASC("ASC"),
    DESC("DESC");

    private String string;
    Order(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
}
