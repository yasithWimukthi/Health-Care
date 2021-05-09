package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidmatters.healthcare.util.CurrentUser;
import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Myprescription extends AppCompatActivity {
    RecyclerView pres_list_rec;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference db = firebaseFirestore.collection("prescription");
    ArrayList<PrescriptionBase> listP = new ArrayList<>();
    public static PrescriptionAdapter passAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_myprescription);

        pres_list_rec = findViewById(R.id.pres_recycler_list);
        String uid = CurrentUser.getInstance().getUserId();
        System.out.println(uid);

        //set selected Item
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomappnavigate);
        bottomNavigationView.setSelectedItemId(R.id.pres);

        //implement transaction
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),home.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.Appointment:
                        //todo intent io the appointmentList
                    case R.id.setting:
                        //todo intent to the userprofile
                }
                return  false;
            }
        });

        db.whereEqualTo("uid",uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot pre_list :queryDocumentSnapshots ) {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        PrescriptionBase prescriptionBase = pre_list.toObject(PrescriptionBase.class);
                        prescriptionBase.setDocumentId(pre_list.getId());
                        listP.add(prescriptionBase);
                    }
                    else{
                        Toast.makeText(Myprescription.this, "No prescription Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }
                //set adapter
                PrescriptionAdapter prescriptionAdapter = new PrescriptionAdapter(Myprescription.this,listP,db);

                pres_list_rec.setLayoutManager(new LinearLayoutManager(Myprescription.this));
                pres_list_rec.setHasFixedSize(true);
                pres_list_rec.setAdapter(prescriptionAdapter);





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Myprescription.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Myprescription.this,home.class);
        overridePendingTransition(-1,0);
        startActivity(intent);
        finish();
    }
}