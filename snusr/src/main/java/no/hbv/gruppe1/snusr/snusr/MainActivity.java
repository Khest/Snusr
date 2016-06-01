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
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.Toast;
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
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    EditText txtSearch;
    Spinner spinManu,spinLine, spinSorting, spinTaste1;
    SortingAdapter sortingArrayAdapter;
    AddSnusSpinnerAdapter lineAdapter;
    AddSnusSpinnerAdapter manuAdapter;
    MenuItem search;
    AddSnusSpinnerAdapter tasteAdaper;
    DatabaseInteractor db;

    private boolean pokedManufacturingSpinner, pokedLineSpinner, pokedTasteSpinner;

    private int selectedManufacturerId, selectedLineId, selectedTasteID1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        PutDummyDataExtra putDummyDataExtra = new PutDummyDataExtra(this);

        for(int i= 1; i <= 10; i++){
            String a = String.valueOf(i);
            putDummyDataExtra.putDymmyData(a, "dummy_data/skruf_knox_starkportion_styrke3.png");
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getResources().getText(R.string.main_write_external_denied).toString(), Toast.LENGTH_SHORT).show();
            finish();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getResources().getText(R.string.app_name).toString();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        txtSearch = (EditText) findViewById(R.id.eText_search);
       addSearchFiltrationAdapter();
        onSearchClicked();
        onSortClicked();
    }

    private void addSearchFiltrationAdapter() {
        spinSorting = (Spinner) findViewById(R.id.spin_Sorting);
        spinManu = (Spinner) findViewById(R.id.spin_serchManu);
        spinLine = (Spinner) findViewById(R.id.spin_searchLine);
        spinTaste1 = (Spinner) findViewById(R.id.spin_Taste1);


        List<Sorting> sortingsList = Arrays.asList(Sorting.values());

        db  = new DatabaseInteractor(this);
        Cursor mcur = db.fetchManufacturers();
        Cursor lines = db.fetchLines();
        Cursor tcur = db.fetchTastes();


        manuAdapter = new AddSnusSpinnerAdapter(this, mcur, 0);
        lineAdapter = new AddSnusSpinnerAdapter(this, lines, 0);
        tasteAdaper = new AddSnusSpinnerAdapter(this, tcur, 0);
        sortingArrayAdapter = new SortingAdapter(getBaseContext(), sortingsList);


        spinTaste1.setAdapter(tasteAdaper);
        spinLine.setAdapter(lineAdapter);
        spinManu.setAdapter(manuAdapter);
        spinSorting.setAdapter(sortingArrayAdapter);

        spinManu.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                pokedManufacturingSpinner = true;
                return false;
            }
        });
        spinLine.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                pokedLineSpinner = true;
                return false;
            }
        });
        spinTaste1.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                pokedTasteSpinner = true;
                return false;
            }
        });

        tasteAdaper.notifyDataSetChanged();
        sortingArrayAdapter.notifyDataSetChanged();
        clearAll();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        db.close();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            case 3:
                fragment = new AboutUs();
                fragTag = "AboutUs";
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragTag)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
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
        search = item;
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
        onNavigationDrawerItemSelected(0);
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
                    onSearchClicked();
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

                List<Filtration> list = new ArrayList<>();

                if (txtSearch.getText().length() > 0) {
                    Filtration f1 = Filtration.WILDCARD;
                    f1.setSearchValue(txtSearch.getText().toString());
                    list.add(f1);
                }

                if (pokedManufacturingSpinner) {
                    Filtration f2 = Filtration.MANUFACTURER_NUMBER;
                    manuAdapter.getCursor().moveToPosition(spinManu.getSelectedItemPosition());
                    selectedManufacturerId = manuAdapter.getCursor().getInt(0);
                    f2.setSearchValue(selectedManufacturerId);
                    list.add(f2);
                }

                if (pokedLineSpinner) {
                    Filtration f3 = Filtration.LINE_NUMBER;
                    lineAdapter.getCursor().moveToPosition(spinLine.getSelectedItemPosition());
                    selectedLineId = lineAdapter.getCursor().getInt(0);
                    f3.setSearchValue(selectedLineId);
                    list.add(f3);
                }

                if (pokedTasteSpinner) {
                    Filtration f4 = Filtration.TASTE_NUMBER;
                    tasteAdaper.getCursor().moveToPosition(spinTaste1.getSelectedItemPosition());
                    selectedTasteID1 = tasteAdaper.getCursor().getInt(0);
                    f4.setSearchValue(selectedTasteID1);
                    list.add(f4);
                }

                if (snusList != null){
                    final View layout = findViewById(R.id.searchLayout);
                    layout.setVisibility(View.GONE);
                    if (search != null) {
                        search.setIcon(R.drawable.search);
                    }
                    Cursor c;
                    if (list.size() == 0) {
                        c = db.fetchSnus(null, Sorting.ALPHABETICAL);
                    } else {
                        c = db.fetchSnus(list, Sorting.ALPHABETICAL);
                    }
                    snusList.search(c);
                    clearAll();
                }

            }
        });
    }

    public void clearAll(){
        pokedManufacturingSpinner = false;
        pokedLineSpinner = false;
        pokedTasteSpinner = false;
        txtSearch.setText("");
        spinManu.setSelection(-1);
        spinLine.setSelection(-1);
        spinTaste1.setSelection(-1);
        spinSorting.setSelection(0);
    }



    public void onSortClicked(){
        onNavigationDrawerItemSelected(0);
        Button btnSort = (Button) findViewById(R.id.btn_SortFiltration);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View rootView) {
                Log.i(Globals.TAG, "Attempting to perform onClick");
                final Sorting sorting = sortingArrayAdapter.getItem(spinSorting.getSelectedItemPosition());
                SnusList snusList = (SnusList) getFragmentManager().findFragmentByTag("SnusList");
                if (snusList != null){
                    snusList.setUp(null, sorting);
                    final View layout = findViewById(R.id.searchLayout);
                    layout.setVisibility(View.GONE);
                    search.setIcon(R.drawable.search);
                    clearAll();
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
