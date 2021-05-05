package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Doctor;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DoctorSignUp extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    private EditText nameEditText;
    private EditText specializationEditText;
    private EditText hospitalEditText;
    private EditText mobileEditText;
    private Button saveBtn;
    private ProgressBar detailsEnterProgressBar;
    private ImageButton selectDoctor;
    private ImageView doctorDp;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        nameEditText = findViewById(R.id.sign_up_first_name);
        specializationEditText = findViewById(R.id.sign_up_specilization);
        hospitalEditText = findViewById(R.id.sign_up_hospital);
        mobileEditText = findViewById(R.id.sign_up_mobile);
        saveBtn = findViewById(R.id.saveDoctorBtn);
        detailsEnterProgressBar = findViewById(R.id.doctorDetailsEnterProgressBar);
        selectDoctor = findViewById(R.id.selectDoctorImageBtn);
        doctorDp = findViewById(R.id.doctorDp);

        storageReference = FirebaseStorage.getInstance().getReference();

        //Toast.makeText(getApplicationContext(),CurrentUser.getInstance().getEmail(),Toast.LENGTH_LONG).show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        !TextUtils.isEmpty(nameEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(specializationEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(hospitalEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mobileEditText.getText().toString().trim())
                ){
                    detailsEnterProgressBar.setVisibility(View.VISIBLE);
                    saveDoctorDetails();
                }else{
                    Toast.makeText(getApplicationContext(),"Empty fields are not allowed.",Toast.LENGTH_LONG).show();
                }

            }
        });

        selectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });
    }

    /**
     *
     *  save doctor details into the doctor collection
     */
    private void saveDoctorDetails() {
        Doctor doctor = new Doctor();
        doctor.setName(nameEditText.getText().toString().trim());
        doctor.setHospital(hospitalEditText.getText().toString().trim());
        doctor.setMobile(mobileEditText.getText().toString().trim());
        doctor.setSpecialization(specializationEditText.getText().toString().trim());
        doctor.setUserId(CurrentUser.getInstance().getUserId());
        doctor.setEmail(CurrentUser.getInstance().getEmail());

//        db.collection("doctors").document(CurrentUser.getInstance().getEmail()).set(doctor)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        detailsEnterProgressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getApplicationContext(),EditDoctor.class));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        detailsEnterProgressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getApplicationContext(),"Try Again.",Toast.LENGTH_LONG).show();
//                    }
//                });

        StorageReference filePath = storageReference.child("doctors").child(CurrentUser.getInstance().getEmail());
        filePath.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        doctor.setProfilePicture(uri.toString());
                                        db.collection("doctors").document(CurrentUser.getInstance().getEmail()).set(doctor)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        detailsEnterProgressBar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getApplicationContext(),EditDoctor.class));
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        detailsEnterProgressBar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(getApplicationContext(),"Try Again.",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("saveDoctorDetails", "onFailure: " + e.getMessage() );
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        detailsEnterProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Try Again.",Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.getData();
                doctorDp.setImageURI(imageUri);
            }
        }
    }
}