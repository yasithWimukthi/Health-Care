package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Patient;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PatientSignUp extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText dobEditText;
    private EditText ageEditText;
    private EditText addressEditText;
    private EditText mobileEditText;
    private ProgressBar patientSignUpProgressBar;
    private Button saveButton;
    private ImageButton selectPatient;
    private ImageView patientDp;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);

        firstNameEditText = findViewById(R.id.sign_up_first_name);
        lastNameEditText = findViewById(R.id.sign_up_last_name);
        dobEditText = findViewById(R.id.sign_up_dob);
        ageEditText = findViewById(R.id.sign_up_age);
        addressEditText = findViewById(R.id.sign_up_address);
        mobileEditText = findViewById(R.id.sign_up_patient_mobile);
        patientSignUpProgressBar = findViewById(R.id.patientDetailsEnterProgressBar);
        saveButton = findViewById(R.id.patientSaveBtn);
        selectPatient = findViewById(R.id.selectPatientImageBtn);
        patientDp = findViewById(R.id.patientDp);

        storageReference = FirebaseStorage.getInstance().getReference();

        Toast.makeText(getApplicationContext(),CurrentUser.getInstance().getEmail(),Toast.LENGTH_LONG).show();
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        !TextUtils.isEmpty(firstNameEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(lastNameEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(dobEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(ageEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(addressEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mobileEditText.getText().toString().trim())
                ){
                    patientSignUpProgressBar.setVisibility(View.VISIBLE);
                    savePatient();
                }else{
                    Toast.makeText(getApplicationContext(),"Empty fields are not allowed.",Toast.LENGTH_LONG).show();
                }

            }
        });

        selectPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });
    }

    private void savePatient() {
        Patient patient = new Patient();
        patient.setFirstName(firstNameEditText.getText().toString().trim());
        patient.setLastName(lastNameEditText.getText().toString().trim());
        patient.setAddress(addressEditText.getText().toString().trim());
        patient.setAge(Integer.parseInt(ageEditText.getText().toString().trim()));
        patient.setDob(dobEditText.getText().toString().trim());
        patient.setMobile(mobileEditText.getText().toString().trim());
        patient.setPatientId(CurrentUser.getInstance().getUserId());
        patient.setEmail(CurrentUser.getInstance().getEmail());

//        db.collection("patients").document(CurrentUser.getInstance().getEmail()).set(patient)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(),"Try Again.",Toast.LENGTH_LONG).show();
//                    }
//                });

        StorageReference filePath = storageReference.child("patients").child(CurrentUser.getInstance().getEmail());
        filePath.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        patient.setProfilePicture(uri.toString());
                                        db.collection("patients").document(CurrentUser.getInstance().getEmail()).set(patient)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(),"Try Again.",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        patientSignUpProgressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        patientSignUpProgressBar.setVisibility(View.INVISIBLE);
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
                patientDp.setImageURI(imageUri);
            }
        }
    }

}