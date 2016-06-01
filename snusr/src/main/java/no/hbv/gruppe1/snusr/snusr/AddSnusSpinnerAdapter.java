package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Dakh on 2016-05-31.
 */
public class AddSnusSpinnerAdapter extends CursorAdapter {

    LayoutInflater inflater;
    public AddSnusSpinnerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtGeneric = (TextView) view.findViewById(R.id.manufacturer_list_txt);
        String inputText = cursor.getString(1);
        txtGeneric.setText(inputText);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.manufacturer_list_item, parent, false);
    }
}
