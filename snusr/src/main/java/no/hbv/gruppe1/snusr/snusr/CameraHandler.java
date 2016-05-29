package no.hbv.gruppe1.snusr.snusr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lassetangeras on 26.05.2016.
 */
public class CameraHandler {

    private static final String REQUEST_CODE = "to";
static final int REQUEST_TAKE_PHOTO = 1;


    String mCurrentPhotoPath; //Create imageFile


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_image_crop);
//
//
//        dispatchTakePictureIntent();
//
//    }

    /**
     *This create a photoFile and start camera intent
     */
//    public void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile));
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

    public String getFileUrl(){
        return mCurrentPhotoPath;
    }

    /**
     * This create tempfile with name and directory.
     * Name is set to data and time starting with the name SnusR_
     * File is set to be .jpg format
     * Environment.DIRECTORY_PICTURES set location for the image on device
     */
    public static File createImageFile(){
        //Check if your application folder exists in the external storage, if not create it:
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Snuser");

        if (! storageDir.exists()){
            if (storageDir.mkdirs()) {
                Log.d("SnusR", "created directory");
            }else{
                Log.d("SnusR", "created directory");
                }
        }


        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SnusR_";
        String fileNameExtension = ".jpg";

        File mediaFile = new File(storageDir.getPath() + File.separator + imageFileName + timeStamp + fileNameExtension);
        // Save a file: path for use with ACTION_VIEW intents

        return mediaFile;
    }

    static Uri getOutputMediaFileUri(){
        return Uri.fromFile(createImageFile());
    }



    /**
     * Starts Add picture to gallery and set picture size after a test on receiving from camera intent
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            galleryAddPic();
//            setPic();
//        }
//    }

//    public void compressImage(){
//        try {
//            Intent intent = new Intent(this, Image_compress.class);
//            intent.putExtra("Image", mCurrentPhotoPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * combined file and url and send image to a device location folder "picture" (DIRECTORY_PICTURES - in createImageFile())
     */
    public void galleryAddPic(Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, REQUEST_CODE));

        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * if statment set width and hight if it´s 0. If not it will crash
     * The image bitmap is decoded and size is set
     * Then image is displayed in imageView
     */
    public void setPic(ImageView imageFromCamera) {
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

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}