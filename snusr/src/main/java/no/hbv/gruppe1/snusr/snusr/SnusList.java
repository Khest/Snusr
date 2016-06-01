package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Filtration;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Sorting;


/**
 * A placeholder fragment containing a simple view.
 */
public class SnusList extends Fragment {



    ListView listview;
    Button btnAllesnus, btnBook;
    private DatabaseInteractor db;
    private Cursor cur;

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
        btnAllesnus = (Button) view.findViewById(R.id.btn_allsnus);
        btnBook = (Button) view.findViewById(R.id.btn_book);
        db = new DatabaseInteractor(this.getActivity());
        cur = db.fetchSnus(null, null);
        SnusAdapter adapter = new SnusAdapter(getActivity(), cur, 0);
        btnAllesnus.setTextColor(Color.parseColor("#881e5d"));
        btnAllesnus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAllesnus.setTextColor(Color.parseColor("#881e5d"));
                btnBook.setTextColor(Color.parseColor("#FFFFFF"));

                cur = db.fetchSnus(null, null);
                listview.setAdapter(new SnusAdapter(getActivity(), cur, 0));
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBook.setTextColor(Color.parseColor("#881e5d"));
                btnAllesnus.setTextColor(Color.parseColor("#FFFFFF"));


                cur = db.fetchMyList(1);
                listview.setAdapter(new SnusAdapter(getActivity(), cur, 0));
            }
        });

       setUp(null, null);
        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtSnusId = (TextView) view.findViewById(R.id.txtSnusId);
                String t = txtSnusId.getText().toString();
                // Intent intent = new Intent(getActivity(), SnusInformationFragment.class);
                // intent.putExtra("snusGetId", t);
                Log.d("teste", t);

                newFragmentSnusInformationFragment(t);

            }
        });
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
    public void newFragmentSnusInformationFragment(String snusId){
        SnusInformationFragment nextFrag= new SnusInformationFragment();
        nextFrag.setSnusID(Integer.parseInt(snusId));
        this.getFragmentManager().beginTransaction()
                .replace(R.id.container, nextFrag)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onDestroy() {
        cur.close();
        db.close();
        Log.i(Globals.TAG, "Closing database in " + this.getClass().getName());
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    public void search(Cursor result){
       // filtration.WILDCARD.setSearchValue();
        //DatabaseHelper h = new DatabaseHelper(getActivity());
        //h.putDummyData();
        //Cursor cur = db.fetchCostume(manu, line, snusname);
        SnusAdapter adapter = new SnusAdapter(getActivity(), result, 0);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setUp(List<Filtration> list, Sorting sort){
        db = new DatabaseInteractor(this.getActivity());
        //DatabaseHelper h = new DatabaseHelper(getActivity());
        //h.putDummyData();
        Cursor cur = db.fetchSnus(list, sort);
        SnusAdapter adapter = new SnusAdapter(getActivity(), cur, 0);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.close();
    }
}
