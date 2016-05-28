package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Created by Knut Johan Hesten on 2016-04-14
 *
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
    private Order order = Order.DESCENDING;
    Sorting(String guiName, String columnName) {
        this.guiName = guiName;
        this.columnName = columnName;
    }

    public String getGuiName() {
        return guiName;
    }

    /**
     * Sets the current order that the sorting should use
     * @param order     {@link Order} for the sorting. Either ascending or descending
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Gets the SQL string for the order
     * @return          Returns SQL order
     */
    public String getSql() {
        return " ORDER BY " + columnName +" " + this.order.getString();
    }


    /** Enum class that handles ascending or descending ordering **/
    public enum Order {
        ASCENDING("ASC"),
        DESCENDING("DESC");

        private String string;
        Order(String string) {
            this.string = string;
        }

        /**
         * Gets the SQL representation of the order
         * @return      Returns SQL representation of the order
         */
        public String getString() {
            return string;
        }
}
}
