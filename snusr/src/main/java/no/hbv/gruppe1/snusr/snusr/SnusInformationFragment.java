package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;


/**
 * TODO: Implement a way to pass a bundle from SnusList when selecting a list item, passing it to SnusInformationFragment
 */
public class SnusInformationFragment extends Fragment {

    private ImageView imgSnus;
    private TextView txtName, txtManufacturer, txtTaste, txtStrength;
    private RatingBar ratingInfo;
    private int snusID;



    public SnusInformationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_snus_information, container, false);
//        if (getArguments() != null) {
//            snusID = savedInstanceState.getInt(DatabaseHelper.FeedEntry.col_snus_id);
//        } else {
//            snusID = 2;
//        }
        txtName = (TextView) view.findViewById(R.id.txtSnusNameInformation);
        imgSnus = (ImageView) view.findViewById(R.id.imgSnusInformation);
        txtManufacturer = (TextView) view.findViewById(R.id.txtManufacturer);
        txtTaste = (TextView) view.findViewById(R.id.txtTaste);
        txtStrength = (TextView) view.findViewById(R.id.txtStrength);
        ratingInfo = (RatingBar) view.findViewById(R.id.ratingSnusInformation);

        DatabaseInteractor db = new DatabaseInteractor(getActivity());
        Cursor cur = db.fetchSpecificSnus(snusID);
        cur.moveToFirst();
        txtName.setText(cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_line_name)) + " "
                + cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_name)));
        txtManufacturer.setText(cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_manufacturer_name)));
        txtStrength.setText(cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_strength)));
        //Log.i("treee", cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.DATABASE_TABLE_TASTE + "." + DatabaseHelper.FeedEntry.col_taste_taste)));
        String taste1 = cur.getString(cur.getColumnIndexOrThrow(DatabaseInteractor.TASTE_COLUMN_TEXT_ALIAS_1 ));
        String taste2 = cur.getString(cur.getColumnIndexOrThrow(DatabaseInteractor.TASTE_COLUMN_TEXT_ALIAS_2 ));
        String taste3 = cur.getString(cur.getColumnIndexOrThrow(DatabaseInteractor.TASTE_COLUMN_TEXT_ALIAS_3 ));

        //Log.i("treee", cur.getString(cur.getColumnIndexOrThrow(DatabaseInteractor.TASTE_TABLE_ALIAS_1 + "." + DatabaseHelper.FeedEntry.col_taste_taste)));


        String test = cur.getString(cur.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_name));
        Log.i("treee", DatabaseUtils.dumpCursorToString(cur));
        txtTaste.setText(makeTasteString(taste1, taste2, taste3));

        byte[] array = cur.getBlob(cur.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.col_snus_img));
        if (array != null) {
            ImageHandler imgHandler = new ImageHandler();
            imgSnus.setImageBitmap(imgHandler.convertByteToBitmap(array));
        } else {
            imgSnus.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.noimagefound));
        }
        cur.close();
        return view;
    }

    public void setSnusID(int snusID){
        this.snusID = snusID;
    }

    public String makeTasteString(String taste1, String taste2, String taste3){
        String output = "";

        if(taste1 != null) {
            if (!taste1.equals(""))
                output = output + taste1;
        }
        if(taste2 != null) {
            if (!taste2.equals(""))
                output = output + ", " + taste2;
        }
        if(taste3 != null) {
            if (!taste3.equals(""))
                output = output + ", " + taste3;
        }
        return output;
    }



}
