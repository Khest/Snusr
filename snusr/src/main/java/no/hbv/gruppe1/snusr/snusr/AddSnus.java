package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import no.hbv.gruppe1.snusr.snusr.dataclasses.Snus;


public class AddSnus extends Fragment {
    RatingBar rating;
    ImageView snusImage;
    EditText innName, innManu, innLine;
    Button btnAdd, btnAddSnusImage;
    Spinner spinTaste1, spinTaste2, spinTaste3, spinStr, spinType, spinTakePic, spinNico;

    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
    final int SELECT_IMAGE = 222;
    Uri fileUri;


    boolean cameraOpen = false;
    Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_snus, container, false);
        context = getActivity();
        snusImage = (ImageView) v.findViewById(R.id.addSnus_imgview);
        rating = (RatingBar) v.findViewById(R.id.ratingbar_addsnus);
        innName = (EditText) v.findViewById(R.id.etxt_name);
        innManu = (EditText) v.findViewById(R.id.etxt_manu);
        innLine = (EditText) v.findViewById(R.id.etxt_line);
        btnAdd = (Button) v.findViewById(R.id.btn_addsnus);
        btnAddSnusImage = (Button) v.findViewById(R.id.btn_addSnusImage);
        spinStr = (Spinner) v.findViewById(R.id.spin_str);
        spinNico = (Spinner) v.findViewById(R.id.spin_nico);
        spinTaste2 = (Spinner) v.findViewById(R.id.spin_taste2);
        spinTaste1 = (Spinner) v.findViewById(R.id.spin_taste1);
        spinTaste3 = (Spinner) v.findViewById(R.id.spin_taste3);
        spinType = (Spinner) v.findViewById(R.id.spin_str);
        spinTakePic = (Spinner) v.findViewById(R.id.spin_takepic);
        Bitmap tempImage = BitmapFactory.decodeResource(getResources(), R.drawable.noimagefound);
        snusImage.setImageBitmap(tempImage);


        btnAddSnusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraOpen = true;
                spinTakePic.performClick();
            }
        });

        spinTakePic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 && cameraOpen == true){
                    cameraOpen = false;
                    takePicture();
                }
                if(position == 1){
                    cameraOpen = false;
                    openGallery();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }


    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    public void takePicture() {
        Log.i("SnusrDebug", " TRYING STUFF");
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); //handle camera and return image
        fileUri = CameraHandler.getOutputMediaFileUri();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("SnusrDebug", " TRYING STUFF");
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            snusImage.setImageBitmap(bitmap);


        }
        if(requestCode == SELECT_IMAGE){
            if(resultCode == getActivity().RESULT_OK){
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        snusImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void addSnus(){
        String mName;
        int mManu, mTaste1, mTaste2, mTaste3, mLine, mType;
        Bitmap mSnusImage;
        Double mRank, mNicotin, mStr;

        mName = innName.getText().toString();
        mManu = Integer.parseInt(innManu.getText().toString());
        mLine = Integer.parseInt(innLine.getText().toString());
        mTaste1 = Integer.parseInt(spinTaste1.getSelectedItem().toString());
        mTaste2 = Integer.parseInt(spinTaste2.getSelectedItem().toString());
        mStr = Double.parseDouble(String.valueOf(spinStr.getSelectedItem()));
        mTaste3 = Integer.parseInt(spinTaste3.getSelectedItem().toString());
        mType = Integer.parseInt(spinType.getSelectedItem().toString());
        mRank = Double.parseDouble(String.valueOf(rating.getNumStars()));
        mNicotin = Double.parseDouble(String.valueOf(spinNico.getSelectedItem()));
        snusImage.buildDrawingCache();
        mSnusImage = snusImage.getDrawingCache();
        try {
            Snus.setSnus(mName, 0, mLine, mTaste1, mTaste2, mTaste3, mType, mStr, mNicotin, mRank, mSnusImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
