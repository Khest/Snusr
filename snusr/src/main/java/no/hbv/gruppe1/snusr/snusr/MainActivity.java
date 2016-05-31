package no.hbv.gruppe1.snusr.snusr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        DatabaseInteractor db2 = new DatabaseInteractor(this);
        db2.resetDatabase();
        db2.close();
        //db2.close();
        //db2 = null;
        PutDummyDataExtra putDummyDataExtra = new PutDummyDataExtra(this);
        putDummyDataExtra.putDymmyData("1", "dummy_data/skruf_knox_starkportion_styrke3.png");

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        android.app.Fragment fragment;
        android.app.FragmentManager fragmentManager = getFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch(position) {
            default:
            case 0:
                fragment = new SnusList();
                break;
            case 1:
                fragment = new AddSnus();
                break;
            case 2:
                fragment = new SendFileFragment();
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
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
        RelativeLayout r =(RelativeLayout) findViewById(R.id.searchLayout);
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_search) {
            if(searchOpen == true){
                item.setIcon(R.drawable.search);
                r.setVisibility(View.GONE);
                searchOpen = false;
            }else{
                drawerLayout.closeDrawers();
                item.setIcon(R.drawable.search_lilla);
                r.setVisibility(View.VISIBLE);
                searchOpen = true;
            }
            return  true;

        }

        return super.onOptionsItemSelected(item);
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
            Button btnSearch = (Button) rootView.findViewById(R.id.btn_search);
            EditText txtSearch = (EditText) rootView.findViewById(R.id.eText_search);
            Spinner spinManu = (Spinner) rootView.findViewById(R.id.spin_serchManu);
            Spinner spinLine = (Spinner) rootView.findViewById(R.id.spin_searchLine);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View rootView) {
                    Log.i(Globals.TAG, "Attempting to perform onClick");
                    SnusList snusList = (SnusList) getActivity().getFragmentManager().findFragmentById(R.id.snuslistFragment);
                    if (snusList != null){
                        snusList.setUp(null, Sorting.TYPE);
                    } else {
                        Log.i(Globals.TAG, " snuslist i onclick er null");
                    }
                }
            });
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
