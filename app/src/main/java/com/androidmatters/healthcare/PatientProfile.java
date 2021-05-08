package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Patient;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PatientProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Button logout,update;
    TextView username,email,location,phone;
    ImageView user_profile,image_pickers;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference db = firebaseFirestore.collection("patients");

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_patient_profile);

        username = findViewById(R.id.ex_username);
        email = findViewById(R.id.ex_email);
        location = findViewById(R.id.ex_location);
        phone = findViewById(R.id.ex_phone);
        user_profile = findViewById(R.id.profile_img);
        image_pickers = findViewById(R.id.image_picker);
        logout = findViewById(R.id.user_logout);
        update = findViewById(R.id.updatePatientBtn1);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);


        String uid = CurrentUser.getInstance().getUserId();


        db.whereEqualTo("patientId",uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot Ulist : queryDocumentSnapshots){
                    Patient patient = Ulist.toObject(Patient.class);
                    username.setText(patient.getFirstName() +" "+patient.getLastName());
                    email.setText(patient.getEmail());
                    location.setText(patient.getAddress());
                    phone.setText(patient.getMobile());

                    Picasso.get().load(patient.getProfilePicture()).resize(200,200).into(user_profile);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientProfile.this, "error"+e, Toast.LENGTH_SHORT).show();
            }
        });

        //pic new image from gallery
        image_pickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                OpenImage();
            }
        });

        //update profile
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, EditPatient.class);
                startActivity(intent);
            }
        });

    }

    private void OpenImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgUrl = "";
        StorageReference ref =  FirebaseStorage.getInstance().getReference().child("patients").child(Timestamp.now().getSeconds()+"");
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Picasso.get().load(data.getData()).resize(200,200).into(user_profile);

            ref.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map UpdateUri = new HashMap<>();
                            UpdateUri.put("profilePicture",uri.toString());
                            db.document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .update(UpdateUri)
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(PatientProfile.this, "Profile Image Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(PatientProfile.this, "error"+e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(PatientProfile.this, "error"+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }
}