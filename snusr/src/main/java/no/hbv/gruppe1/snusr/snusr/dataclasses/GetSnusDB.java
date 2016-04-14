package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

/**
 * Created by Long Huynh on 14.04.2016.
 */
public class GetSnusDB {
    /** TOdo lag en tabell for filtrering og hent ut ved hjelp for løkke*/
    /**Combine filter and sorting to make a sql query then put data into a cursor */
    public Cursor getData(Context context, Sorting sorte, Filtration filter){
        DatabaseHelper DbHelper = new DatabaseHelper(context);
        String sql = "SELECT * FROM " + DatabaseHelper.FeedEntry.DATABASE_NAME + filter.toString() + sorte.toString();
        SQLiteDatabase db = DbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
}
