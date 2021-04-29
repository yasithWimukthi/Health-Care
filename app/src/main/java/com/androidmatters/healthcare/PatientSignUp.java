package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Patient;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientSignUp extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText dobEditText;
    private EditText ageEditText;
    private EditText addressEditText;
    private EditText mobileEditText;
    private ProgressBar patientSignUpProgressBar;
    private Button saveButton;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    }

    private void savePatient() {
        Patient patient = new Patient();
        patient.setFirstName(firstNameEditText.getText().toString().trim());
        patient.setLastName(lastNameEditText.getText().toString().trim());
        patient.setAddress(addressEditText.getText().toString().trim());
        patient.setAge(Integer.parseInt(ageEditText.getText().toString().trim()));
        patient.setDob(dobEditText.getText().toString().trim());
        patient.setMobile(mobileEditText.getText().toString().trim());

        db.collection("patients").document(CurrentUser.getInstance().getEmail()).set(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        patientSignUpProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
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
}