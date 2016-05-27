package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dakh on 2016-05-26.
 */
public class GetSnusDBTest extends AndroidTestCase {
    private GetSnusDB getSnusDB;
    private RenamingDelegatingContext context;
    private DatabaseHelper db;

    public void setUp() {
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context);
        db.putDummyData();
        getSnusDB = new GetSnusDB();
    }

    public void testFetchSnus() {
        Cursor c = getSnusDB.fetchSnus(context, null, null);
        assertNotNull(c);
        if (c.getCount() <= 0 ) throw new RuntimeException("Empty cursor in test 1");
        c.moveToFirst();
        if (c.getString(1).equals("")) throw new RuntimeException("First string in test 1 is empty");
        System.out.println(c.getString(1));
    }

    public void testFetchSnusWithFiltration() throws Exception {
        List<Filtration> filtrations = new  ArrayList<>();
        filtrations.add(Filtration.NAME);
        filtrations.get(0).defineVariables("General", null, null);
        Cursor c = getSnusDB.fetchSnus(context, filtrations, null);
        assertNotNull(c);
    }

    public void testFetchSnusWithOrder() throws Exception {
        Sorting s = Sorting.STRENGTH;
        s.setOrder(Sorting.Order.ASC);
        Cursor c = getSnusDB.fetchSnus(context, null, s);
        assertNotNull(c);
        if (c.getCount() <= 0) throw new RuntimeException("test 3 empty cursor");
    }

    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
