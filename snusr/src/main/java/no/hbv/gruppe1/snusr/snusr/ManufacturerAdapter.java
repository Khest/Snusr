package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ManufacturerAdapter extends CursorAdapter {

    LayoutInflater inflater;
    public ManufacturerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtManufacturer = (TextView) view.findViewById(R.id.manufacturer_list_txt);
        String inputText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_manufacturer_name));
        int inputID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_manufacturer_id));
        txtManufacturer.setTag(inputID, "id");
        txtManufacturer.setText(inputText);
        txtManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for proceeding to fragment for adding snus.
                
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.from(context).inflate(R.layout.manufacturer_list_item, parent, false);
    }
}
