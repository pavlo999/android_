package com.example.sim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCategory extends AppCompatActivity {

    private TextView captureText;
    private String path;
    private Uri uri;
    private ImageView captureImage;

    private final int CROP_PIC_REQUEST_CODE =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        captureText = findViewById(R.id.textClick);
        captureImage = findViewById(R.id.my_avatar);
        captureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, CROP_PIC_REQUEST_CODE);

            }
        });
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Match the request 'pic id with requestCode
//        if (requestCode == 1) {
//            // BitMap is data structure of image file which store the image in memory
//            Uri selectedImageUri = data.getData();
//            //            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            // Set the image in imageview for display
//            captureImage.setImageURI(selectedImageUri);
//        }
//    }

//    private void doCrop(Uri picUri) {
//        try {
//
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//
//            cropIntent.setDataAndType(picUri, "image/*");
//            cropIntent.putExtra("crop", "true");
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            cropIntent.putExtra("outputX", 128);
//            cropIntent.putExtra("outputY", 128);
//            cropIntent.putExtra("return-data", true);
//            startActivityForResult(cropIntent, CROP_PIC_REQUEST_CODE);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (ActivityNotFoundException anfe) {
//            // display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_PIC_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(selectedImageUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);

                Bundle extras = cropIntent.getExtras();
                Bitmap bitmap= extras.getParcelable("data");
                captureImage.setImageBitmap(bitmap);

        }

    }

}