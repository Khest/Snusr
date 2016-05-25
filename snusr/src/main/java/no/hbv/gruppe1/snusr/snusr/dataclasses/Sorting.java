package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Enumerable class that handles sorting queries
 */
public enum  Sorting {
    //TODO Endre første argument til entries i strings.xml
    ALFABETICAL("Sortering A-Z", DatabaseHelper.FeedEntry.col_line_name),
    POPULARITY("Popularitet", DatabaseHelper.FeedEntry.col_snus_totalrank),
    NONEBOOKMARKED("Ikke bokmarkert",DatabaseHelper.FeedEntry.col_mylist_bookmark),
    BOOKMARKED("Bokmarkert", DatabaseHelper.FeedEntry.col_mylist_bookmark),
    TASTE("Smak", DatabaseHelper.FeedEntry.col_snus_taste1),
    NICOTINLEVEL("Nikotinnivå", DatabaseHelper.FeedEntry.col_snus_nicotinelevel),
    TYPE("Type", DatabaseHelper.FeedEntry.col_snus_type),
    STRENGHT("Styrke", DatabaseHelper.FeedEntry.col_snus_strength);


    String guiName, sql;
    Sorting(String guiName, String sql) {
        this.guiName = guiName;
        this.sql = sql;
    }

    public String getGuiName() {
        return guiName;
    }

    public String getSql(Order order) {
        return " ORDER BY " + sql +" " + order.toString();
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
