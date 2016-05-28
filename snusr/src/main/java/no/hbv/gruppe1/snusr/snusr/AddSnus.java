package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class AddSnus extends Fragment {
    RatingBar rating;
    ImageView snusImage;
    EditText innName, innManu, innLine;
    Button btnAdd, btnAddSnusImage;
    Spinner spinSweet, spinSalt, spinTaste, spinStr;

    int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
    int SELECT_IMAGE = 222;


    Uri fileUri;
    File file;

    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_snus, container, false);
        context = getActivity();
        snusImage = (ImageView) v.findViewById(R.id.addSnus_imgview);
        rating = (RatingBar) v.findViewById(R.id.ratingbar_addsnus);
        innName = (EditText) v.findViewById(R.id.etxt_name);
        innManu = (EditText) v.findViewById(R.id.etxt_manu);
        innLine = (EditText) v.findViewById(R.id.etxt_line);
        btnAdd = (Button) v.findViewById(R.id.btn_addsnus);
        btnAddSnusImage = (Button) v.findViewById(R.id.btn_addSnusImage);
        spinSalt = (Spinner) v.findViewById(R.id.spin_salt);
        spinSweet = (Spinner) v.findViewById(R.id.spin_sweet);
        spinStr = (Spinner) v.findViewById(R.id.spin_str);
        spinTaste = (Spinner) v.findViewById(R.id.spin_taste);

        btnAddSnusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                //openGallery();
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
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); //handle camera and return image
//        CameraHandler cHandler = new CameraHandler();
//        file = cHandler.createImageFile();
//        fileUri = Uri.fromFile(file);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == getActivity().RESULT_OK){
                if(data != null){
                    // Image captured and saved to fileUri specified in the Intent
                    //Bitmap bitmap = CameraHandler.decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    //innName.setText(data.getData().toString());
                    snusImage.setImageBitmap(bitmap);
                }
            }
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
        String mName, mManu, mSweet, mSalt, mTaste, mStr, mRating, mLine;

        mName = innName.getText().toString();
        mManu = innManu.getText().toString();
        mLine = innLine.getText().toString();
        mSalt = spinSalt.getSelectedItem().toString();
        mSweet = spinSweet.getSelectedItem().toString();
        mStr = spinStr.getSelectedItem().toString();
        mTaste = spinTaste.getSelectedItem().toString();


    }



}
