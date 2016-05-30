package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Created by Knut Johan Hesten on 2016-04-14.
 * Functions as intermediate between database and view layer. Handles text and number formatting.
 */
public final class Snus {
    private static String name;
    private static double strength, nicotineLevel, totalRank;
    private static int    manufacturer, line, taste1, taste2, taste3, type;
    private static byte[] image;

    /**
     * Inserts the currently loaded snus into the database. Requires all parameters to have been set
     * @param context       Application context
     * @return              Returns whether the insertion was successful or not
     * @throws SQLException Throws SQLExceptions on database error
     */
    public static boolean insertSnusIntoDatabase(Context context) throws SQLException {
        SQLiteDatabase s = getWriteableDatabase(context);
        long result = s.insert(
                DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
                null,
                getContentValues());
        return (result >0 );
    }

    /**
     * Checks whether the snus exists in the database already. Requires that name, manufacturer and line to already have been set.
     * @param context   Application context
     * @return          Boolean indicating whether the snus exists or not
     */
    public static boolean snusExists(Context context) {
        List<Filtration> filtrationList = new ArrayList<>();
        Filtration f1 = Filtration.MANUFACTURER;
        Filtration f2 = Filtration.LINE_NUMBER;
        Filtration f3 = Filtration.NAME;
        f1.setSearchValue(getManufacturer());
        f2.setSearchValue(getLine());
        f3.setSearchValue(getName());
        filtrationList.add(f1);
        filtrationList.add(f2);
        filtrationList.add(f3);
        DatabaseInteractor db = new DatabaseInteractor(context);
        Cursor c = db.fetchSnus(filtrationList, null);
        int count = c.getCount();
        db.close();
        return (count > 0);
    }

    public static void setName(String name) {
        Snus.name = name.trim();
    }

    public static void setStrength(Double strength) {
        Snus.strength = strength;
    }

    public static void setNicotineLevel(Double nicotineLevel) {
        Snus.nicotineLevel = nicotineLevel;
    }

    public static void setTotalRank(Double totalRank) {
        Snus.totalRank = totalRank;
    }

    public static void setManufacturer(int manufacturer) {
        Snus.manufacturer = manufacturer;
    }

    public static void setLine(int line) {
        Snus.line = line;
    }

    public static void setTaste1(int taste1) {
        Snus.taste1 = taste1;
    }

    public static void setTaste2(int taste2) {
        Snus.taste2 = taste2;
    }

    public static void setTaste3(int taste3) {
        Snus.taste3 = taste3;
    }

    public static void setType(int type) {
        Snus.type = type;
    }

    public static void setImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Globals.COMPRESS_FORMAT, Globals.IMAGE_QUALITY, byteArrayOutputStream);
        Snus.image = byteArrayOutputStream.toByteArray();
    }

    public static String getName() {
        return name;
    }

    public static double getStrength() {
        return strength;
    }

    public static double getNicotineLevel() {
        return nicotineLevel;
    }

    public static double getTotalRank() {
        return totalRank;
    }

    public static int getManufacturer() {
        return manufacturer;
    }

    public static int getLine() {
        return line;
    }

    public static int getTaste1() {
        return taste1;
    }

    public static int getTaste2() {
        return taste2;
    }

    public static int getTaste3() {
        return taste3;
    }

    public static int getType() {
        return type;
    }

    public static byte[] getImage() {
        return image;
    }

    /**
     * Setting up parameters for a snus
     * @param name          Name of the Snus
     * @param manufacturer  The manufacturer's ID
     * @param line          The line's ID
     * @param taste1        The first taste ID
     * @param taste2        The second taste ID
     * @param taste3        The third taste ID
     * @param type          The type's ID
     * @param strength      The strength of the snus
     * @param nicotineLevel The nicotine level of the snus, in percent
     * @param totalRank     The total rank of the snus
     * @param image         The image for the snus
     * @throws Exception    Throws context-aware exception on invalid input
     */
    public static void setSnus(String name, int manufacturer, int line, int taste1, int taste2, int taste3, int type,
                               Double strength, Double nicotineLevel, Double totalRank, Bitmap image) throws Exception {
        Snus.name = "";
        Snus.strength = Snus.nicotineLevel = Snus.totalRank = 0.0;
        Snus.manufacturer = Snus.line = Snus.taste1 = Snus.taste2 = Snus.taste3 = Snus.type = 0;
        setName(name);
        setStrength(strength);
        setNicotineLevel(nicotineLevel);
        setTotalRank(totalRank);
        setManufacturer(manufacturer);
        setLine(line);
        setTaste1(taste1);
        setTaste2(taste2);
        setTaste3(taste3);
        setType(type);
        setImage(image);
    }

    private static SQLiteDatabase getWriteableDatabase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.getWritableDatabase();
    }

    private static ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.FeedEntry.col_snus_name, getName());
        cv.put(DatabaseHelper.FeedEntry.col_snus_manufactorer, getManufacturer());
        cv.put(DatabaseHelper.FeedEntry.col_snus_line, getLine());
        cv.put(DatabaseHelper.FeedEntry.col_snus_taste1, getTaste1());
        cv.put(DatabaseHelper.FeedEntry.col_snus_taste2, getTaste2());
        cv.put(DatabaseHelper.FeedEntry.col_snus_taste3, getTaste3());
        cv.put(DatabaseHelper.FeedEntry.col_snus_strength, getStrength());
        cv.put(DatabaseHelper.FeedEntry.col_snus_nicotinelevel, getNicotineLevel());
        cv.put(DatabaseHelper.FeedEntry.col_snus_totalrank, getTotalRank());
        cv.put(DatabaseHelper.FeedEntry.col_snus_type, getType());
        cv.put(DatabaseHelper.FeedEntry.col_snus_img, getImage());
        return cv;
    }

    private Snus() {}

}
