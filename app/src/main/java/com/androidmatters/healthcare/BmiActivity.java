package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BmiActivity extends AppCompatActivity {

    private EditText weightEditText;
    private EditText heightEditText;
    private Button calcBmi;
    private TextView bmiValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bmi);

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        calcBmi = findViewById(R.id.calcBmiBtn);
        bmiValue = findViewById(R.id.bmiValueText);

        calcBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(weightEditText.getText().toString().trim()) && !TextUtils.isEmpty(heightEditText.getText().toString().trim())){
                    calculateBmi(Float.parseFloat(heightEditText.getText().toString().trim()),
                            Float.parseFloat(weightEditText.getText().toString().trim()));
                }else{
                    Toast.makeText(getApplicationContext(),"Enter weight and height.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public float calculateBmi(float height,float weight) {
        float bmi = weight/(height*height);
        bmiValue.setText("Your BMI value is : " + bmi);
        return bmi;
    }
}