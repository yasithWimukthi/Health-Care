package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    private Button loginBtn;
    private Button signUp;
    private EditText userEmail;
    private EditText passwordEditText;
    private ProgressBar loginProgressBar;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.logbtn);
        signUp = findViewById(R.id.redirect_sign_up);
        userEmail = findViewById(R.id.Uemail);
        passwordEditText = findViewById(R.id.Upassword);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        //redirect to sign up page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,signUpUi.class);
                startActivity(intent);
                finish();
            }
        });
        //redirect to sign up page end
        //animation page called
        LoginAnimation();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                loginWithEmailAndPassword(email, password);
            }
        });

    }

    private void loginWithEmailAndPassword(String email, String password) {
        loginProgressBar.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String currentUid = user.getUid();

                            userCollection
                                    .whereEqualTo("userId",currentUid)

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loginProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });

        }else{
            loginProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(login.this,"Please Enter Email And Password",Toast.LENGTH_SHORT).show();
        }
    }


    //create login animation
    public void LoginAnimation(){
        //animation start--------
        loginBtn.setTranslationX(100);
        signUp.setTranslationX(100);
        userEmail.setTranslationX(100);
        passwordEditText.setTranslationX(100);

        loginBtn.setAlpha(0);
        signUp.setAlpha(0);
        userEmail.setAlpha(0);
        passwordEditText.setAlpha(0);

        loginBtn.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        signUp.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        userEmail.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        passwordEditText.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        //animation end----------

    }
}