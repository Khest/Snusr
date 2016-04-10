package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Håkon Stensheim on 10.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public static final int DATABASE_VERSION = 1;
    public static final String INTEGER = "INTEGER";
    public static final String DOUBLE = "DOUBLE";
    public static final String TEXT = "TEXT";
    public static final String CREATE_TABLE_SNUS = "CREATE TABLE " + FeedEntry.DATABASE_TABLE_SNUS + "("
            + FeedEntry.col_snus_id + " INTEGER PRIMARY KEY, "
            + FeedEntry.col_snus_manufactorer + " " + INTEGER + ", "
            + FeedEntry.col_snus_line + " " + INTEGER + ", "
            + FeedEntry.col_snus_taste1 + " " + INTEGER + ", "
            + FeedEntry.col_snus_taste2 + " " + INTEGER + ", "
            + FeedEntry.col_snus_taste3 + " " + INTEGER + ", "
            + FeedEntry.col_snus_strength + " " + DOUBLE+ ", "
            + FeedEntry.col_snus_nicotinelevel + " " + DOUBLE + ", "
            + FeedEntry.col_snus_totalrank + " " + DOUBLE + ", "
            + FeedEntry.col_snus_type + " " + INTEGER + ", "
            + "FOREIGN KEY (" + FeedEntry.col_snus_manufactorer + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_MANUFACTORER + "(" + FeedEntry.col_manufactorer_id + "), "
            + "FOREIGN KEY (" + FeedEntry.col_snus_line + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_LINE + "(" + FeedEntry.col_line_id + "), "
            + "FOREIGN KEY (" + FeedEntry.col_snus_taste1 + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_TASTE + "(" + FeedEntry.col_taste_id + "), "
            + "FOREIGN KEY (" + FeedEntry.col_snus_taste2 + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_TASTE + "(" + FeedEntry.col_taste_id + "), "
            + "FOREIGN KEY (" + FeedEntry.col_snus_taste3 + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_TASTE + "(" + FeedEntry.col_taste_id + "), "
            + "FOREIGN KEY (" + FeedEntry.col_snus_type + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_TYPE + "(" + FeedEntry.col_type_id + "));"
            ;

    public static final String CREATE_TABLE_LINE = "CREATE TABLE " + FeedEntry.DATABASE_TABLE_LINE + "("
            + FeedEntry.col_line_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_line_manufactorer + " " + INTEGER + ", "
            + FeedEntry.col_line_name + " " + TEXT + ", "
            + "FOREIGN KEY (" + FeedEntry.col_line_manufactorer + ") "
            + "REFERENCES " + FeedEntry.DATABASE_TABLE_MANUFACTORER + "("
            + FeedEntry.col_manufactorer_id + ")" + ");";

    public static final String CREATE_TABLE_MANUFACTORER = "CREATE TABLE " + FeedEntry.DATABASE_TABLE_MANUFACTORER
            + "(" + FeedEntry.col_manufactorer_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_manufactorer_name + " " + TEXT + ", "
            + FeedEntry.col_manufactorer_country + " " + TEXT + ", "
            + FeedEntry.col_manufactorer_url + " " + TEXT+ ");";

    public static final String CREATE_TABLE_TASTE = "CREATE TABLE " + FeedEntry.DATABASE_TABLE_TASTE + "("
            + FeedEntry.col_taste_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_taste_taste + " " + TEXT + ");";

    public static final String CREATE_TABLE_TYPE = "CREATE TABLE " + FeedEntry.DATABASE_TABLE_TYPE + "("
            + FeedEntry.col_type_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_type_text + " " + TEXT + ");";

    public static final String CREATE_TABLE_MYLIST = "CREATE TABLE " + FeedEntry.DATABASE_TABLE_MYLIST + "("
            + FeedEntry.col_mylist_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_mylist_snusid + " " + INTEGER + ", "
            + FeedEntry.col_mylist_myrank + " " + INTEGER + " "
            + "FOREIGN KEY (" + FeedEntry.col_mylist_snusid + ") REFERENCES " + FeedEntry.DATABASE_TABLE_SNUS
            + "(" + FeedEntry.col_snus_id + ")" + ");";

    public static abstract class FeedEntry implements BaseColumns{
        public static final String DATABASE_NAME = "Snusr.db";
        //Table SNUS:
        public static final String DATABASE_TABLE_SNUS = "SNUS";
        public static final String col_snus_id = "_id";
        public static final String col_snus_name = "NAME";
        public static final String col_snus_manufactorer = "MANUFACTORER_ID";
        public static final String col_snus_line = "LINE_ID";
        public static final String col_snus_taste1 = "TASTE1";
        public static final String col_snus_taste2 = "TASTE2";
        public static final String col_snus_taste3 = "TASTE3";
        public static final String col_snus_strength = "STRENGTH";
        public static final String col_snus_nicotinelevel = "NICOTINELEVEL";
        public static final String col_snus_totalrank = "TOTALRANK";
        public static final String col_snus_type = "TYPE";


        //Table MANUFACTORER:
        public static final String DATABASE_TABLE_MANUFACTORER = "MANUFACTORER";
        public static final String col_manufactorer_id = "_id";
        public static final String col_manufactorer_name = "NAME";
        public static final String col_manufactorer_url = "URL";
        public static final String col_manufactorer_country = "COUNTRY";

        //Table LINE:
        public static final String DATABASE_TABLE_LINE = "LINE";
        public static final String col_line_id = "_id";
        public static final String col_line_manufactorer = "MANUFACTORER";
        public static final String col_line_name = "NAME";

        //Table TASTE:
        public static final String DATABASE_TABLE_TASTE = "TASTE";
        public static final String col_taste_id = "_id";
        public static final String col_taste_taste = "TASTE";

        //Table TYPE:
        public static final String DATABASE_TABLE_TYPE = "TYPE";
        public static final String col_type_id = "_id";
        public static final String col_type_text = "TEXT";

        //Table MYLIST:
        public static final String DATABASE_TABLE_MYLIST = "MYLIST";
        public static final String col_mylist_id = "_id";
        public static final String col_mylist_snusid = "SNUS_ID";
        public static final String col_mylist_myrank = "MYRANK";
    }


    public DatabaseHelper(Context context){
        super(context, FeedEntry.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MANUFACTORER);
        db.execSQL(CREATE_TABLE_LINE);
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_TASTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
