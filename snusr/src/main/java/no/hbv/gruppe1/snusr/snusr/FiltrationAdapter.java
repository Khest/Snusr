package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import no.hbv.gruppe1.snusr.snusr.dataclasses.Filtration;

/**
 * Created by Long Huynh on 31.05.2016.
 */
public class FiltrationAdapter extends ArrayAdapter<Filtration> {
    List<Filtration> list;
    LayoutInflater inflater =null;
    public FiltrationAdapter(Context context, List<Filtration> f) {
        super(context, 0, f);
        this.list = f;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }



    public Filtration getItem(int p){
        return list.get(p);
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.spinner_item, null);
        TextView text = (TextView) v.findViewById(R.id.spinner_text);
        Filtration f = list.get(position);
        if(position==0) {
            text.setText("Choose filtration");
        }else{
            text.setText(f.toString());
        }
        return v;

    }
}
