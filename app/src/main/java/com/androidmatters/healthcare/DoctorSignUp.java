package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorSignUp extends AppCompatActivity {

    private EditText nameEditText;
    private EditText specializationEditText;
    private EditText hospitalEditText;
    private EditText mobileEditText;
    private Button saveBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        nameEditText = findViewById(R.id.sign_up_name);
        specializationEditText = findViewById(R.id.sign_up_specilization);
        hospitalEditText = findViewById(R.id.sign_up_hospital);
        mobileEditText = findViewById(R.id.sign_up_mobile);
        saveBtn = findViewById(R.id.saveBtn);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}