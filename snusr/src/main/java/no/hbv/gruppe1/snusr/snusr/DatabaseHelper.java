package no.hbv.gruppe1.snusr.snusr;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;

/**
 * Created by Håkon Stensheim 10.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 14;
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
        public static final String DATABASE_NAME = "snusr.db";
        //Table SNUS:
        public static final String DATABASE_TABLE_SNUS = "SNUS";
        public static final String col_snus_id = "_id";
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
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MANUFACTURER);
        db.execSQL(CREATE_TABLE_LINE);
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_TASTE);
        db.execSQL(CREATE_TABLE_SNUS);
        db.execSQL(CREATE_TABLE_MYLIST);
        putDummyData(db);
    }

    public boolean putDummyData(SQLiteDatabase db){
        Log.i(Globals.TAG, " Putting dummy data");
        try {
            putManufacturer(db, "Swedish Match", "www.swedishmatch.com", "Sweden");
            putManufacturer(db, "Skruf", "www.skruf.se", "Sweden");
            putManufacturer(db, "British American Tobacco", "www.bat.com", "England");
            putManufacturer(db, "Ettan", "www.ettan.com", "Sweden");


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

//           1  name
//           2  manufactorer
//           3  line
//           4  taste1
//           5  taste2
//           6  taste3
//           7  strength
//           8  nicotinelevel
//           9  totalrank
//           10 type
//           11 img

            //Swedish Match    1        2  3  4  5  6  7   8   9  10  11
            putSnus(db, "Extra Strong", 1, 2, 2, 7, 5, 4, 1.4, 1, 4, null);
            putSnus(db, "Strong",       1, 2, 2, 8, 6, 3, 1.3, 3, 2, null);
            putSnus(db, "White",        1, 1, 3, 9, 7, 1, 0.1, 1, 4, null);
            putSnus(db, "Slim White",   1, 2, 4, 2, 8, 3, 1.3, 5, 2, null);
            putSnus(db, "Slim",         1, 2, 5, 3, 9, 2, 0.2, 0, 3, null);
            putSnus(db, "Normal",       1, 1, 6, 4, 2, 2, 0.2, 3, 1, null);

            //Skruf            1        2  3  4  5  6  7   8   9  10  11
            putSnus(db, "Extra Strong", 2, 2, 2, 2, 2, 4, 1.4, 3, 1, null);
            putSnus(db, "Strong",       2, 2, 5, 4, 8, 3, 1.3, 3, 2, null);
            putSnus(db, "White",        2, 2, 6, 3, 7, 1, 0.1, 0, 4, null);
            putSnus(db, "Slim White",   2, 2, 7, 2, 8, 3, 1.3, 1, 3, null);
            putSnus(db, "Slim",         2, 2, 8, 6, 9, 2, 0.2, 4, 5, null);
            putSnus(db, "Normal",       2, 2, 9, 7, 2, 2, 0.2, 3, 5, null);

            //Knox            1        2  3  4  5  6  7   8   9  10  11
            putSnus(db, "Extra Strong", 3, 6, 9, 2, 5, 4, 1.4, 5, 1, null);
            putSnus(db, "Strong",       3, 6, 5, 8, 5, 3, 1.3, 5, 2, null);
            putSnus(db, "White",        3, 6, 3, 9, 5, 4, 1.4, 2, 4, null);
            putSnus(db, "Slim White",   3, 6, 2, 8, 5, 1, 0.1, 1, 3, null);
            putSnus(db, "Slim",         3, 6, 2, 5, 9, 4, 1.4, 0, 2, null);
            putSnus(db, "Normal",       3, 6, 5, 7, 2, 4, 1.4, 0, 2, null);

            //Ettan            1        2  3  4  5  6  7   8   9  10  11
            putSnus(db, "Extra Strong", 4, 3, 7, 9, 2, 4, 1.4, 0, 2, null);
            putSnus(db, "Strong",       4, 3, 8, 8, 3, 3, 1.3, 2, 1, null);
            putSnus(db, "White",        4, 3, 9, 8, 4, 2, 0.2, 3, 4, null);
            putSnus(db, "Slim White",   4, 3, 2, 7, 6, 2, 0.2, 4, 4, null);
            putSnus(db, "Slim",         4, 3, 3, 6, 5, 2, 0.2, 4, 2, null);
            putSnus(db, "Normal",       4, 3, 4, 5, 2, 2, 0.2, 0, 5, null);


        } catch (Exception ex){
            Log.e(Globals.TAG, " Fatal error inserting dummy data " + ex.getMessage());
        }
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
