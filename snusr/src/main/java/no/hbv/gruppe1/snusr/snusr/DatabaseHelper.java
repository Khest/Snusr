package no.hbv.gruppe1.snusr.snusr;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Håkon Stensheim 10.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 3;
    public static final String INTEGER = "INTEGER";
    public static final String DOUBLE = "DOUBLE";
    public static final String TEXT = "TEXT";

    public static final String DROP_TABLE_SNUS = "DROP TABLE IF EXISTS " + FeedEntry.DATABASE_TABLE_SNUS + ";";
    public static final String CREATE_TABLE_SNUS =
            "CREATE TABLE " + FeedEntry.DATABASE_TABLE_SNUS + "("
            + FeedEntry.col_snus_id + " INTEGER PRIMARY KEY, "
            + FeedEntry.col_snus_name + " " + TEXT + ", "
            + FeedEntry.col_snus_manufactorer + " " + INTEGER + ", "
            + FeedEntry.col_snus_line + " " + INTEGER + ", "
            + FeedEntry.col_snus_taste1 + " " + INTEGER + ", "
            + FeedEntry.col_snus_taste2 + " " + INTEGER + ", "
            + FeedEntry.col_snus_taste3 + " " + INTEGER + ", "
            + FeedEntry.col_snus_strength + " " + DOUBLE+ ", "
            + FeedEntry.col_snus_nicotinelevel + " " + DOUBLE + ", "
            + FeedEntry.col_snus_totalrank + " " + DOUBLE + ", "
            + FeedEntry.col_snus_type + " " + INTEGER + ", "
            + FeedEntry.col_snus_img + " BLOB, "
            + "FOREIGN KEY (" + FeedEntry.col_snus_manufactorer + ") REFERENCES "
            + FeedEntry.DATABASE_TABLE_MANUFACTURER + "(" + FeedEntry.col_manufacturer_id + "), "
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

    public static final String DROP_TABLE_LINE = "DROP TABLE IF EXISTS " + FeedEntry.DATABASE_TABLE_LINE + ";";
    public static final String CREATE_TABLE_LINE =
            "CREATE TABLE " + FeedEntry.DATABASE_TABLE_LINE + "("
            + FeedEntry.col_line_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_line_manufactorer + " " + INTEGER + ", "
            + FeedEntry.col_line_name + " " + TEXT + ", "
            + "FOREIGN KEY (" + FeedEntry.col_line_manufactorer + ") "
            + "REFERENCES " + FeedEntry.DATABASE_TABLE_MANUFACTURER + "("
            + FeedEntry.col_manufacturer_id + ")" + ");";

    public static final String DROP_TABLE_MANUFACTURER = "DROP TABLE IF EXISTS " + FeedEntry.DATABASE_TABLE_MANUFACTURER + ";";
    public static final String CREATE_TABLE_MANUFACTURER =
            "CREATE TABLE " + FeedEntry.DATABASE_TABLE_MANUFACTURER
            + "(" + FeedEntry.col_manufacturer_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_manufacturer_name + " " + TEXT + ", "
            + FeedEntry.col_manufacturer_country + " " + TEXT + ", "
            + FeedEntry.col_manufacturer_url + " " + TEXT+ ");";

    public static final String DROP_TABLE_TASTE = "DROP TABLE IF EXISTS " + FeedEntry.DATABASE_TABLE_TASTE + ";";
    public static final String CREATE_TABLE_TASTE =
            "CREATE TABLE " + FeedEntry.DATABASE_TABLE_TASTE + "("
            + FeedEntry.col_taste_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_taste_taste + " " + TEXT + ");";

    public static final String DROP_TABLE_TYPE = "DROP TABLE IF EXISTS " + FeedEntry.DATABASE_TABLE_TYPE + ";";
    public static final String CREATE_TABLE_TYPE =
            "CREATE TABLE " + FeedEntry.DATABASE_TABLE_TYPE + "("
            + FeedEntry.col_type_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_type_text + " " + TEXT + ");";

    // TODO: Add constraint for bookmark. Int 0/1 (bool)
    public static final String DROP_TABLE_MYLIST = "DROP TABLE IF EXISTS " + FeedEntry.DATABASE_TABLE_MYLIST + ";";
    public static final String CREATE_TABLE_MYLIST =
            "CREATE TABLE " + FeedEntry.DATABASE_TABLE_MYLIST + "("
            + FeedEntry.col_mylist_id + " " + INTEGER + " PRIMARY KEY, "
            + FeedEntry.col_mylist_snusid + " " + INTEGER + ", "
            + FeedEntry.col_mylist_myrank + " " + INTEGER + ", "
            + FeedEntry.col_mylist_bookmark + " " + INTEGER + ", "
            + "FOREIGN KEY (" + FeedEntry.col_mylist_snusid + ") REFERENCES " + FeedEntry.DATABASE_TABLE_SNUS
            + "(" + FeedEntry.col_snus_id + ")" + ");";

    public static abstract class FeedEntry implements BaseColumns{
        public static final String DATABASE_NAME = "Snusr.db";
        //Table SNUS:
        public static final String DATABASE_TABLE_SNUS = "SNUS";
        public static final String col_snus_id = "SNUS_id";
        public static final String col_snus_name = "SNUS_NAME";
        public static final String col_snus_manufactorer = "SNUS_MANUFACTORER_ID";
        public static final String col_snus_line = "SNUS_LINE_ID";
        public static final String col_snus_taste1 = "TASTE1";
        public static final String col_snus_taste2 = "TASTE2";
        public static final String col_snus_taste3 = "TASTE3";
        public static final String col_snus_strength = "STRENGTH";
        public static final String col_snus_nicotinelevel = "NICOTINELEVEL";
        public static final String col_snus_totalrank = "TOTALRANK";
        public static final String col_snus_type = "SNUS_TYPE";
        public static final String col_snus_img = "IMG";


        //Table MANUFACTURER:
        public static final String DATABASE_TABLE_MANUFACTURER = "MANUFACTURER";
        public static final String col_manufacturer_id = "MANUFACTURER_id";
        public static final String col_manufacturer_name = "MANUFACTURER_NAME";
        public static final String col_manufacturer_url = "URL";
        public static final String col_manufacturer_country = "COUNTRY";

        //Table LINE:
        public static final String DATABASE_TABLE_LINE = "LINE";
        public static final String col_line_id = "LINE_id";
        public static final String col_line_manufactorer = "LINE_MANUFACTURER";
        public static final String col_line_name = "LINE_NAME";

        //Table TASTE:
        public static final String DATABASE_TABLE_TASTE = "TASTE";
        public static final String col_taste_id = "TASTE_id";
        public static final String col_taste_taste = "TASTE_TASTE";

        //Table TYPE:
        public static final String DATABASE_TABLE_TYPE = "TYPE";
        public static final String col_type_id = "TYPE_id";
        public static final String col_type_text = "TYPE_TEXT";

        //Table MYLIST:
        public static final String DATABASE_TABLE_MYLIST = "MYLIST";
        public static final String col_mylist_id = "MYLIST_id";
        public static final String col_mylist_snusid = "MYLIST_SNUS_ID";
        public static final String col_mylist_myrank = "MYRANK";
        public static final String col_mylist_bookmark = "BOOKMARK";
    }


    public DatabaseHelper(Context context){
        super(context, FeedEntry.DATABASE_NAME, null, DATABASE_VERSION);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_MYLIST);
        db.execSQL(DROP_TABLE_SNUS);
        db.execSQL(DROP_TABLE_TASTE);
        db.execSQL(DROP_TABLE_TYPE);
        db.execSQL(DROP_TABLE_LINE);
        db.execSQL(DROP_TABLE_MANUFACTURER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MANUFACTURER);
        db.execSQL(CREATE_TABLE_LINE);
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_TASTE);
        db.execSQL(CREATE_TABLE_SNUS);
        db.execSQL(CREATE_TABLE_MYLIST);
    }

    public boolean putDummyData(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            putManufacturer(db, "Swedish Match", "www.swedishmatch.com", "Sweden");
            putManufacturer(db, "Skruf", "www.skruf.se", "Sweden");
            putManufacturer(db, "British American Tobacco", "www.bat.com", "England");

            putLine(db, 1, "General");
            putLine(db, 1, "General G3");
            putLine(db, 1, "Ettan");
            putLine(db, 1, "Catch");
            putLine(db, 2, "Skruf");
            putLine(db, 2, "KNOX");
            putLine(db, 2, "Smålands");
            putLine(db, 3, "Lucky Strike");

            putType(db, "Loose");
            putType(db, "Portion");
            putType(db, "White Loose");
            putType(db, "White Portion");
            putType(db, "White Tobacco Portion");

            putTaste(db, "");
            putTaste(db, "Licorice");
            putTaste(db, "Coffee");
            putTaste(db, "Blueberry");
            putTaste(db, "Mint");
            putTaste(db, "Tobacco");
            putTaste(db, "Lemon");
            putTaste(db, "Orange");
            putTaste(db, "Apple");
            putTaste(db, "Vanilla");

            putSnus(db, "Extra Strong", 1, 2, 3, 0, 0, 5, 1.8, 0, 4, null);
            putSnus(db, "Extra Weak",   2, 5, 2, 0, 0, 3, 0.6, 3, 3, null);
            db.close();
        } catch (Exception ex){return false;}
        return true;
    }

    public void putManufacturer(SQLiteDatabase db, String name, String url, String country){
        ContentValues input = new ContentValues();
        input.put(FeedEntry.col_manufacturer_name, name);
        input.put(FeedEntry.col_manufacturer_url, url);
        input.put(FeedEntry.col_manufacturer_country, country);
        db.insert(FeedEntry.DATABASE_TABLE_MANUFACTURER, null, input);
    }

    public void putLine(SQLiteDatabase db, int manufactorer, String name){
        ContentValues input = new ContentValues();
        input.put(FeedEntry.col_line_manufactorer, manufactorer);
        input.put(FeedEntry.col_line_name, name);
        db.insert(FeedEntry.DATABASE_TABLE_LINE, null, input);
    }

    public void putType(SQLiteDatabase db, String text){
        ContentValues input = new ContentValues();
        input.put(FeedEntry.col_type_text, text);
        db.insert(FeedEntry.DATABASE_TABLE_TYPE, null, input);
    }

    public void putTaste(SQLiteDatabase db, String taste){
        ContentValues input = new ContentValues();
        input.put(FeedEntry.col_taste_taste, taste);
        db.insert(FeedEntry.DATABASE_TABLE_TASTE, null, input);
    }

    public void putSnus(SQLiteDatabase db, String name, int manufactorer, int line, int taste1, int taste2, int taste3,
                        double strength, double nicotinelevel, double rank, int type, byte[] img){
        ContentValues input = new ContentValues();
        input.put(FeedEntry.col_snus_name, name);
        input.put(FeedEntry.col_snus_manufactorer, manufactorer);
        input.put(FeedEntry.col_snus_line, line);
        input.put(FeedEntry.col_snus_taste1, taste1);
        input.put(FeedEntry.col_snus_taste2, taste2);
        input.put(FeedEntry.col_snus_taste3, taste3);
        input.put(FeedEntry.col_snus_strength, strength);
        input.put(FeedEntry.col_snus_nicotinelevel, nicotinelevel);
        input.put(FeedEntry.col_snus_totalrank, rank);
        input.put(FeedEntry.col_snus_type, type);
        input.put(FeedEntry.col_snus_img, img);
        db.insert(FeedEntry.DATABASE_TABLE_SNUS, null, input);
    }

    public void putMyList(SQLiteDatabase db, int snus, int rank, int bookmark){
        ContentValues input = new ContentValues();
        input.put(FeedEntry.col_mylist_snusid, snus);
        input.put(FeedEntry.col_mylist_myrank, rank);
        input.put(FeedEntry.col_mylist_bookmark, bookmark);
        db.insert(FeedEntry.DATABASE_TABLE_MYLIST, null, input);
    }
}
