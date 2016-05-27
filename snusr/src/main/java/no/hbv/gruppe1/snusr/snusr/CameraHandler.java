package no.hbv.gruppe1.snusr.snusr;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lassetangeras on 26.05.2016.
 */
public class CameraHandler extends AppCompatActivity {

    private static final String REQUEST_CODE = "to";
static final int REQUEST_TAKE_PHOTO = 1;
static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};// Spørr om tilatelse for å nå bilder på device

    String mCurrentPhotoPath; //Create imageFile
    ImageView imageFromCamera;
    Bitmap imageSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_crop);

        imageFromCamera = (ImageView) findViewById(R.id.imageView1);
        //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, 0);

        dispatchTakePictureIntent();

    }

    /**
     *This create a photoFile and start camera intent
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //handle camera and return image
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("photoFile", "PhotoFile can´t be made");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                       Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    /**
     * This create tempfile with name and directory.
     * Name is set to data and time starting with the name SnusR_
     * File is set to be .jpg format
     * Environment.DIRECTORY_PICTURES set location for the image on device
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SnusR_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Starts Add picture to gallery and set picture size after a test on receiving from camera intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
            setPic();
        }
    }

    public void compressImage(){
        try {
            Intent intent = new Intent(this, Image_compress.class);
            intent.putExtra("Image", mCurrentPhotoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * combined file and url and send image to a device location folder "picture" (DIRECTORY_PICTURES - in createImageFile())
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, REQUEST_CODE));

        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * if statment set width and hight if it´s 0. If not it will crash
     * The image bitmap is decoded and size is set
     * Then image is displayed in imageView
     */
    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageFromCamera.getWidth();
        int targetH = imageFromCamera.getHeight();

        if (targetW == 0 || targetH == 0){ // settes pga imageview returnerte at den var nullpointer
            targetH = targetW = 694;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        imageFromCamera.setImageBitmap(bitmap);

    }
}