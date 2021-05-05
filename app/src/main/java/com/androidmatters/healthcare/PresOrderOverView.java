package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmatters.healthcare.util.CurrentUser;
import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PresOrderOverView extends AppCompatActivity {
    ImageView imageView;
    TextView SuccessTile,pname,address;
    Button submit,order_canceled;
    ProgressBar loading_bar;
    private final String TAG_PRES = "tag";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    //StorageReference file = firebaseStorage.getReference("prescription");
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference db = firebaseFirestore.collection("prescription");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pres_order_over_view);

        imageView = findViewById(R.id.over_pres_image);
        SuccessTile = findViewById(R.id.successtitle);
        pname = findViewById(R.id.over_pname);
        address = findViewById(R.id.over_address);
        submit = findViewById(R.id.upload_pres);
        order_canceled = findViewById(R.id.order_cancel);
        loading_bar = findViewById(R.id.progressBar);
        loading_bar.setVisibility(View.INVISIBLE);

        PrescriptionBase prescriptionBase = PrescriptionBase.getInstaceBase(); //GET SINGLETON OBJECT OF PRESCRIPTION
        String UserId = CurrentUser.getInstance().getUserId(); //GET CURRENT USER ID
        String U_email = firebaseAuth.getCurrentUser().getEmail();

        //assign all values to object
        Picasso.get().load(prescriptionBase.getPres_image()).centerCrop().resize(400,400).into(imageView);
        pname.setText(prescriptionBase.getPharmacy_name());
        address.setText(prescriptionBase.getAddress());
        prescriptionBase.setUid(UserId);
        prescriptionBase.setUploadedDate(String.valueOf(Calendar.getInstance().getTime()));


        //data insert
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.INVISIBLE);
                loading_bar.setVisibility(View.VISIBLE);
                if(prescriptionBase.getPres_image() != null){

                    StorageReference filepath = firebaseStorage.getReference().child("prescription").child(Timestamp.now().getSeconds()+"");
                    filepath.putFile(Uri.parse(prescriptionBase.getPres_image())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //now store data Firestore
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if(uri.toString() != null){
                                        PrescriptionBase.getInstaceBase()
                                                .setPres_image(uri.toString());
                                        db.add(prescriptionBase).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                loading_bar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(PresOrderOverView.this,Myprescription.class);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(PresOrderOverView.this, "Data Insert Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading_bar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(PresOrderOverView.this, "Error"+e, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(PresOrderOverView.this, "Upload failed", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PresOrderOverView.this, "error"+e, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PresOrderOverView.this, "Error"+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(PresOrderOverView.this, "Image upload failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        order_canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PresOrderOverView.this, "Upload Canceled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PresOrderOverView.this,UploadPrescription.class);
                startActivity(intent);
                finish();
            }
        });

        Animation();
    }



    public void Animation(){
        SuccessTile.setTranslationX(100);
        SuccessTile.setAlpha(0);
        SuccessTile.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();

    }




}