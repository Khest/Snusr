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
 * Created by Dakh on 2016-05-26.
 */
public class GetSnusDBTest extends AndroidTestCase {
    public static final String TAG = "SnusrDebug";
    private GetSnusDB getSnusDB;
    private RenamingDelegatingContext context;
    private DatabaseHelper db;

    public void setUp() {
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context);
        db.putDummyData();
        getSnusDB = new GetSnusDB();
    }

    public void _testFetchSnus() {
        Cursor c = getSnusDB.fetchSnus(context, null, null);
        assertNotNull(c);
        if (c.getCount() <= 0 ) throw new RuntimeException("Empty cursor in test 1");
        c.moveToFirst();
        if (c.getString(1).equals("")) throw new RuntimeException("First string in test 1 is empty");
        System.out.println(c.getString(1));
    }

    public void _testFetchSnusWithFiltration() throws Exception {
        List<Filtration> filtrations = new  ArrayList<>();
        filtrations.add(Filtration.LINE);
        filtrations.get(0).setSearchValue("General");
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        if (c.getCount() <= 0) throw new RuntimeException("empty cursor");
        c.moveToFirst();
    }

    public void _testFetchSnusWithOrder() throws Exception {
        Sorting s = Sorting.STRENGTH;
        s.setOrder(Sorting.Order.ASC);
        Cursor c = getSnusDB.fetchSnus(context, null, s);
        assertNotNull(c);
        if (c.getCount() <= 0) throw new RuntimeException("test 3 empty cursor");
    }

    public void _testFetchSnusWithFiltrationAndOrder() throws Exception {
        Sorting s = Sorting.STRENGTH;
        s.setOrder(Sorting.Order.ASC);
        List<Filtration> filtrations = new ArrayList<>();
        filtrations.add(Filtration.MANUFACTURER);
        filtrations.get(0).setSearchValue("swedish");
        Cursor c = getSnusDB.fetchSnus(context, filtrations, s);
        assertNotNull(c);
        if (c.getCount() <= 0) throw new RuntimeException("empty cursor");
    }

    public void testNameFiltration() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.NAME;
        String testValue = "strong";
        f.setSearchValue(testValue);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        if (!c.getString(1).toLowerCase().contains(testValue))
            throw new RuntimeException("result does not contain '" + testValue + "'");
    }
    public void testLineFiltration() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.LINE;
        String searchValue = "skruf";
        f.setSearchValue(searchValue);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        if (!c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_line_name))
                .toLowerCase().contains(searchValue)) throw new RuntimeException("result does not contain " + searchValue);
    }

    public void testManufacturerFiltration() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.MANUFACTURER;
        String searchValue = "swedish match";
        f.setSearchValue(searchValue);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        if (!c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_manufacturer_name))
                .toLowerCase().contains(searchValue)) throw new RuntimeException("result does not contain " + searchValue);
    }

    public void testPopularityFiltration() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.POPULARITY;
        double val1 = 0.0;
        double val2 = 2.0;
        f.setSearchValue(val1, val2);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        Log.d(TAG, DatabaseUtils.dumpCursorToString(c));
    }

    public void testStrengthFiltration() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.STRENGTH;
        double val1 = 0.0;
        double val2 = 5.0;
        f.setSearchValue(val1, val2);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(2, c.getCount());
    }

    public void testTasteNumber() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.TASTE_NUMBER;
        int val1 = 2;
        f.setSearchValue(val1);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        Log.d(TAG, DatabaseUtils.dumpCursorToString(c));
    }

    public void testTasteText() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.TASTE_TEXT;
        String val1 = "Licorice";
        f.setSearchValue(val1);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
//        if (!c.getString(c.getColumnIndex("_TASTE1")).toLowerCase().contains(val1))
//            throw new RuntimeException("result does not contain " + val1);
        Log.d(TAG, DatabaseUtils.dumpCursorToString(c));
    }

    public void testNictotine() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.NICOTINE;
        double val1 = 0;
        double val2 = 1;
        f.setSearchValue(val1, val2);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
    }

    public void testTypeNumber() throws Exception {
        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.TYPE_NUMBER;
        double val1 = 3;
        f.setSearchValue(val1);
        filtrations.add(f);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        Log.d(TAG, DatabaseUtils.dumpCursorToString(c));
    }

    public void testUpgrade() throws  Exception {
        db.onUpgrade(db.getWritableDatabase(), 1, 2);
    }

    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
