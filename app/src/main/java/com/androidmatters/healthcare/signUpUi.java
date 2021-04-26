package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signUpUi extends AppCompatActivity {

    private Button redirectToLogin;
    private Button signUpBtn;
    private ProgressBar signUpProgressBar;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RadioButton asDoctor;
    private RadioButton asPatient;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private String userType;

    // FIRESTORE CONNECTION
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up_ui);

        redirectToLogin = findViewById(R.id.redirect_login);
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        asDoctor = findViewById(R.id.asDoctor);
        asPatient = findViewById(R.id.asPatient);

        userType = "Patient";

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    //todo already logged in
                }else{

                }
            }
        };

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        !TextUtils.isEmpty(emailEditText.getText().toString().trim()) &&
                        !TextUtils.isEmpty(passwordEditText.getText().toString().trim()) &&
                         passwordEditText.getText().toString().length() >= 6
                ){
                    createUserAccount(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim());
                }else if(passwordEditText.getText().toString().length() < 6){
                    Toast.makeText(getApplicationContext(),"Password should contain at least 6 characters.",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Empty fields are not allowed.",Toast.LENGTH_LONG).show();
                }
            }
        });

        redirectToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpUi.this,login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void createUserAccount(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            signUpProgressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserId = currentUser.getUid();

                                //create a user map to create a user in the User collection
                                Map<String, String> userObject = new HashMap<String, String>();
                                userObject.put("userId",currentUserId);
                                userObject.put("userEmail",email);
                                userObject.put("userType",userType);

                                // save to firestore DB
                                collectionReference.add(userObject)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(Objects.requireNonNull(task.getResult()).exists()){
                                                                    signUpProgressBar.setVisibility(View.INVISIBLE);
                                                                    String email = task.getResult().getString("email");
                                                                    //String name = task.getResult().getString("username");

                                                                    CurrentUser currentUser = CurrentUser.getInstance();
                                                                    currentUser.setUserId(currentUserId);
                                                                    currentUser.setEmail(email);

                                                                    //todo navigate to home screen
                                                                }else{
                                                                    signUpProgressBar.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"Sign up failed.",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Sign up failed.",Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(),"Please enter email and password.",Toast.LENGTH_LONG).show();
        }
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.asDoctor:
                if (checked)
                    userType = "Doctor";
                    break;
            case R.id.asPatient:
                if (checked)
                    userType = "Patient";
                    break;
        }
    }
}