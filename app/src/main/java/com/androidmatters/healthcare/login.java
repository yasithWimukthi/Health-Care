package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {
    Button Loginbtn;
    Button signUp;
    EditText userEmail;
    EditText Upassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        Loginbtn = findViewById(R.id.logbtn);
        signUp = findViewById(R.id.signbtn);
        userEmail = findViewById(R.id.Uemail);
        Upassword = findViewById(R.id.Upassword);

        LoginAnimation();


    }


    //create login animation
    public void LoginAnimation(){
        //animation start--------
        Loginbtn.setTranslationX(100);
        signUp.setTranslationX(100);
        userEmail.setTranslationX(100);
        Upassword.setTranslationX(100);

        Loginbtn.setAlpha(0);
        signUp.setAlpha(0);
        userEmail.setAlpha(0);
        Upassword.setAlpha(0);

        Loginbtn.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        signUp.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        userEmail.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        Upassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        //animation end----------


    }
}