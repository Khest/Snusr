package no.hbv.gruppe1.snusr.snusr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Filtration;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Sorting;
import no.hbv.gruppe1.snusr.snusr.dummydata.PutDummyDataExtra;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private final String PREFS_NAME = "PrefsFile";

    boolean searchOpen = false;

    DrawerLayout drawerLayout;

    public void onSearch(List<Filtration> list, Sorting sort) {
        if(list != null || sort != null){
            SnusList snusList = (SnusList) getFragmentManager().findFragmentById(R.id.snuslistFragment);
            if (snusList != null){
                snusList.setUp(list, sort);
            }

        }
    }

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    EditText txtSearch;
    Spinner spinManu,spinLine, spinFiltration, spinSorting;
    SortingAdapter sortingArrayAdapter;
    FiltrationAdapter filtrationAdapter;
    AddSnusSpinnerAdapter lineAdapter;
    ManuAdapter manuAdapter;

    DatabaseInteractor db;
    
    int filtrationID;

    private int selectedManufacturerId, selectedLineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //DatabaseInteractor db2 = new DatabaseInteractor(this);
        //db2.resetDatabase();
        //db2.close();
        //db2.close();
        //db2 = null;
        PutDummyDataExtra putDummyDataExtra = new PutDummyDataExtra(this);

        for(int i= 1; i <= 10; i++){
            String a = String.valueOf(i);
            putDummyDataExtra.putDymmyData(a, "dummy_data/skruf_knox_starkportion_styrke3.png");
        }
        //Log.i(Globals.TAG, "MA ver: "+ db.getReadableDatabase().getVersion());
        if (settings.getBoolean("first_time", true)){
            // Kode som skal kjøres første gang appen tas i bruk.
            // Opprett database
            // Ved testing av appen kan det være greit å slette appen fra mobilen
            // om det er gjort endringer i databasen, og lignende.
           // db.putDummyData();
            // Setter first_time til false, denne koden kjøres aldri igjen.
            settings.edit().putBoolean("first_time", false).apply();
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i(Globals.TAG, "Permission denied");
        }

        //db.close();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "SNUSR";

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

       addSearchFiltrationAdapter();
    }

    private void addSearchFiltrationAdapter() {
        spinFiltration = (Spinner) findViewById(R.id.spin_Filtration);
        spinSorting = (Spinner) findViewById(R.id.spin_Sorting);
        spinManu = (Spinner) findViewById(R.id.spin_serchManu);
        spinLine = (Spinner) findViewById(R.id.spin_searchLine);

        List<Filtration> filtrationsList = Arrays.asList(Filtration.values());
        List<Sorting> sortingsList = Arrays.asList(Sorting.values());

        db  = new DatabaseInteractor(this);
        Cursor mcur = db.fetchManufacturers();

        manuAdapter = new ManuAdapter(this, mcur, 0);
        filtrationAdapter = new FiltrationAdapter(getBaseContext(), filtrationsList);

        sortingArrayAdapter = new SortingAdapter(getBaseContext(), sortingsList);

        Cursor lines = db.fetchLines();
        lineAdapter = new AddSnusSpinnerAdapter(getApplication(), lines, 0);
        spinLine.setAdapter(lineAdapter);

        spinManu.setAdapter(manuAdapter);
        spinFiltration.setAdapter(filtrationAdapter);
        spinSorting.setAdapter(sortingArrayAdapter);

        filtrationAdapter.notifyDataSetChanged();
        sortingArrayAdapter.notifyDataSetChanged();
        spinFiltration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filtrationID = position;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        android.app.Fragment fragment;
        String fragTag = "";
        android.app.FragmentManager fragmentManager = getFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch(position) {
            default:
            case 0:
                fragment = new SnusList();
                fragTag = "SnusList";
                break;
            case 1:
                fragment = new AddSnus();
                fragTag = "AddSnus";
                break;
            case 2:
                fragment = new SendFileFragment();
                fragTag = "SendFile";
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragTag)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void setSearchOpen(boolean condition){
        searchOpen = condition;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        final View layout = findViewById(R.id.searchLayout);
        if (id == R.id.action_search) {
            if(searchOpen == true){
                item.setIcon(R.drawable.search);
                layout.setVisibility(View.GONE);
                searchOpen = false;
            }else{
                item.setIcon(R.drawable.search_lilla);
                drawerLayout.closeDrawers();
               searchWindow();
            }
            return  true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void searchWindow(){
        final View layout = findViewById(R.id.searchLayout);
            layout.setVisibility(View.VISIBLE);
            final Button buttonSearch = (Button) layout.findViewById(R.id.btnSearch);
            final Button buttonSorting = (Button) layout.findViewById(R.id.btnSortingFiltration);
        layout.findViewById(R.id.relativeLayoutSortingFiltrationWindow).setVisibility(View.GONE);
        layout.findViewById(R.id.searchWindow).setVisibility(View.VISIBLE);
        buttonSearch.setTextColor(Color.parseColor("#881e5d"));
        buttonSorting.setTextColor(Color.parseColor("#FFFFFF"));
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.findViewById(R.id.relativeLayoutSortingFiltrationWindow).setVisibility(View.GONE);
                    layout.findViewById(R.id.searchWindow).setVisibility(View.VISIBLE);
                    buttonSearch.setTextColor(Color.parseColor("#881e5d"));
                    buttonSorting.setTextColor(Color.parseColor("#FFFFFF"));
                }
            });
            buttonSorting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.findViewById(R.id.searchWindow).setVisibility(View.GONE);
                    layout.findViewById(R.id.relativeLayoutSortingFiltrationWindow).setVisibility(View.VISIBLE);
                    buttonSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    buttonSorting.setTextColor(Color.parseColor("#881e5d"));
                    onSortClicked();
                }
            });
            searchOpen = true;

    }

    public void onSearchClicked(){
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnusList snusList = (SnusList) getFragmentManager().findFragmentByTag("SnusList");


                EditText etxt = (EditText) findViewById(R.id.eText_search);
                List<Filtration> list = new ArrayList<>();
                Filtration f = Filtration.WILDCARD;
                f.setSearchValue(etxt.getText().toString());
                Filtration f2 = Filtration.MANUFACTURER_NUMBER;
                manuAdapter.getCursor().moveToPosition(spinManu.getSelectedItemPosition());
                selectedManufacturerId = manuAdapter.getCursor().getInt(0);
                f2.setSearchValue(selectedManufacturerId);

                Filtration f3 = Filtration.LINE_NUMBER;
                lineAdapter.getCursor().moveToPosition(spinLine.getSelectedItemPosition());
                selectedLineId = lineAdapter.getCursor().getInt(0);
                f3.setSearchValue(selectedLineId);
                list.add(f);
                list.add(f2);
                list.add(f3);

                if (snusList != null){
                    final View layout = findViewById(R.id.searchLayout);
                    layout.setVisibility(View.GONE);
                    Cursor c = db.fetchSnus(list, null);
                    snusList.search(c);
                    //snusList.search("", "", String.valueOf(etxt.getText()), list);
                }

            }
        });
    }




    public void onSortClicked(){
        Button btnSort = (Button) findViewById(R.id.btn_SortFiltration);
        final Sorting sorting = sortingArrayAdapter.getItem(spinSorting.getSelectedItemPosition());
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rootView) {
                Log.i(Globals.TAG, "Attempting to perform onClick");

                SnusList snusList = (SnusList) getFragmentManager().findFragmentByTag("SnusList");
                if (snusList != null){
                    Log.i(Globals.TAG, "snuslist is not null");
                    snusList.setUp(null, sorting);
                    final View layout = findViewById(R.id.searchLayout);
                    layout.setVisibility(View.GONE);
                } else {
                }
            }
        });

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}
