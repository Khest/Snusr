package no.hbv.gruppe1.snusr.snusr.dataclasses;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.interfaces.ImageHandlerInterface;

/**
 * Created by Knut Johan Hesten on 2016-04-14.
 */

/**
 * Enumerable class that handles SQL generation for a variety of filters
 */
public enum Filtration {
    //TODO Endre første argument til entries i strings.xml
    NAME("Name", DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
            DatabaseHelper.FeedEntry.col_snus_name, FiltrationRule.LIKE),
    PRODUCER("Producer", null,
            DatabaseHelper.FeedEntry.col_snus_manufactorer, FiltrationRule.LIKE),
    POPULARITY("Poularity", null,
            DatabaseHelper.FeedEntry.col_snus_totalrank, FiltrationRule.RANGE),
    STRENGTH("Strength", null,
            DatabaseHelper.FeedEntry.col_snus_strength, FiltrationRule.RANGE),
    TASTE("Taste", null,
            DatabaseHelper.FeedEntry.col_snus_taste1, FiltrationRule.EXACT),
    NICOTINE("Nicotine", null,
            DatabaseHelper.FeedEntry.col_snus_nicotinelevel, FiltrationRule.RANGE),
    TYPE("Type", null,
            DatabaseHelper.FeedEntry.col_snus_type, FiltrationRule.EXACT);

    private String GuiName, colName;
    private FiltrationRule filtrationRule;
    private String filterSql;
    Filtration(String realName, String tableName, String columnName, FiltrationRule filtrationRule) {
        this.GuiName = realName;
        this.colName = columnName;
        this.filtrationRule = filtrationRule;
    }

    /**
     * Sets the filtration SQL string for a filter based on either a searchValue OR start and end ranges
     * @param searchValue The value to search for
     * @param startRange The minimum value in the range
     * @param endRange The maximum value in the range
     */
    public void defineVariables(Object searchValue, Double startRange, Double endRange) throws Exception {
        if (startRange != null && endRange != null && searchValue == null || searchValue.toString().equals("")) {
            this.filtrationRule.getRule(this.colName, startRange, endRange);
        } else if (!searchValue.equals("")) {
            if (searchValue.getClass() == Integer.class) {
//                return this.filtrationRule.getRule(this.colName, (int)searchValue);
                this.filterSql = this.filtrationRule.getRule(this.colName, (int)searchValue);
            } else {
                this.filterSql = this.filtrationRule.getRule(this.colName, searchValue.toString());
//                return this.filtrationRule.getRule(this.colName, searchValue.toString());
            }
        } else {
            throw new Exception("No valid filter variable defined");
        }
    }

    /**
     * Gets the SQL String of the current filtration
     * @return Returns SQL String value of the current filtration
     */
    public String getFilterSql() {
        String s = this.filterSql;
        this.filterSql = "";
        return s;
    }

    /**
     * Returns the filtration SQL string for a filter with a fixed value
     * @param searchValue Delimiter for the rule. If Integer it finds exact, if String it finds nearest neighbour
     * @return Returns the SQL string value of the specific rule
     */
    public String filtrationString(Object searchValue) {
        if (searchValue.getClass() == Integer.class) {
            return this.filtrationRule.getRule(this.colName, (int)searchValue);
        } else {
            return this.filtrationRule.getRule(this.colName, searchValue.toString());
        }
    }

    /**
     * Returns the filtration SQL string for a filter with range value
     * @param startRange The minimum value in the range
     * @param endRange The maximum value in the range
     * @return Returns the SQL string value of the specific rule
     */
    public String filtrationString(Double startRange, Double endRange) {
        return this.filtrationRule.getRule(this.colName, startRange, endRange);
    }

    /**
     * Gets the GUI name representation of the filtration rule
     * @return String
     */
    public String getGuiName() {return this.GuiName;}

    public enum FiltrationRule {
        EXACT(" = "),
        LIKE(" LIKE "),
        RANGE();

        protected String getRule(String columnName, String searchValue) {
            return " " + columnName + " " + getValue() + " \"%" + searchValue + "%\" ";
        }

        protected String getRule(String sqlSnippet, double searchValue) {
            return " " + sqlSnippet + " " + getValue() + String.valueOf(searchValue) + " ";
        }
        protected String getRule(String sqlSnippet, Double startRange, Double endRange) {
            return " " + sqlSnippet + " > " + String.valueOf(startRange) + " AND " + sqlSnippet + " < " + String.valueOf(endRange) + " ";
        }

        private String getValue() {return this.value;}

        private String value;
        FiltrationRule(String value) {
            this.value = value;
        }

        FiltrationRule() { }
    }
}
