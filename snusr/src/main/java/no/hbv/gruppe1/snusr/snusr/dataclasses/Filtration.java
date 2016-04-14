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
    NAME("Name", DatabaseHelper.FeedEntry.col_snus_name, FiltrationRule.LIKE),
    PRODUCER("Producer", DatabaseHelper.FeedEntry.col_snus_manufactorer, FiltrationRule.LIKE),
    POPULARITY("Poularity", DatabaseHelper.FeedEntry.col_snus_totalrank, FiltrationRule.RANGE),
    STRENGTH("Strength", DatabaseHelper.FeedEntry.col_snus_strength, FiltrationRule.RANGE),
    TASTE("Taste", DatabaseHelper.FeedEntry.col_snus_taste1, FiltrationRule.EXACT),
    NICOTINE("Nicotine", DatabaseHelper.FeedEntry.col_snus_nicotinelevel, FiltrationRule.RANGE),
    TYPE("Type", DatabaseHelper.FeedEntry.col_snus_type, FiltrationRule.EXACT);

    private String GuiName, colName;
    private FiltrationRule filtrationRule;
    Filtration(String name, String sql, FiltrationRule filtrationRule) {
        this.GuiName = name;
        this.colName = sql;
        this.filtrationRule = filtrationRule;
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
    public String filtrationString(double startRange, double endRange) {
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

        protected String getRule(String sqlSnippet, String searchValue) {
            return " " + sqlSnippet + " " + getValue() + " \"%" + searchValue + "%\" ";
        }

        protected String getRule(String sqlSnippet, double searchValue) {
            return " " + sqlSnippet + " " + getValue() + String.valueOf(searchValue) + " ";
        }
        protected String getRule(String sqlSnippet, double startRange, double endRange) {
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
