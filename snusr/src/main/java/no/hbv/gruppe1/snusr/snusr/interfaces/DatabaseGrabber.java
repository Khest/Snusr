package no.hbv.gruppe1.snusr.snusr.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Filtration;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Sorting;

import java.util.List;

/**
 * Created by Dakh on 2016-05-26.
 */
public interface DatabaseGrabber {

    Cursor fetchSnus(Context context, List<Filtration> filtrationList, Sorting sorting);

    Cursor fetchMyList(Context context);

    String snusDetailSqlJoinString();
}
