package com.androidmatters.healthcare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Whr extends AppCompatActivity {

    EditText waists,hips;
    TextView value, back;
    Button calc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_whr);
        waists = findViewById(R.id.waist);
        hips =  findViewById(R.id.hip);
        calc = findViewById(R.id.calwhr);
        value = findViewById(R.id.whrtexts);
        back = findViewById(R.id.backhome);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Whr.this,home.class);
                startActivity(intent);
                finish();
            }
        });


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float h =  Float.parseFloat(hips.getText().toString());
                float w =   Float.parseFloat(waists.getText().toString());

                float tot = w/h;


                if(tot < 0.96){
                    openCal(tot,"Low Risk");
                }
                else if(tot > 0.96 && tot <= 1.00){
                    openCal(tot,"Moderate Risk");

                }
                else if(tot > 1.01 && tot < 1.13){
                    openCal(tot,"High Risk");


                }
                else if(tot >1.03){
                    openCal(tot,"Very High Risk");

                }
                else{
                    value.setText("Invalid");
                }

            }
        });

    }
    public void openCal(float value ,String msg){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("WHR Value :"+value)
                .setMessage("Risk :"+msg)
                .create();
        dialog.show();
    }

}