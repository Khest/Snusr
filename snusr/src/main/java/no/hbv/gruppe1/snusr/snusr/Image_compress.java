package no.hbv.gruppe1.snusr.snusr;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Image_compress extends AppCompatActivity {
    Bitmap bitmap = null;
    ImageView imageView2;
    ImageView imageView1;
    Intent takePictureIntent;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    //static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath; //Create imageFile

    private static final int REQUEST_EXTERNAL_STORAGE = 1;//TODO Spør om tilatelse for å nå bilder på device
    private static String[] PERMISSIONS_STORAGE = { //TODO Spør om tilatelse for å nå bilder på device
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_crop);

       /* int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE); //TODO Spør om tilatelse for å nå bilder på device
        if (permission != PackageManager.PERMISSION_GRANTED) {//TODO Spør om tilatelse for å nå bilder på device
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }*/


        imageView2 = (ImageView) findViewById(R.id.imageTest);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2.setBackgroundColor(Color.parseColor("#000000"));
        imageView1.setBackgroundColor(Color.parseColor("#000000"));

        TextView text = (TextView) findViewById(R.id.edit);
        TextView text2 = (TextView) findViewById(R.id.edit2);

        dispatchTakePictureIntent();

    }
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                galleryAddPic();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView1.setImageBitmap(imageBitmap);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





    //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    //startActivityForResult(intent, 0);

    //dispatchTakePictureIntent();

//
//        imageView2.setImageResource(R.drawable.general_los_styrke3);
//
//        Display display = getWindowManager().getDefaultDisplay(); //Setter ImageView størrelse
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x - 100;
//        int height = size.y / 4;
//
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
//        imageView2.setLayoutParams(layoutParams);
//        imageView1.setLayoutParams(layoutParams);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.general_los_styrke3);
//        int høyde = bm.getHeight();
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.9f, 0.9f);
//        bm = Bitmap.createBitmap(bm, 150, 150, høyde, height,matrix, false);
//        imageView2.setImageBitmap(bm);
//
//        bm.compress(Bitmap.CompressFormat.JPEG, 20, stream); //Funker best å konvertere til JPEG.
//        byte[] byteArray = stream.toByteArray();
//        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//        imageView1.setImageBitmap(compressedBitmap);
//
//
//
//        int getByteCount = bm.getByteCount();
//
//        text.setText(String.valueOf("Ekte bildet KB: " + bm.getByteCount() / 1024)); // Returns the minimum number of bytes that can be used to store this bitmap's pixels.
//        text2.setText(String.valueOf("Komprimert bildet KB: " +stream.size() / 1024));


}