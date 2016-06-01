package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.interfaces.DatabaseInteraction;

/**
 * Created by Knut Johan Hesten 2016-05-26.
 */
public class DatabaseInteractor implements DatabaseInteraction {
    public static final String TASTE_TABLE_ALIAS_1 = "_TASTE1";
    public static final String TASTE_COLUMN_ALIAS_1 = "TasteId1";
    public static final String TASTE_COLUMN_TEXT_ALIAS_1 = "taste_text_1";
    public static final String TASTE_TABLE_ALIAS_2 = "_TASTE2";
    public static final String TASTE_COLUMN_ALIAS_2 = "TasteId2";
    public static final String TASTE_COLUMN_TEXT_ALIAS_2 = "taste_text_2";
    public static final String TASTE_TABLE_ALIAS_3 = "_TASTE3";
    public static final String TASTE_COLUMN_ALIAS_3 = "TasteId3";
    public static final String TASTE_COLUMN_TEXT_ALIAS_3 = "taste_text_3";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Context context;

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
        sb.append(leftJoinMyList());
        if (filtrationList != null) {
            sb.append(" WHERE ");
            Log.i(Globals.TAG, "FiltrationListSize = " + filtrationList.size());
            for (int i = 0; i < filtrationList.size(); i++) {
                sb.append(filtrationList.get(i).filtrationString());
                Log.i(Globals.TAG, "CurrentFiltration = " + i);
                if (i < filtrationList.size() - 1) sb.append(" AND ");
//                Log.i(Globals.TAG, sb.toString());
            }
        }
        if (sorting == null) {
            sb.append(Sorting.ALPHABETICAL.getSql());
        } else {
            sb.append(sorting.getSql());
        }
        return dbCursor(sb.toString());
    }

    /**
     * Sets a ranking in the personal list, or if the snus does not exist there inserts it into the personal list
     * @param snusId        The snus ID
     * @param ranking       Snus ranking
     * @return              Returns int depending on what action was performed
     */
    @Override
    public int updatePersonalRankingAndUpdateAverage(int snusId, double ranking) {
        Cursor c = fetchSpecificSnus(snusId);
        c.moveToFirst();
        if (c.getInt(c.getColumnIndex(DatabaseHelper.FeedEntry.col_mylist_snusid)) != 0) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.FeedEntry.col_mylist_myrank, ranking);
            update(DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST, cv, DatabaseHelper.FeedEntry.col_mylist_snusid + "=" + String.valueOf(snusId), null);
            updateAndSetAverage(snusId, c.getDouble(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_totalrank)), ranking);
            return Globals.MYLIST_RATED;
        } else {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.FeedEntry.col_mylist_snusid, snusId);
            cv.put(DatabaseHelper.FeedEntry.col_mylist_myrank, ranking);
            long insertValue = insert(DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST, cv);
            updateAndSetAverage(snusId, c.getDouble(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_totalrank)), ranking);
            return Globals.MYLIST_ADDED;
        }
    }

    /**
     * Updates and sets the average in the main snus list
     */
    private void updateAndSetAverage(int snusId, double currentRanking, double incomingRanking) {
        double newRanking = currentRanking + incomingRanking / 2;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.FeedEntry.col_snus_totalrank, newRanking);
        update(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS, cv, DatabaseHelper.FeedEntry.col_snus_id + "=" + String.valueOf(snusId), null);
    }

    /**
     * Sets or unsets the bookmark flag on a snus. If the snus does not exist in my list it will insert it.
     * @param snusId    ID of the snus to set
     * @return          Returns an int describing whether the snus was added to or removed from bookmarks
     */
    @Override
    public int setMyListBookmarked(int snusId) {
        Cursor c = fetchSpecificSnus(snusId);
        c.moveToFirst();
        ContentValues cv = new ContentValues();
        if (c.getInt(c.getColumnIndex(DatabaseHelper.FeedEntry.col_mylist_snusid)) != 0) {
            if (c.getInt(c.getColumnIndex(DatabaseHelper.FeedEntry.col_mylist_bookmark)) == Globals.MYLIST_BOOKMARKED) {
                cv.put(DatabaseHelper.FeedEntry.col_mylist_bookmark, Globals.MYLIST_REMOVED_FROM_BOOKMARKS);
                update(
                        DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST,
                        cv,
                        DatabaseHelper.FeedEntry.col_mylist_snusid + "=" + String.valueOf(snusId),
                        null);
                return Globals.MYLIST_REMOVED_FROM_BOOKMARKS;
            } else {
                cv.put(DatabaseHelper.FeedEntry.col_mylist_bookmark, Globals.MYLIST_BOOKMARKED);
                update(
                        DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST,
                        cv,
                        DatabaseHelper.FeedEntry.col_mylist_snusid + "=" + String.valueOf(snusId),
                        null);
                return Globals.MYLIST_BOOKMARKED;

            }
        } else {
            cv.put(DatabaseHelper.FeedEntry.col_mylist_snusid, snusId);
            cv.put(DatabaseHelper.FeedEntry.col_mylist_bookmark, Globals.MYLIST_BOOKMARKED);
            insert(
                    DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST,
                    cv);
            return Globals.MYLIST_ADDED;
        }
    }

    //updateRankingPersonalAndAverage(int 1 -> if exist in my list update ranking i mylist else insert and set ranking,

    //setMyListBookmarked

    /**
     * Returns my favourites
     * @param restriction       Specify whether to get favourites, bookmarks or both via integer
     * @return                  Returns cursor containing the user's favourites
     */
    @Override
    public Cursor fetchMyList(int restriction) {
        StringBuilder sb = new StringBuilder();
        sb.append(snusDetailSqlJoinString());
        sb.append(leftJoinMyList());
        if (restriction != Globals.MYLIST_ALL) {
            sb.append(" WHERE ").append(DatabaseHelper.FeedEntry.col_mylist_bookmark)
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
        String sql = snusDetailSqlJoinString() + leftJoinMyList();
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
                    DatabaseHelper.FeedEntry.col_manufacturer_id + " AS _id, " +
                    DatabaseHelper.FeedEntry.col_manufacturer_name +
                " FROM " + DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER +
                Sorting.MANUFACTURER.getSql();
        return dbCursor(sql);
    }

    /**
     * Fetches a list of lines
     * @param manufacturerId    ID of the manufacturer
     * @return                  Returns a cursor containing id and names of lines
     */
    @Override
    public Cursor fetchLines(int manufacturerId) {
        String sql = "SELECT " +
                DatabaseHelper.FeedEntry.col_line_id + " AS _id, " +
                DatabaseHelper.FeedEntry.col_line_name +
                " FROM " + DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE +
                " WHERE " + DatabaseHelper.FeedEntry.col_line_manufactorer +
                " = " + String.valueOf(manufacturerId) +
                Sorting.LINE.getSql();
        return dbCursor(sql);
    }

    /**
     *
     * @return  Returns all lines in the database
     */
    @Override
    public Cursor fetchLines() {
        String sql = "SELECT " +
                DatabaseHelper.FeedEntry.col_line_id + " AS _id, " +
                DatabaseHelper.FeedEntry.col_line_name +
                " FROM " + DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE;
        return dbCursor(sql);
    }

    /**
     *
     * @return  Returns all tastes
     */
    @Override
    public Cursor fetchTastes() {
        String sql = "SELECT " +
                DatabaseHelper.FeedEntry.col_taste_id + " AS _id, " +
                DatabaseHelper.FeedEntry.col_taste_taste +
                " FROM " + DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE;
        return dbCursor(sql);
    }

    /**
     *
     * @return  Returns all types
     */
    @Override
    public Cursor fetchTypes() {
        String sql = "SELECT " +
                DatabaseHelper.FeedEntry.col_type_id + " AS _id, " +
                DatabaseHelper.FeedEntry.col_type_text +
                " FROM " + DatabaseHelper.FeedEntry.DATABASE_TABLE_TYPE;
        return dbCursor(sql);
    }

    /**
     * Update an entry in the database
     * @param table         The table to update
     * @param values        The {@link ContentValues} containing the key/value pairs to update
     * @param whereClause   The where clause
     * @param whereArgs     Where arguments. Can be null
     */
    @Override
    public void update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        try (SQLiteDatabase writeable = this.databaseHelper.getWritableDatabase()) {
            writeable.update(table, values, whereClause, whereArgs);

        } catch (Exception ex) {
            Log.e(Globals.TAG, "Fatal error on update " + ex.getMessage());
        }
    }

    /**
     * Inserts into the database
     * @param table     The table to insert into
     * @param values    The {@link ContentValues} containing the key/value pairs to insert
     */
    @Override
    public long insert(String table, ContentValues values) {
        try (SQLiteDatabase writeable = this.databaseHelper.getWritableDatabase()) {
           return writeable.insert(table, null, values);
        } catch (Exception ex) {
            Log.e(Globals.TAG, "Fatal error on insertion " + ex.getMessage());
            return -1;
        }
    }


    private Cursor dbCursor(String sqlString) {
        if (!db.isOpen()) {
            Log.i(Globals.TAG, "Database has been closed. Reopening.");
            db = databaseHelper.getWritableDatabase();
        }
        return db.rawQuery(sqlString, null);
    }

    public DatabaseInteractor(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);

        //context.openOrCreateDatabase(DatabaseHelper.FeedEntry.DATABASE_NAME, DatabaseHelper.DATABASE_VERSION, null);
        //try {
        //} catch (SQLiteCantOpenDatabaseException sqlex) {
        //    Log.e(Globals.TAG, "Fatal error opening database: " + sqlex.getMessage());
        //    throw new RuntimeException("OH GOD NO");
        //}
        this.db = databaseHelper.getReadableDatabase();
//        db.close();
    }

    public String getMeta() {return this.db.getPath(); }

    public int getVersion() {return this.db.getVersion();}

    public void close() {
        if (this.db != null) {
            Log.i(Globals.TAG, "Closing database from " + this.getClass().getName());
            databaseHelper.close();
            this.db.close();
        } else {
            Log.i(Globals.TAG, "Unable to decouple from database. The database object is null");
        }
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
                    .append(" AS ").append(TASTE_COLUMN_ALIAS_3).append(", ")
                .append(TASTE_TABLE_ALIAS_1).append(".").append(DatabaseHelper.FeedEntry.col_taste_taste)
                    .append(" AS ").append(TASTE_COLUMN_TEXT_ALIAS_1).append(", ")
                .append(TASTE_TABLE_ALIAS_2).append(".").append(DatabaseHelper.FeedEntry.col_taste_taste)
                    .append(" AS ").append(TASTE_COLUMN_TEXT_ALIAS_2).append(", ")
                .append(TASTE_TABLE_ALIAS_3).append(".").append(DatabaseHelper.FeedEntry.col_taste_taste)
                    .append(" AS ").append(TASTE_COLUMN_TEXT_ALIAS_3).append(" ")
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
        ;
        return sb.toString();
    }

    private String leftJoinMyList() {
        StringBuilder sb = new StringBuilder();
        sb.append(" LEFT JOIN ")
                .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST).append(" ON ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS).append(".")
                        .append(DatabaseHelper.FeedEntry.col_snus_id)
                .append(" = ")
                    .append(DatabaseHelper.FeedEntry.DATABASE_TABLE_MYLIST).append(".")
                        .append(DatabaseHelper.FeedEntry.col_mylist_snusid)
                .append(" ")
                ;
        return sb.toString();
    }
}
