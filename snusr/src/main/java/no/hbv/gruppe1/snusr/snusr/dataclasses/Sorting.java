package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Created by Dakh on 2016-04-14.
 */
public enum  Sorting {
    ALFABETICAL("Sortering A-Z", DatabaseHelper.FeedEntry.col_line_name),
    POPULARITY("Popularitet", DatabaseHelper.FeedEntry.col_snus_totalrank),
    NONEBOOKMARKED("Ikke bokmarkert",""),
    BOOKMARKED("", ""),
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
        return "ORDER BY " + sql + order.toString();
    }

private enum Order{
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
