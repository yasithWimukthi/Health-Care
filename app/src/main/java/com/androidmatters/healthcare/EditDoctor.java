package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class EditDoctor extends AppCompatActivity {

    private EditText nameEditText;
    private EditText hospitalEditText;
    private EditText specializationEditText;
    private EditText mobileEditText;
    private Button updateButton;
    private ProgressBar updateProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor2);

        nameEditText = findViewById(R.id.updateDoctorNameEditText);
        hospitalEditText = findViewById(R.id.updateDoctorHospitalEditText);
        specializationEditText = findViewById(R.id.updateDoctorSpecializationEditText);
        mobileEditText = findViewById(R.id.updateDoctorMobileEditText);
        updateButton = findViewById(R.id.updateDoctorBtn);
        updateProgressBar = findViewById(R.id.updateProgressBar);
    }
}