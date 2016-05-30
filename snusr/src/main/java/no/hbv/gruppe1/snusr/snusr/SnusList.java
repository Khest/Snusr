package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;


/**
 * A placeholder fragment containing a simple view.
 */
public class SnusList extends Fragment {

    ListView listview;
    private DatabaseInteractor db;
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
        db = new DatabaseInteractor(this.getActivity());
        //DatabaseHelper h = new DatabaseHelper(getActivity());
        //h.putDummyData();
        Cursor cur = db.fetchSnus(null, null);
        Log.i(Globals.TAG, "Num found: " + String.valueOf(cur.getCount()));
        Log.i(Globals.TAG, "SnusList ver:" + String.valueOf(db.getVersion()));
        SnusAdapter adapter = new SnusAdapter(getActivity(), cur, 0);
        listview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        db.close();
        super.onDestroyView();

    }
}
