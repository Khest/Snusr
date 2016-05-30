package no.hbv.gruppe1.snusr.snusr;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Pair;

import java.util.List;

public class DatabaseHelperTest extends AndroidTestCase {
    private DatabaseHelper db;

    public void setUp(){
        RenamingDelegatingContext context
                = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context);
        assertNotNull(db);
    }

    public void testAddEntry(){
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        assertNotNull(sqLiteDatabase);
    }

    public void tearDown() throws Exception{
        db.close();
        super.tearDown();
    }
}