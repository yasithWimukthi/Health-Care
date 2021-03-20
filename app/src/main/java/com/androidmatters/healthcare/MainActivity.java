package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.royrodriguez.transitionbutton.TransitionButton;

public class MainActivity extends AppCompatActivity {

    private TransitionButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.update_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the loading animation when the user tap the button
                loginButton.startAnimation();

                // Do your networking task or background work here.
                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        boolean isSuccessful = true;

                        if (isSuccessful) {
                            loginButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                @Override
                                public void onAnimationStopEnd() {
                                    Intent intent = new Intent(getBaseContext(), EditDoctor.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            loginButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                        }
                    }
                },2000);
            }
        });
    }
}