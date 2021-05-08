package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmatters.healthcare.util.CurrentUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class home extends AppCompatActivity {

    private TextView helloText;
    private Button uploadPrescription;
    private Button bmiBtn;
    private Button callAmbulance;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patientCollection = db.collection("patients");
    private CollectionReference doctorCollection = db.collection("doctors");

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            makeCall();
        }else {
            Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCall() {
        if (ActivityCompat.checkSelfPermission(home.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:1990"));
            home.this.startActivity(callIntent);
        }else{
            Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        helloText = findViewById(R.id.helloText);
        uploadPrescription =  findViewById(R.id.uploadPrescriprionBtn);
        bmiBtn = findViewById(R.id.bmiBtn);
        callAmbulance = findViewById(R.id.callAmbulanceBtn);


        if(CurrentUser.getInstance().getUserType().equals("Doctor")){
            doctorCollection
                    .whereEqualTo("userId",CurrentUser.getInstance().getUserId())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(!value.isEmpty()){
                                for(QueryDocumentSnapshot snapshot : value){
                                    helloText.setText("Hello " + snapshot.getString("name").split(" ")[0]);
                                }
                            }
                        }
                    });
            }else{
            patientCollection
                    .whereEqualTo("patientId",CurrentUser.getInstance().getUserId())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(!value.isEmpty()){
                                for(QueryDocumentSnapshot snapshot : value){
                                    helloText.setText("Hello " + snapshot.getString("firstName"));
                                }
                            }
                        }
                    });
            }

        callAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    makeCall();
                }else {
                    if (ActivityCompat.checkSelfPermission(home.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        makeCall();
                    }else {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        //Asking request Permissions
                        ActivityCompat.requestPermissions(home.this, PERMISSIONS_STORAGE, 9);
                    }
                }
            }
        });


    }

}
