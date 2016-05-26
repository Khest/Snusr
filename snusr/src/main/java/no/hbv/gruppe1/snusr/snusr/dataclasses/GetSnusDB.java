package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.provider.ContactsContract;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.interfaces.DatabaseGrabber;

import java.util.List;

/**
 * Created by Long Huynh on 14.04.2016.
 */
public class GetSnusDB implements DatabaseGrabber {
    /**
     * Gets a Cursor to traverse snus data
     * @param context           Application context
     * @param filtrationList    A list of Filtration that should apply
     * @param sorting           The sorting method to use
     * @return                  Returns a cursor containing the database with the aforementioned restrictions
     */
    @Override
    public Cursor fetchSnus(Context context, List<Filtration> filtrationList, Sorting sorting) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS);
        sb.append(snusDetailSqlJoinString());
        if (filtrationList != null) {
            sb.append(" WHERE ");
            for (int i = 0; i > filtrationList.size(); i++) {
                sb.append(filtrationList.get(i).getFilterSql());
                if (i != filtrationList.size()) sb.append(" AND ");
            }
        }
        if (sorting == null) {
            sb.append(Sorting.ALPHABETICAL.getSql());
        } else {
            sb.append(sorting.getSql());
        }
        return dbCursor(context, sb.toString());
    }

    @Override
    public Cursor fetchMyList(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS);
        sb.append(snusDetailSqlJoinString());
        sb.append(Sorting.ALPHABETICAL.getSql());
        return dbCursor(context, sb.toString());
    }

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
        sb.append("SELECT * FROM ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS);
        sb.append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE).append(" ON ")
                .append(DatabaseHelper.FeedEntry.col_snus_line).append(" = ").append(DatabaseHelper.FeedEntry.col_line_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTORER).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.col_snus_manufactorer).append(" = ").append(DatabaseHelper.FeedEntry.col_manufactorer_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.col_snus_taste1).append(" = ").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(DatabaseHelper.FeedEntry.col_snus_taste2).append(" = ").append(DatabaseHelper.FeedEntry.col_taste_id)
                    .append(DatabaseHelper.FeedEntry.col_snus_taste3).append(" = ").append(DatabaseHelper.FeedEntry.col_taste_id)
                .append(" LEFT JOIN ").append(DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE).append(" ON ")
                .append(DatabaseHelper.FeedEntry.col_snus_type).append(" = ").append(DatabaseHelper.FeedEntry.col_type_id);
        return sb.toString();
    }
}
