package com.example.amd.tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity {
    String email, password;
    EditText e5_name;
    CircleImageView circleImageView;

    public static int RESULT_SELECT_IMG;
    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e5_name = (EditText) findViewById(R.id.editText5);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageVie);
        Intent myintent = getIntent();
        {
            if (myintent != null) {
                email = myintent.getStringExtra("Email");
                password = myintent.getStringExtra("Password");
            }

        }


    }


    public void generatecode(View v) {
        Date mydate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss", Locale.getDefault());
        String date = format1.format(mydate);
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);


        if (resultUri !=null)
        {
            Intent myintent = new Intent(NameActivity.this, InviteCodeActivity.class);
            myintent.putExtra("name", e5_name.getText().toString());
            myintent.putExtra("email", email);
            myintent.putExtra("password", password);
            myintent.putExtra("date", date);
            myintent.putExtra("IsShareing", "true");
            myintent.putExtra("code", code);
            myintent.putExtra("imageUri", resultUri);



            startActivity(myintent);
            finish();

        }
        else
            {

            Toast.makeText(getApplicationContext(), "Please Choose an Image", Toast.LENGTH_SHORT).show();
        }


    }

    public void selectimage(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_SELECT_IMG);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_SELECT_IMG);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("APP_DEBUG", String.valueOf(requestCode));

        try {
            // When an Image is picked
            if (requestCode == RESULT_SELECT_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();

                CropImage.activity(selectedImage)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(800, 800)
                        .setMaxCropResultSize(1000, 1000)
                        .start(this);
            }
            // when image is cropped
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("APP_DEBUG",result.toString());
                if (resultCode == Activity.RESULT_OK) {
                    resultUri = result.getUri();
                    Log.d("APP_DEBUG",resultUri.toString());
                    Bitmap bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    circleImageView.setImageBitmap(bitmap);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }




    }
}
