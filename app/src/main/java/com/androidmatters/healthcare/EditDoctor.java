package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Doctor;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        !TextUtils.isEmpty(nameEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(hospitalEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(specializationEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(mobileEditText.getText().toString().trim())
                ){
                    // ALERT MESSAGE
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            EditDoctor.this);
                    alertDialog2.setTitle("Confirm Update...");

                    alertDialog2.setMessage("Do you want to update details ?");

                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    updateProgressBar.setVisibility(View.VISIBLE);
                                    updateDoctor();
                                }
                            });

                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog2.show();

                }else{
                    Toast.makeText(getApplicationContext(),"Empty fields are not allowed.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setName(nameEditText.getText().toString().trim());
        doctor.setHospital(hospitalEditText.getText().toString().trim());
        doctor.setSpecialization(specializationEditText.getText().toString().trim());
        doctor.setMobile(mobileEditText.getText().toString().trim());

        doctorsCollection.document(CurrentUser.getInstance().getEmail())
                .set(doctor)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateProgressBar.setVisibility(View.INVISIBLE);
                        //todo redirect to user profile
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Somthing went wrong. Try again.",Toast.LENGTH_LONG).show();
                    }
                });
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