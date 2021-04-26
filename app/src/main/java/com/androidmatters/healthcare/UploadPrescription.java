package com.androidmatters.healthcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmatters.healthcare.util.CurrentUser;

public class UploadPrescription extends AppCompatActivity {
    //get current user object
    CurrentUser currentUser = CurrentUser.getInstance();
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri imgUrl;

    Button uploadimage;
    TextView Valid_pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_upload_prescription);

        if(currentUser == null){
            //no user found
        }

        Valid_pres = findViewById(R.id.valid_pres);
        uploadimage = findViewById(R.id.upload_img);


        //display custom dialog box
        Valid_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogAlert();
            }
        });

        //redirect to gallery for upload

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();

            }
        });

    }

    private void ShowDialogAlert() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.valid_pres_giudeline,null);
        Button done = view.findViewById(R.id.done);
        //positive button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadPrescription.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();


    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUrl = data.getData();
            Intent intent = new Intent(UploadPrescription.this,AddPresInfo.class);
            String url = imgUrl.toString();
            intent.putExtra("imgUrl",url);
            startActivity(intent);

        }
    }

}