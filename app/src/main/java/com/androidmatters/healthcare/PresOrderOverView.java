package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmatters.healthcare.util.PrescriptionBase;
import com.squareup.picasso.Picasso;

public class PresOrderOverView extends AppCompatActivity {
    ImageView imageView;
    TextView SuccessTile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pres_order_over_view);
        imageView = findViewById(R.id.over_pres_image);
        SuccessTile = findViewById(R.id.successtitle);
        PrescriptionBase prescriptionBase = PrescriptionBase.getInstaceBase();


        Picasso.get().load(prescriptionBase.getPres_image()).centerCrop().resize(400,400).into(imageView);
        Animation();


    }

    public void Animation(){
        SuccessTile.setTranslationX(100);
        SuccessTile.setAlpha(0);
        SuccessTile.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();


    }




}