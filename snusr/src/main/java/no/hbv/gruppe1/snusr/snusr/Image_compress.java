package no.hbv.gruppe1.snusr.snusr;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Image_compress extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = { //TODO Spør om tilatelse for å nå bilder på device
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    ImageView imageView2;
    ImageView imageView1;
    Bitmap dstBmp;
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_crop);


        imageView2 = (ImageView) findViewById(R.id.imageTest);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2.setBackgroundColor(Color.parseColor("#000000"));
        imageView1.setBackgroundColor(Color.parseColor("#000000"));

        TextView text = (TextView) findViewById(R.id.edit);
        TextView text2 = (TextView) findViewById(R.id.edit2);


        //imageView2.setImageResource(R.drawable.general_los_styrke3);
        //  Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        //    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);


                //    ImageView previewThumbnail = new ImageView(this);

        // Bitmap  bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        //Bitmap bm = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"), 0, getIntent().getByteArrayExtra("byteArray").length);

        Display display = getWindowManager().getDefaultDisplay(); //Setter ImageView størrelse
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.x;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        imageView2.setLayoutParams(layoutParams);
         //imageView1.setLayoutParams(layoutParams);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.general_los_styrke3);
        width = bm.getWidth();
        height =bm.getHeight();


        if(width >= height){
            width = height;
        }else{
            height =width;
        }


        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 100, 0, width- 100, height-100);

        imageView2.setImageBitmap(resizedBitmap);





//
//        Display display = getWindowManager().getDefaultDisplay(); //Setter ImageView størrelse
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x - 100;
//        int height = size.y / 4;


        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        //imageView2.setLayoutParams(layoutParams);
       // imageView1.setLayoutParams(layoutParams);

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.general_los_styrke3);
//        int høyde = bm.getHeight();
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.9f, 0.9f);
//        bm = Bitmap.createBitmap(bm, 150, 150, width, height, matrix, false);
//        imageView2.setImageBitmap(bm);
//
//        bm.compress(Bitmap.CompressFormat.JPEG, 20, stream); //Funker best å konvertere til JPEG.
//        byte[] byteArray = stream.toByteArray();
//        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//        if (compressedBitmap.getWidth() >= compressedBitmap.getHeight()){
//
//            dstBmp = Bitmap.createBitmap(
//                    compressedBitmap,
//                    compressedBitmap.getWidth()/2 - compressedBitmap.getHeight()/2,
//                    0,
//                    compressedBitmap.getHeight(),
//                    compressedBitmap.getHeight()
//            );
//            imageView1.setImageBitmap(dstBmp);
//
//        }else {
//
//            dstBmp = Bitmap.createBitmap(
//                    compressedBitmap,
//                    0,
//                    compressedBitmap.getHeight() / 2 - compressedBitmap.getWidth() / 2,
//                    compressedBitmap.getWidth(),
//                    compressedBitmap.getWidth()
//            );
//           // imageView1.setImageBitmap(compressedBitmap);
//
//
//
//        }

//
//        text.setText(String.valueOf("Ekte bildet KB: " + bm.getByteCount() / 1024)); // Returns the minimum number of bytes that can be used to store this bitmap's pixels.
//        text2.setText(String.valueOf("Komprimert bildet KB: " + stream.size() / 1024));


    }
}





        
         /* int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE); //TODO Spør om tilatelse for å nå bilder på device
        if (permission != PackageManager.PERMISSION_GRANTED) {//TODO Spør om tilatelse for å nå bilder på device
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }*/


        //dispatchTakePictureIntent();

//    }
//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
//
//            }
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
////            Bundle extras = data.getExtras();
////            Bitmap imageBitmap = (Bitmap) extras.get("data");
////            imageView1.setImageBitmap(imageBitmap);
//            galleryAddPic();
//            setPic();
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = imageView1.getWidth();
//        int targetH = imageView1.getHeight();
//
//        if (targetW == 0 || targetH == 0){ // settes pga imageview returnerte at den var nullpointer
//            targetH = targetW = 694;
//        }
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//
////        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
////        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.general_los_styrke3); //TODO disse linjene er ikke helt ferdig - compress
////        int høyde = bitmap.getHeight();
////        Matrix matrix = new Matrix();
////        matrix.postScale(0.9f, 0.9f);
////        bitmap = Bitmap.createBitmap(bitmap, 150, 150, targetW, targetH,matrix, false);
////        imageView2.setImageBitmap(bitmap);
//
////        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream); //Funker best å konvertere til JPEG.
////        byte[] byteArray = stream.toByteArray();
////        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
////        imageView1.setImageBitmap(compressedBitmap);
//
//        imageView1.setImageBitmap(bitmap);
//
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }





    //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    //startActivityForResult(intent, 0);

    //dispatchTakePictureIntent();





