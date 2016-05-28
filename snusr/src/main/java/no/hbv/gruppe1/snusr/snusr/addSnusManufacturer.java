package no.hbv.gruppe1.snusr.snusr;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import no.hbv.gruppe1.snusr.snusr.R;
import no.hbv.gruppe1.snusr.snusr.dataclasses.GetSnusDB;


public class addSnusManufacturer extends Fragment {


    public addSnusManufacturer(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_snus_manufacturer, container, false);

        ImageView img = (ImageView) view.findViewById(R.id.manufacturer_img);
        ListView listView = (ListView) view.findViewById(R.id.manufacturer_list);
        GetSnusDB db = new GetSnusDB();
        Cursor cur = db
        img.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.maxresdefault));
        listView.setAdapter(new ManufacturerAdapter(this, cur, 0));
        return view;
    }


}
