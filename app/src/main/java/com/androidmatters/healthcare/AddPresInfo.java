package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.androidmatters.healthcare.Model.Patient;
import com.androidmatters.healthcare.util.CurrentUser;
import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddPresInfo extends AppCompatActivity {
    ImageView uploadedImage;
    Button upload_saved;
    EditText city,postal,address,phone;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference db = firebaseFirestore.collection("patients");
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

        //get patient object
        getPatientInfo();


        PrescriptionBase prescription = PrescriptionBase.getInstaceBase(); //GET SINGLETON OBJECT
        //attached prescription image
        Picasso.get().load(prescription.getPres_image()).centerCrop().resize(400,400).into(uploadedImage);

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
                    Toast.makeText(AddPresInfo.this, "Please Enter address", Toast.LENGTH_SHORT).show();
                }
                else{
                    //go to the pharmacy list if no error
                    prescription.setCity(getCity);
                    prescription.setAddress(getAddress);
                    prescription.setPhone(getPhone);
                    prescription.setPostalCode(getPostal);
                    //SINGLETON OBJECT UPDATED
                    Intent intent = new Intent(AddPresInfo.this,PharmacyList.class);
                    startActivity(intent);

                }

            }
        });


    }
    public void startAnimation(){
        upload_saved.setTranslationX(100);
        upload_saved.setAlpha(0);
        upload_saved.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();

    }

    //get patient info
    public void getPatientInfo(){
        ArrayList<Patient> pli = new ArrayList<>();
        db.whereEqualTo("patientId",firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //if it is success
                for(QueryDocumentSnapshot plist : queryDocumentSnapshots){
                    Patient p = plist.toObject(Patient.class);
                    address.setText(p.getAddress());
                    phone.setText(p.getMobile());
                    PrescriptionBase.getInstaceBase().setUsername(p.getFirstName()+" "+p.getLastName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("NoUser","No user found this ID");
            }
        });

    }

}