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

import com.androidmatters.healthcare.Model.Doctor;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorSignUp extends AppCompatActivity {

    private EditText nameEditText;
    private EditText specializationEditText;
    private EditText hospitalEditText;
    private EditText mobileEditText;
    private Button saveBtn;
    private ProgressBar detailsEnterProgressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        nameEditText = findViewById(R.id.sign_up_first_name);
        specializationEditText = findViewById(R.id.sign_up_specilization);
        hospitalEditText = findViewById(R.id.sign_up_hospital);
        mobileEditText = findViewById(R.id.sign_up_mobile);
        saveBtn = findViewById(R.id.updateDoctorBtn);
        detailsEnterProgressBar = findViewById(R.id.doctorDetailsEnterProgressBar);

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
    }

    private void saveDoctorDetails() {
        Doctor doctor = new Doctor();
        doctor.setName(nameEditText.getText().toString().trim());
        doctor.setHospital(hospitalEditText.getText().toString().trim());
        doctor.setMobile(mobileEditText.getText().toString().trim());
        doctor.setSpecialization(specializationEditText.getText().toString().trim());
        doctor.setUserId(CurrentUser.getInstance().getUserId());

        db.collection("doctors").document(CurrentUser.getInstance().getEmail()).set(doctor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        detailsEnterProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
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
}