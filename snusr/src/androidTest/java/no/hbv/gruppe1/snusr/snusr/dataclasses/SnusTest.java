package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Knut Johan Hesten 2016-05-28.
 */
public class SnusTest extends AndroidTestCase {
    public static final String TAG = "SnusrDebug";
    private DatabaseInteractor databaseInteractor;
    private RenamingDelegatingContext context;

    public void setUp() {
        context = new RenamingDelegatingContext(getContext(), "test_");
        databaseInteractor = new DatabaseInteractor(context);
    }

    @SuppressWarnings("Duplicates")
    public void testSnusExists() throws Exception {
        List<Filtration> filtrationList = new ArrayList<>();
        Filtration f1 = Filtration.MANUFACTURER;
        Filtration f2 = Filtration.LINE_NUMBER;
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
        Cursor c = databaseInteractor.fetchSnus(filtrationList, null);
        Log.d(TAG, DatabaseUtils.dumpCursorToString(c));
    }

    public void tearDown() throws Exception {
        databaseInteractor.close();
        super.tearDown();
    }
}
