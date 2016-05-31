package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;

/**
 * Created by Long Huynh on 01.06.2016.
 */
public class TasteAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private DatabaseInteractor db;
    Cursor cursor;
    Context context;

    public TasteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.cursor = c;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.from(context).inflate(R.layout.spinner_item, parent, false);
    }

    public int getID(){
        return cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_taste_taste));
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

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
        if(position==0) {
            text.setText("Choose taste");
        }else{
            text.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_taste_taste)));
        }
        return v;

    }
}
