package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Patient;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditPatient extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText dobEditText;
    private EditText ageEditText;
    private EditText addressEditText;
    private EditText mobileEditText;
    private Button updateButton;
    private ProgressBar updateProgressBar;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference patientsCollection = db.collection("patients");

    private Patient currentLoggedPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        currentLoggedPatient = new Patient();

        firstNameEditText = findViewById(R.id.updatePatientFirstName);
        lastNameEditText = findViewById(R.id.updatePatientLastName);
        dobEditText = findViewById(R.id.updatePatientDOF);
        //ageEditText = findViewById(R.id.updatePatientAge);
        addressEditText = findViewById(R.id.updatePatientAddress);
        mobileEditText = findViewById(R.id.updatePatientMobile);
        updateButton = findViewById(R.id.updatePatientBtn);
        updateProgressBar = findViewById(R.id.updateProgressBar);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        !TextUtils.isEmpty(firstNameEditText.getText().toString().trim()) &&
                                !TextUtils.isEmpty(lastNameEditText.getText().toString().trim()) &&
                                !TextUtils.isEmpty(dobEditText.getText().toString().trim()) &&
                                //!TextUtils.isEmpty(ageEditText.getText().toString().trim()) &&
                                !TextUtils.isEmpty(addressEditText.getText().toString().trim()) &&
                                !TextUtils.isEmpty(mobileEditText.getText().toString().trim())
                ){
                    // ALERT MESSAGE
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            EditPatient.this);
                    alertDialog2.setTitle("Confirm Update...");

                    alertDialog2.setMessage("Do you want to update details ?");

                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    updateProgressBar.setVisibility(View.VISIBLE);
                                    updatePatient();
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

    private void updatePatient() {
        Map updatePatient = new HashMap();
        updatePatient.put("firstName", firstNameEditText.getText().toString().trim());
        updatePatient.put("lastName", lastNameEditText.getText().toString().trim());
        updatePatient.put("dob", dobEditText.getText().toString().trim());
        //updatePatient.put("age", ageEditText.getText().toString().trim());
        updatePatient.put("address", addressEditText.getText().toString().trim());
        updatePatient.put("mobile", mobileEditText.getText().toString().trim());

        patientsCollection.document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(updatePatient)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateProgressBar.setVisibility(View.INVISIBLE);
                        //todo redirect to user profile
                        Intent intent = new Intent(EditPatient.this, PatientProfile.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something went wrong. Try again.",Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        patientsCollection.whereEqualTo("patientId", CurrentUser.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot patient : queryDocumentSnapshots){
                                currentLoggedPatient = patient.toObject(Patient.class);
                            }
                            firstNameEditText.setText(currentLoggedPatient.getFirstName());
                            lastNameEditText.setText(currentLoggedPatient.getLastName());
                            dobEditText.setText(currentLoggedPatient.getDob());
                            //ageEditText.setText(String.valueOf(Integer.toString(currentLoggedPatient.getAge())));
                            addressEditText.setText(currentLoggedPatient.getAddress());
                            mobileEditText.setText(currentLoggedPatient.getMobile());
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