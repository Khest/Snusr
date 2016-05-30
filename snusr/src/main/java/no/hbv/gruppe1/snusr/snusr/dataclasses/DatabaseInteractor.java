package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.interfaces.DatabaseInteraction;

/**
 * Created by Knut Johan Hesten 2016-05-26.
 */
public class DatabaseInteractor implements DatabaseInteraction {
    public static final String TASTE_TABLE_ALIAS_1 = "_TASTE1";
    public static final String TASTE_COLUMN_ALIAS_1 = "TasteId1";
    public static final String TASTE_TABLE_ALIAS_2 = "_TASTE2";
    public static final String TASTE_COLUMN_ALIAS_2 = "TasteId2";
    public static final String TASTE_TABLE_ALIAS_3 = "_TASTE3";
    public static final String TASTE_COLUMN_ALIAS_3 = "TasteId3";

    private SQLiteDatabase db;

    /**
     * Gets a Cursor to traverse snus data
     * @param filtrationList    A list of Filtration that should apply
     * @param sorting           The sorting method to use
     * @return                  Returns a cursor with all snus in accordance with filters
     */
    @Override
    public Cursor fetchSnus(List<Filtration> filtrationList, Sorting sorting) {
        StringBuilder sb = new StringBuilder();
        sb.append(snusDetailSqlJoinString());
        if (filtrationList != null) {
            sb.append(" WHERE ");
            for (int i = 0; i < filtrationList.size(); i++) {
                sb.append(filtrationList.get(i).filtrationString());
                if (i != filtrationList.size() - 1) sb.append(" AND ");
            }
        }
        if (sorting == null) {
            sb.append(Sorting.ALPHABETICAL.getSql());
        } else {
            sb.append(sorting.getSql());
        }
        //Log.d(TAG, sb.toString());
        return dbCursor(sb.toString());
    }

    /**
     * Returns my favourites
     * @param restriction       Specify whether to get favourites, bookmarks or both via integer
     * @return                  Returns cursor containing the user's favourites
     */
    @Override
    public Cursor fetchMyList(int restriction) {
        StringBuilder sb = new StringBuilder();
        sb.append(snusDetailSqlJoinString());
        sb.append(" WHERE ").append(DatabaseHelper.FeedEntry.col_snus_id)
                .append(" = ")
                            .append(DatabaseHelper.FeedEntry.col_mylist_snusid);
        if (restriction != Globals.MYLIST_ALL) {
            sb.append(" AND ").append(DatabaseHelper.FeedEntry.col_mylist_bookmark)
                    .append(" = ").append(String.valueOf(restriction));
        }
        sb.append(Sorting.ALPHABETICAL.getSql());
        return dbCursor(sb.toString());
    }

    /**
     * Fetches a specific snus
     * @param snusId            The snus internal ID
     * @return                  Returns cursor containing the specific snus
     */
    @Override
    public Cursor fetchSpecificSnus(int snusId) {
        String sql = snusDetailSqlJoinString();
        sql += " WHERE " + DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS + "." +
                DatabaseHelper.FeedEntry.col_snus_id + " = " + String.valueOf(snusId);
        return dbCursor(sql);
    }

    /**
     * Fetches a list of manufacturers
     * @return                  Returns a navigable cursor containing id and names of the manufacturers
     */
    @Override
    public Cursor fetchManufacturers() {
        String sql = "SELECT " +
                    DatabaseHelper.FeedEntry.col_manufacturer_id + ", " +
                    DatabaseHelper.FeedEntry.col_manufacturer_name +
                " FROM " + DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER +
                Sorting.ALPHABETICAL.getSql();
        return dbCursor(sql);
    }

    private Cursor dbCursor(String sqlString) {
        return db.rawQuery(sqlString, null);
    }

    public DatabaseInteractor(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        context.openOrCreateDatabase(DatabaseHelper.FeedEntry.DATABASE_NAME, DatabaseHelper.DATABASE_VERSION, null);
        //try {
        //} catch (SQLiteCantOpenDatabaseException sqlex) {
        //    Log.e(Globals.TAG, "Fatal error opening database: " + sqlex.getMessage());
        //    throw new RuntimeException("OH GOD NO");
        //}
        this.db = databaseHelper.getReadableDatabase();
    }

    public String getMeta() {return this.db.getPath(); }

    public int getVersion() {return this.db.getVersion();}

    public void close() {
        this.db.close();
        this.db = null;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String snusDetailSqlJoinString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT *, ")
                .append(TASTE_TABLE_ALIAS_1).append(".").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(" AS ").append(TASTE_COLUMN_ALIAS_1).append(", ")
                .append(TASTE_TABLE_ALIAS_2).append(".").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(" AS ").append(TASTE_COLUMN_ALIAS_2).append(", ")
                .append(TASTE_TABLE_ALIAS_3).append(".").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(" AS ").append(TASTE_COLUMN_ALIAS_3).append(" ")
                .append(" FROM ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS);
        sb.append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_line)
                .append(" = ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(".")
                    .append(DatabaseHelper.FeedEntry.col_line_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_manufactorer)
                .append(" = ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER).append(".")
                        .append(DatabaseHelper.FeedEntry.col_manufacturer_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ").append(TASTE_TABLE_ALIAS_1).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_taste1)
                .append(" = ")
                    .append(TASTE_TABLE_ALIAS_1).append(".")
                        .append(DatabaseHelper.FeedEntry.col_taste_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ").append(TASTE_TABLE_ALIAS_2).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_taste2)
                .append(" = ")
                    .append(TASTE_TABLE_ALIAS_2).append(".")
                        .append(DatabaseHelper.FeedEntry.col_taste_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ").append(TASTE_TABLE_ALIAS_3).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_taste3)
                .append(" = ")
                    .append(TASTE_TABLE_ALIAS_3).append(".")
                        .append(DatabaseHelper.FeedEntry.col_taste_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_type)
                .append(" = ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE).append(".")
                        .append(DatabaseHelper.FeedEntry.col_type_id)
//                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(" ON ")
//                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
//                        .append(DatabaseHelper.FeedEntry.col_snus_line)
//                .append(" = ")
//                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(".")
//                        .append(DatabaseHelper.FeedEntry.col_line_id)
        ;
        return sb.toString();
    }
}
