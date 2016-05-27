package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import no.hbv.gruppe1.snusr.snusr.dataclasses.GetSnusDB;


/**
 * A placeholder fragment containing a simple view.
 */
public class SnusList extends Fragment {

    ListView listview;
    /**
     * Inneholder våre favorittsnus, alle snus og snus med bokmerke
     */
    public SnusList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_snuslist, container, false);

        listview = (ListView) view.findViewById(R.id.snuslist);
        GetSnusDB db = new GetSnusDB();
        Cursor cur = db.fetchSnus(getActivity(), null, null);
        //DatabaseHelper helper = new DatabaseHelper(getActivity());
        //SQLiteDatabase db = helper.getWritableDatabase();
        //Cursor cur = db.rawQuery("SELECT * FROM SNUS", null);

//        while(cur.moveToNext()){
//            Toast.makeText(getActivity(), cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_name)), Toast.LENGTH_LONG).show();
//        }

        SnusAdapter adapter = new SnusAdapter(getActivity(), cur, 0);
        listview.setAdapter(adapter);

        return view;
    }
}
