package no.hbv.gruppe1.snusr.snusr;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;
import no.hbv.gruppe1.snusr.snusr.DatabaseHelper;
import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dakh on 2016-05-30.
 */
public class SendFileFragmentTest extends AndroidTestCase {
    public static final String TAG = "SnusrDebug";
    private DatabaseInteractor databaseInteractor;
    private RenamingDelegatingContext context;

    public void setUp() {
        context = new RenamingDelegatingContext(getContext(), "test_");
        databaseInteractor = new DatabaseInteractor(context);
    }


    @SuppressWarnings("Duplicates")
    public void testConversion() throws Exception {
        try {
            StringBuilder sb = new StringBuilder();
            String delimiter = "|";
            byte[] delimiter2 = "|".getBytes();

            byte[] bytes = new byte[1024];

            Cursor c = databaseInteractor.fetchSnus(null, null);
    //
    //                        for (int i = 0; i < bytes.length; i++) {
    //                            byte[] id = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_id)).getBytes();
    //                            byte[] name = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_name)).getBytes();
    //                            byte[] manu = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_manufactorer)).getBytes();
    //                            byte[] line = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_line)).getBytes();
    //                            byte[] t1 = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_taste1)).getBytes();
    //                            byte[] t2 = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_taste2)).getBytes();
    //                            byte[] t3 = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_taste3)).getBytes();
    //                            byte[] str = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_strength)).getBytes();
    //                            byte[] nic = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_nicotinelevel)).getBytes();
    //                            byte[] rank = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_totalrank)).getBytes();
    //                            byte[] type = c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_type)).getBytes();
    //                            byte[] img = c.getBlob(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_img));
    //
    //                        }
            assertNotNull(c);
           // while (c.moveToNext()) {
            c.moveToFirst();
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_id))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_name))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_manufactorer))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_line))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_taste1))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_taste2))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_taste3))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_strength))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_nicotinelevel))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_totalrank))).append(delimiter);
                sb.append(c.getString(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_type))).append(delimiter);
                byte[] b = c.getBlob(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_img));
                //String s = new String(b, "ISO-8859-1");
            if (b != null) {
                sb.append(new String(b, "ISO-8859-1"));
            }
                //sb.append(c.getBlob(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_img))).append(delimiter);

            byte[] bytes1 = sb.toString().getBytes();

            byte[] bytes2 = bytes1;
            String ss = new String(bytes2, "ISO-8859-1");
            Log.i(Globals.TAG, ss);

                //bluetoothHandler.write(sb.toString().getBytes());


           // }
        } catch (UnsupportedEncodingException uex) {
            Log.e(Globals.TAG, "Fatal error when converting byte to string " + uex.getMessage());
        }
    }

    public void tearDown() throws Exception {
        databaseInteractor.close();
        super.tearDown();
    }
}
