package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Created by Knut Johan Hesten 2016-04-14.
 */

/**
 * Enumerable class that handles SQL generation for a variety of filters
 */
public enum Filtration {
    //TODO Endre første argument til entries i strings.xml
    NAME("Name", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_name, FiltrationRule.LIKE),
    LINE("Line", DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE,
            DatabaseHelper.FeedEntry.col_line_name, FiltrationRule.LIKE),
    MANUFACTURER("Producer", DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER,
            DatabaseHelper.FeedEntry.col_manufacturer_name, FiltrationRule.LIKE),
    POPULARITY("Poularity", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_totalrank, FiltrationRule.RANGE),
    STRENGTH("Strength", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_strength, FiltrationRule.RANGE),
    TASTE_NUMBER("Taste", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_taste1, FiltrationRule.TASTE_NUMBER),
    TASTE_TEXT("Taste text", GetSnusDB.TASTE_TABLE_ALIAS_1,
            DatabaseHelper.FeedEntry.col_taste_taste, FiltrationRule.LIKE),
    NICOTINE("Nicotine", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_nicotinelevel, FiltrationRule.RANGE),
    TYPE_NUMBER("Type", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_type, FiltrationRule.EXACT);

    private String GuiName, tableName, columnName;
    private FiltrationRule filtrationRule;
    private String filterSql;
    Filtration(String realName, String tableName, String columnName, FiltrationRule filtrationRule) {
        this.GuiName = realName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.filtrationRule = filtrationRule;
    }

    /**
     * Sets the filtration SQL string for a filter based on either a searchValue OR start and end ranges
     * @param startRange The minimum value in the range
     * @param endRange The maximum value in the range
     */
    public void setSearchValue(Double startRange, Double endRange) {
        this.filterSql = this.filtrationRule.getRule(this.tableName, this.columnName, startRange, endRange);
    }

    public void setSearchValue(Object searchValue) {
        if (searchValue.getClass() == Integer.class) {
            this.filterSql = this.filtrationRule.getRule(this.tableName, this.columnName, searchValue, null);
        } else if (searchValue.getClass() == Double.class) {
            this.filterSql = this.filtrationRule.getRule(this.tableName, this.columnName, searchValue, null);
        } else {
            this.filterSql = this.filtrationRule.getRule(this.tableName, this.columnName, searchValue.toString(), null);
        }
    }

    /**
     * Gets the SQL String with preset filtration rules applied
     * @return Returns SQL String value of the current filtration
     */
    public String filtrationString() {
        String s = this.filterSql;
        this.filterSql = "";
        return s;
    }

    /**
     * Returns the filtration SQL string for a filter with a fixed value
     * @param searchValue Delimiter for the rule. If Integer it finds exact, if String it finds nearest neighbour
     * @return Returns the SQL string value of the specific rule
     */
//    public String filtrationString(Object searchValue) {
//        if (searchValue.getClass() == Integer.class) {
//            return this.filtrationRule.getRule(this.tableName, this.columnName, (int)searchValue);
//        } else {
//            return this.filtrationRule.getRule(this.tableName, this.columnName, searchValue.toString());
//        }
//    }

    /**
     * Returns the filtration SQL string for a filter with range value
     * @param startRange The minimum value in the range
     * @param endRange The maximum value in the range
     * @return Returns the SQL string value of the specific rule
     */
//    public String filtrationString(Double startRange, Double endRange) {
//        return this.filtrationRule.getRule(this.tableName, this.columnName, startRange, endRange);
//    }

    /**
     * Gets the GUI name representation of the filtration rule
     * @return String
     */
//    public String getGuiName() {return this.GuiName;}

    public enum FiltrationRule {
        EXACT(" = ") {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + tableName + "." + columnName + " = " + String.valueOf(searchValue1) + " ";
            }
        },
        LIKE(" LIKE ") {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + tableName + "." + columnName + " LIKE " + " \"%" + searchValue1 + "%\" COLLATE NOCASE ";
            }
        },
        RANGE() {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + tableName + "." + columnName + " >= " + String.valueOf(searchValue1) +
                        " AND " + tableName + "." + columnName + " <= " + String.valueOf(searchValue2) + " ";
            }
        },
        TASTE_NUMBER() {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + tableName + "." + DatabaseHelper.FeedEntry.col_snus_taste1 + " = " + String.valueOf(searchValue1) +
                        " OR " + tableName + "." + DatabaseHelper.FeedEntry.col_snus_taste2 + " = " + String.valueOf(searchValue1) +
                        " OR " + tableName + "." + DatabaseHelper.FeedEntry.col_snus_taste3 + " = " + String.valueOf(searchValue1);
            }
        };

        protected abstract String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2);

        private String value;
        FiltrationRule(String value) {
            this.value = value;
        }

        FiltrationRule() { }
    }
}
