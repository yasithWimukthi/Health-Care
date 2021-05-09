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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Button callCovid;
    private BottomNavigationView bottomNavigationView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patientCollection = db.collection("patients");
    private CollectionReference doctorCollection = db.collection("doctors");
    private FirebaseAuth firebaseAuth;
    private String dpPath = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            makeCall("tel:1390");
        }else {
            Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCall(String uri) {
        if (ActivityCompat.checkSelfPermission(home.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(uri));
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

        //Toast.makeText(getApplicationContext(),CurrentUser.getInstance().getEmail(),Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        String userId = intent.getStringExtra("USER_ID");
        String name = intent.getStringExtra("USER_NAME");
        String user_type = intent.getStringExtra("USER_TYPE");

        firebaseAuth = FirebaseAuth.getInstance();

        helloText = findViewById(R.id.helloText);
        uploadPrescription =  findViewById(R.id.uploadPrescriprionBtn);
        bmiBtn = findViewById(R.id.bmiBtn);
        callAmbulance = findViewById(R.id.callAmbulanceBtn);
        callCovid = findViewById(R.id.callCovidBtn);

        helloText.setText("Hello " + name.split(" ")[0]);

        //Toast.makeText(getApplicationContext(),user_type,Toast.LENGTH_LONG).show();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user_type.equals("Doctor")){
            doctorCollection
                    .whereEqualTo("userId",user.getUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(!value.isEmpty()){
                                for(QueryDocumentSnapshot snapshot : value){
                                    dpPath = snapshot.getString("profilePicture");
                                    helloText.setText("Hello " + snapshot.getString("name").split(" ")[0]);
                                    //Toast.makeText(getApplicationContext(),dpPath,Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            }else{
            patientCollection
                    .whereEqualTo("patientId",user.getUid())
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
                    makeCall("tel:1990");
                }else {
                    if (ActivityCompat.checkSelfPermission(home.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        makeCall("tel:1990");
                    }else {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        //Asking request Permissions
                        ActivityCompat.requestPermissions(home.this, PERMISSIONS_STORAGE, 9);
                    }
                }
            }
        });

        callCovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    makeCall("tel:1390");
                }else {
                    if (ActivityCompat.checkSelfPermission(home.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        makeCall("tel:1390");
                    }else {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        //Asking request Permissions
                        ActivityCompat.requestPermissions(home.this, PERMISSIONS_STORAGE, 9);
                    }
                }
            }
        });

//        callAmbulance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), DoctorAccount.class);
//                intent.putExtra("EMAIL_TO_DOCTOR", email);
//                intent.putExtra("USER_ID_TO_DOCTOR", userId);
//                intent.putExtra("DP", dpPath);
//                startActivity(intent);
//            }
//        });

        /**Bottom navigation*/
        bottomNavigationView = findViewById(R.id.bottomappnavigate);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.pres:
                        startActivity(new Intent(getApplicationContext(),Myprescription.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.Appointment:
                        //startActivity(new Intent(getApplicationContext(),SelectDoctorAppointment.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.setting:
                        //todo intent to the userprofile
                        Intent intent = new Intent(getApplicationContext(), DoctorAccount.class);
                        intent.putExtra("EMAIL_TO_DOCTOR", email);
                        intent.putExtra("USER_ID_TO_DOCTOR", userId);
                        intent.putExtra("DP", dpPath);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return  false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
