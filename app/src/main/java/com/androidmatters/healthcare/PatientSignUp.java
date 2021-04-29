package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        dobEditText = findViewById(R.id.sign_up_specilization);
        ageEditText = findViewById(R.id.sign_up_mobile);
        addressEditText = findViewById(R.id.sign_up_address);
        mobileEditText = findViewById(R.id.sign_up_patient_mobile);
        patientSignUpProgressBar = findViewById(R.id.patientDetailsEnterProgressBar);
        saveButton = findViewById(R.id.patientSaveBtn);
        
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

    }
}