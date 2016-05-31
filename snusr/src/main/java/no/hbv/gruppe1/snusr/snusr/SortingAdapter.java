package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import no.hbv.gruppe1.snusr.snusr.dataclasses.Sorting;

/**
 * Created by Long Huynh on 31.05.2016.
 */
public class SortingAdapter extends ArrayAdapter{
    List<Sorting> list;
    LayoutInflater inflater =null;
    public SortingAdapter(Context context, List<Sorting> f) {
        super(context, 0, f);
        this.list = f;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }


    public Sorting getItem(int p){
        return list.get(p);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        View v = inflater.inflate(R.layout.spinner_item, null);
        TextView text = (TextView) v.findViewById(R.id.spinner_text);
        if(position==0) {
            text.setText("Choose filtration");
        }else{
            text.setText(list.get(position).toString());
        }
        return v;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.spinner_item, null);
        TextView text = (TextView) v.findViewById(R.id.spinner_text);

        if(position==0) {
            text.setText("Choose sorting");
        }else{
            text.setText(list.get(position).toString());
        }
        return v;

    }
}
