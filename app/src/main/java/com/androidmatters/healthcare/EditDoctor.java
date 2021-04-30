package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Doctor;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

public class EditDoctor extends AppCompatActivity {

    private EditText nameEditText;
    private EditText hospitalEditText;
    private EditText specializationEditText;
    private EditText mobileEditText;
    private Button updateButton;
    private ProgressBar updateProgressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference doctorsCollection = db.collection("doctors");

    private Doctor currentLoggedDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor2);

        currentLoggedDoctor = new Doctor();

        nameEditText = findViewById(R.id.updateDoctorNameEditText);
        hospitalEditText = findViewById(R.id.updateDoctorHospitalEditText);
        specializationEditText = findViewById(R.id.updateDoctorSpecializationEditText);
        mobileEditText = findViewById(R.id.updateDoctorMobileEditText);
        updateButton = findViewById(R.id.updateDoctorBtn);
        updateProgressBar = findViewById(R.id.updateProgressBar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        doctorsCollection.whereEqualTo("userId", CurrentUser.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot doctor : queryDocumentSnapshots){
                                currentLoggedDoctor = doctor.toObject(Doctor.class);
                            }
                            nameEditText.setText(currentLoggedDoctor.getName());
                            hospitalEditText.setText(currentLoggedDoctor.getHospital());
                            specializationEditText.setText(currentLoggedDoctor.getSpecialization());
                            mobileEditText.setText(currentLoggedDoctor.getMobile());
                        }else{
                            Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                    }
                });
    }
}