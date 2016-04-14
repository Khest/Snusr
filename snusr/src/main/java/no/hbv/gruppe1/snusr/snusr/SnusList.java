package no.hbv.gruppe1.snusr.snusr;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class SnusList extends Fragment {

    /**
     * Inneholder våre favorittsnus, alle snus og snus med bokmerke
     *
     */
    public SnusList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snuslist, container, false);
    }
}
