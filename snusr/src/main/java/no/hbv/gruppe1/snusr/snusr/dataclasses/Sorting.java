package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Enumerable class that handles sorting queries
 */
public enum  Sorting {
    //TODO Endre første argument til entries i strings.xml
    ALPHABETICAL("Sortering A-Z", DatabaseHelper.FeedEntry.col_line_name),
    POPULARITY("Popularitet", DatabaseHelper.FeedEntry.col_snus_totalrank),
    TASTE("Smak", DatabaseHelper.FeedEntry.col_snus_taste1),
    NICOTINELEVEL("Nikotinnivå", DatabaseHelper.FeedEntry.col_snus_nicotinelevel),
    TYPE("Type", DatabaseHelper.FeedEntry.col_snus_type),
    STRENGTH("Styrke", DatabaseHelper.FeedEntry.col_snus_strength),
    NEWESTTOOLDEST("Nyeste til gamleste", DatabaseHelper.FeedEntry.col_snus_id);


    private String guiName, columnName;
    private Order order = Order.DESC;
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

    public String getSql() {
        return " ORDER BY " + columnName +" " + this.order.toString();
    }


/** Enum class contains ASC and DESC **/
public enum Order {
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
