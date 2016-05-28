package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Knut Johan Hesten 2016-05-28.
 */
public class SnusTest extends AndroidTestCase {
    public static final String TAG = "SnusrDebug";
    private DatabaseInteractor databaseInteractor;
    private RenamingDelegatingContext context;
    private DatabaseHelper db;

    public void setUp() {
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context);
        db.putDummyData();
        databaseInteractor = new DatabaseInteractor();
    }

    @SuppressWarnings("Duplicates")
    public void testSnusExists() throws Exception {
        List<Filtration> filtrationList = new ArrayList<>();
        Filtration f1 = Filtration.MANUFACTURER;
        Filtration f2 = Filtration.LINE;
        Filtration f3 = Filtration.NAME;
        String manufacturer = "swedish match";
        String line = "G3";
        String name = "extra strong";
        f1.setSearchValue(manufacturer);
        f2.setSearchValue(line);
        f3.setSearchValue(name);
        filtrationList.add(f1);
        filtrationList.add(f2);
        filtrationList.add(f3);
        Cursor c = databaseInteractor.fetchSnus(context, filtrationList, null);
        Log.d(TAG, DatabaseUtils.dumpCursorToString(c));
    }

    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
