package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import no.hbv.gruppe1.snusr.snusr.dataclasses.DatabaseInteractor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Snus;


public class AddSnus extends Fragment {
    RatingBar ratingSnusRating;
    ImageView snusImage;
    EditText eTxtName, etxtNicotine, eTxtStrength;
    Button btnAdd, btnAddSnusImage;
    Spinner spinTaste1, spinTaste2, spinTaste3, spinType, spinTakePic, spinnerManufacturer, spinnerLine;

    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
    final int SELECT_IMAGE = 222;
    Uri fileUri;

    private DatabaseInteractor interactor;
    private AddSnusSpinnerAdapter manufacturerAdapter;
    private AddSnusSpinnerAdapter lineAdapter;
    private AddSnusSpinnerAdapter tasteAdapter1, tasteAdapter2, tasteAdapter3;
    private AddSnusSpinnerAdapter typeAdapter;

    boolean cameraOpen = false;
    Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_snus, container, false);
        interactor = new DatabaseInteractor(getActivity());
        context = getActivity();
        snusImage = (ImageView) v.findViewById(R.id.addSnus_imgview);
        ratingSnusRating = (RatingBar) v.findViewById(R.id.ratingbar_addsnus);
        eTxtName = (EditText) v.findViewById(R.id.etxt_name);
        spinnerManufacturer = (Spinner) v.findViewById(R.id.spinner_add_snus_manufacturer);
        spinnerLine = (Spinner) v.findViewById(R.id.spinner_addsnus_line);
        btnAdd = (Button) v.findViewById(R.id.btn_addsnus);
        btnAddSnusImage = (Button) v.findViewById(R.id.btn_addSnusImage);
        eTxtStrength = (EditText) v.findViewById(R.id.etxt_strength);
        etxtNicotine = (EditText) v.findViewById(R.id.etxt_nicotine_level);
        spinTaste1 = (Spinner) v.findViewById(R.id.spin_taste1);
        spinTaste2 = (Spinner) v.findViewById(R.id.spin_taste2);
        spinTaste3 = (Spinner) v.findViewById(R.id.spin_taste3);
        spinType = (Spinner) v.findViewById(R.id.spin_type);
        spinTakePic = (Spinner) v.findViewById(R.id.spin_takepic);
        Bitmap tempImage = BitmapFactory.decodeResource(getResources(), R.drawable.noimagefound);
        snusImage.setImageBitmap(tempImage);

        Cursor c = interactor.fetchManufacturers();
        Cursor tasteCursor = interactor.fetchTastes();
        Cursor typeCursor = interactor.fetchTypes();

        tasteAdapter1 = new AddSnusSpinnerAdapter(getActivity(), tasteCursor, 0);
        tasteAdapter2 = new AddSnusSpinnerAdapter(getActivity(), tasteCursor, 0);
        tasteAdapter3 = new AddSnusSpinnerAdapter(getActivity(), tasteCursor, 0);

        spinTaste1.setAdapter(tasteAdapter1);
        spinTaste2.setAdapter(tasteAdapter2);
        spinTaste3.setAdapter(tasteAdapter3);

        typeAdapter = new AddSnusSpinnerAdapter(getActivity(), typeCursor, 0);
        spinType.setAdapter(typeAdapter);

        manufacturerAdapter = new AddSnusSpinnerAdapter(getActivity(), c, 0);
        spinnerManufacturer.setAdapter(manufacturerAdapter);

        spinnerManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if (position != -1) {
                     manufacturerAdapter.getCursor().moveToPosition(position);
                     int manufacturer = manufacturerAdapter.getCursor()
                             .getInt(0);
                     Cursor c2 = interactor.fetchLines(manufacturer);
                     lineAdapter = new AddSnusSpinnerAdapter(getActivity(), c2, 0);
                     spinnerLine.setAdapter(lineAdapter);
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

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
                if(position == 0){
                    cameraOpen = false;
                    takePicture();
                } else {
                    cameraOpen = false;
                    openGallery();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSnus();
            }
        });

        return v;
    }


    public void clearAll(){
        eTxtName.setText("");
        snusImage.setImageDrawable(getResources().getDrawable(R.drawable.noimagefound));
    }

    @Override
    public void onDestroyView() {
        interactor.close();
        super.onDestroyView();
    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    public void takePicture() {
        Log.i(Globals.TAG, " Attempting to invoke image capture");
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); //handle camera and return image
        fileUri = CameraHandler.getOutputMediaFileUri();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Globals.TAG, "Evaluating returned image");
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
        int mTaste1, mTaste2, mTaste3, mType, mManu, mLine;
        Bitmap mSnusImage;
        Double mRank, mNicotin, mStr;

        mName = eTxtName.getText().toString();
        manufacturerAdapter.getCursor().moveToPosition(spinnerManufacturer.getSelectedItemPosition());
        mManu = manufacturerAdapter.getCursor().getInt(0);
        lineAdapter.getCursor().moveToPosition(spinnerLine.getSelectedItemPosition());
        mLine = lineAdapter.getCursor().getInt(0);
        tasteAdapter1.getCursor().moveToPosition(spinTaste1.getSelectedItemPosition());
        mTaste1 = tasteAdapter1.getCursor().getInt(0);
        tasteAdapter2.getCursor().moveToPosition(spinTaste2.getSelectedItemPosition());
        mTaste2 = tasteAdapter2.getCursor().getInt(0);
        tasteAdapter3.getCursor().moveToPosition(spinTaste3.getSelectedItemPosition());
        mTaste3 = tasteAdapter3.getCursor().getInt(0);
        typeAdapter.getCursor().moveToPosition(spinType.getSelectedItemPosition());
        mType = typeAdapter.getCursor().getInt(0);
        mRank = Double.parseDouble(String.valueOf(ratingSnusRating.getRating()));
        if (eTxtStrength.getText().toString().length() > 0) {
            mStr = Double.parseDouble(eTxtStrength.getText().toString());
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.add_snus_error_strength).toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (etxtNicotine.getText().toString().length() > 0) {
            mNicotin = Double.parseDouble(etxtNicotine.getText().toString());
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.add_snus_error_nicotine).toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        snusImage.buildDrawingCache();
        mSnusImage = snusImage.getDrawingCache();
        Log.i(Globals.TAG, mManu + " " + mLine + " " + mName + " " + mTaste1 + " " + mTaste2 + " " + mTaste3 + " " + mStr + " " + mType + " " + mRank + " " + mNicotin);
        try {
            Snus.setSnus(mName, mManu, mLine, mTaste1, mTaste2, mTaste3, mType, mStr, mNicotin, mRank, mSnusImage);
            if (Snus.snusExists(getActivity())) {
                Toast.makeText(getActivity(), getResources().getText(R.string.add_snus_snus_exists).toString(), Toast.LENGTH_SHORT).show();
            } else if (Snus.insertSnusIntoDatabase(getActivity())) {
                Toast.makeText(getActivity(),
                        getResources().getText(R.string.add_snus_successful).toString(),
                        Toast.LENGTH_SHORT).show();
                clearAll();
            } else {
                Toast.makeText(getActivity(),
                        getResources().getText(R.string.add_snus_failed).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
