package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Created by Knut Johan Hesten 2016-04-14.
 *
 * Enumerable class that handles SQL generation for a variety of filters
 */
public enum Filtration {
    //TODO Endre første argument til entries i strings.xml
    NAME("Name", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_name, FiltrationRule.LIKE),
    LINE_NUMBER("Line", DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE,
            DatabaseHelper.FeedEntry.col_line_id, FiltrationRule.EXACT),
    LINE_TEXT("Line", DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE,
            DatabaseHelper.FeedEntry.col_line_name, FiltrationRule.LIKE),
    MANUFACTURER("Producer", DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER,
            DatabaseHelper.FeedEntry.col_manufacturer_name, FiltrationRule.LIKE),
    MANUFACTURER_NUMBER("Producer", DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER,
            DatabaseHelper.FeedEntry.col_manufacturer_id, FiltrationRule.EXACT),
    POPULARITY("Poularity", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_totalrank, FiltrationRule.RANGE),
    STRENGTH("Strength", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_strength, FiltrationRule.RANGE),
    TASTE_NUMBER("Taste", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_taste1, FiltrationRule.TASTE_NUMBER),
    TASTE_TEXT("Taste text", DatabaseInteractor.TASTE_TABLE_ALIAS_1,
            DatabaseHelper.FeedEntry.col_taste_taste, FiltrationRule.LIKE),
    NICOTINE("Nicotine", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_nicotinelevel, FiltrationRule.RANGE),
    TYPE_NUMBER("Type", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_type, FiltrationRule.EXACT),
    TYPE_TEXT("Type", DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE,
            DatabaseHelper.FeedEntry.col_type_text, FiltrationRule.LIKE),
    WILDCARD("Wildcard", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_name, FiltrationRule.WILDCARD),
    NAME_EXACT("Exact name", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_name, FiltrationRule.EXACT_STRING);

    private String guiName, tableName, columnName;
    private FiltrationRule filtrationRule;
    private String filterSql;
    Filtration(String guiName, String tableName, String columnName, FiltrationRule filtrationRule) {
        this.guiName = guiName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.filtrationRule = filtrationRule;
    }

    @Override
    public String toString() {
        return this.guiName;
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

    public enum FiltrationRule {
        EXACT() {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + tableName + "." + columnName + " = " + String.valueOf(searchValue1) + " ";
            }
        },
        LIKE() {
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
                        " AND " + tableName + "." + DatabaseHelper.FeedEntry.col_snus_taste2 + " = " + String.valueOf(searchValue1) +
                        " AND " + tableName + "." + DatabaseHelper.FeedEntry.col_snus_taste3 + " = " + String.valueOf(searchValue1) + " ";
            }
        },
        WILDCARD() {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS + "." + DatabaseHelper.FeedEntry.col_snus_name +
                            " LIKE \"%" + String.valueOf(searchValue1) + "%\"" +
                        " OR " + DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE + "." + DatabaseHelper.FeedEntry.col_line_name +
                            " LIKE \"%" + String.valueOf(searchValue1) + "%\"" +
                        " OR " + DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER + "." + DatabaseHelper.FeedEntry.col_manufacturer_name +
                            " LIKE \"%" + String.valueOf(searchValue1) + "%\"" +
                        " OR " + DatabaseInteractor.TASTE_TABLE_ALIAS_1 + "." + DatabaseHelper.FeedEntry.col_taste_taste +
                            " LIKE \"%" + String.valueOf(searchValue1 + "%\"" +
                        " OR " + DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER + "." + DatabaseHelper.FeedEntry.col_manufacturer_country +
                            " LIKE \"%" + String.valueOf(searchValue1)) + "%\"" +
                        " ";
            }
        },
        EXACT_STRING() {
            protected String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2) {
                return " " + tableName + "." + columnName + " = \"" + String.valueOf(searchValue1) + "\" ";
            }
        }
        ;

        /**
         * Gets the specific rule for the current FiltrationRule
         * @param tableName         Table name to apply filtration to
         * @param columnName        Column name to apply filtration to
         * @param searchValue1      The first search value.
         * @param searchValue2      Optional second search value. If included defines a range to reach for
         * @return                  Returns the sql string for the filtration value
         */
        protected abstract String getRule(String tableName, String columnName, Object searchValue1, Object searchValue2);

        FiltrationRule() { }
    }
}
