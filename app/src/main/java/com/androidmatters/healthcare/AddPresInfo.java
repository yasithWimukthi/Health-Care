package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AddPresInfo extends AppCompatActivity {
    ImageView uploadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_pres_info);
        uploadedImage = findViewById(R.id.pres_image);

        Intent intent = getIntent();
        String imgurl = intent.getStringExtra("imgUrl");
        Picasso.get().load(Uri.parse(imgurl)).centerCrop().resize(400,400).into(uploadedImage);


    }
}