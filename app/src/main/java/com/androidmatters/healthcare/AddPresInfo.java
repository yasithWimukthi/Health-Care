package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class AddPresInfo extends AppCompatActivity {
    ImageView uploadedImage;
    Button upload_saved;
    EditText city,postal,address,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_pres_info);


        uploadedImage = findViewById(R.id.pres_image);
        upload_saved = findViewById(R.id.contineBtn);
        city = findViewById(R.id.Pres_city);
        postal = findViewById(R.id.pres_postcode);
        phone = findViewById(R.id.pres_phone);
        address = findViewById(R.id.pres_address);

        //attached prescription image
        Intent intent = getIntent();
        String imgurl = intent.getStringExtra("imgUrl");
        Picasso.get().load(Uri.parse(imgurl)).centerCrop().resize(400,400).into(uploadedImage);

        //animation start
        startAnimation();

        //save btn and next
        upload_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCity  = city.getText().toString();
                String getPostal = postal.getText().toString();
                String getPhone = phone.getText().toString();
                String getAddress = address.getText().toString();
                //validation
                if(getCity.trim().isEmpty()){
                    Toast.makeText(AddPresInfo.this, "Please Enter City", Toast.LENGTH_SHORT).show();
                }
                else if(getPostal.trim().isEmpty()){
                    Toast.makeText(AddPresInfo.this, "Please Enter City Postal code", Toast.LENGTH_SHORT).show();
                }
                else if(getPhone.trim().isEmpty()){
                    Toast.makeText(AddPresInfo.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
                else if(getPhone.trim().length() > 10 || getPhone.trim().length() <10){
                    Toast.makeText(AddPresInfo.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else if(getAddress.trim().isEmpty()){
                    Toast.makeText(AddPresInfo.this, "Please Enter  Phone Number", Toast.LENGTH_SHORT).show();
                }
                else{
                    //go to the pharmacy list if no error






                }

            }
        });










    }
    public void startAnimation(){
        upload_saved.setTranslationX(100);
        upload_saved.setAlpha(0);
        upload_saved.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();

    }



}