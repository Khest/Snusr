package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.provider.ContactsContract;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.interfaces.DatabaseGrabber;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Knut Johan Hesten 2016-05-26.
 */
public class GetSnusDB implements DatabaseGrabber {
    public static final int MYLIST_FAVOURITES = 1;
    public static final int MYLIST_BOOKMARKS  = 0;
    public static final int MYLIST_ALL        = -1;

    /**
     * Gets a Cursor to traverse snus data
     * @param context           Application context
     * @param filtrationList    A list of Filtration that should apply
     * @param sorting           The sorting method to use
     * @return                  Returns a cursor with all snus in accordance with filters
     */
    @Override
    public Cursor fetchSnus(Context context, List<Filtration> filtrationList, Sorting sorting) {
        StringBuilder sb = new StringBuilder();
        sb.append(snusDetailSqlJoinString());
        if (filtrationList != null) {
            sb.append(" WHERE ");
            for (int i = 0; i < filtrationList.size(); i++) {
                sb.append(filtrationList.get(i).getFilterSql());
                if (i != filtrationList.size() - 1) sb.append(" AND ");
            }
        }
        if (sorting == null) {
            sb.append(Sorting.ALPHABETICAL.getSql());
        } else {
            sb.append(sorting.getSql());
        }
        return dbCursor(context, sb.toString());
    }

    /**
     * Returns my favourites
     * @param context Application context
     * @param restriction Specify whether to get favourites, bookmarks or both via integer
     * @return Returns cursor of my favourites
     */
    @Override
    public Cursor fetchMyList(Context context, int restriction) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS);
        sb.append(snusDetailSqlJoinString());
        sb.append(" WHERE ").append(DatabaseHelper.FeedEntry.col_snus_id)
                .append(" = ")
                            .append(DatabaseHelper.FeedEntry.col_mylist_snusid);
        if (restriction != MYLIST_ALL) {
            sb.append(" AND ").append(DatabaseHelper.FeedEntry.col_mylist_bookmark)
                    .append(" = ").append(String.valueOf(restriction));
        }
        sb.append(Sorting.ALPHABETICAL.getSql());
        return dbCursor(context, sb.toString());
    }

    /**
     * Fetches a specific snus
     * @param context Application context
     * @param snusId The snus internal ID
     * @return Returns cursor containing the specific snus
     */
    @Override
    public Cursor fetchSpecificSnus(Context context, int snusId) {
        String sql = snusDetailSqlJoinString();
        sql += " WHERE " + DatabaseHelper.FeedEntry.col_snus_id + " = " + String.valueOf(snusId);
        return dbCursor(context, sql);
    }

    private Cursor dbCursor(Context context, String sqlString) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(sqlString, null);
    }

    private String snusDetailSqlJoinString() {
        StringBuilder sb = new StringBuilder();
        String tasteMain = DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE + "." + DatabaseHelper.FeedEntry.col_taste_id;
        String taste1 = "_TASTE1";
        String tasteCol1 = "TasteId1";
        String taste2 = "_TASTE2";
        String tasteCol2 = "TasteId2";
        String taste3 = "_TASTE3";
        String tasteCol3 = "TasteId3";
        sb.append("SELECT *, ")
                .append(taste1).append(".").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(" AS ").append(tasteCol1).append(", ")
                .append(taste2).append(".").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(" AS ").append(tasteCol2).append(", ")
                .append(taste3).append(".").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(" AS ").append(tasteCol3).append(" ")
                .append(" FROM ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS);
        sb.append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_line)
                .append(" = ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(".")
                    .append(DatabaseHelper.FeedEntry.col_line_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTORER).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_manufactorer)
                .append(" = ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTORER).append(".")
                        .append(DatabaseHelper.FeedEntry.col_manufactorer_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ").append(taste1).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_taste1)
                .append(" = ")
                    .append(taste1).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ").append(taste2).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_taste2)
                .append(" = ")
                    .append(taste2).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ").append(taste3).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_taste3)
                .append(" = ")
                    .append(taste3).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE).append(" ON ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                    .append(DatabaseHelper.FeedEntry.col_snus_type)
                .append(" = ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE).append(".")
                        .append(DatabaseHelper.FeedEntry.col_type_id);
        return sb.toString();
    }
}
