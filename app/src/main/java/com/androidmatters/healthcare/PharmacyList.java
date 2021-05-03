package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PharmacyList extends AppCompatActivity {

    ListView listView;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference db = firebaseFirestore.collection("pharmacy");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pharmacy_list);

        listView = findViewById(R.id.p_listView);
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();
        PrescriptionBase prescription = PrescriptionBase.getInstaceBase(); //GET SINGLETON OBJECT


        if(prescription != null){
            //retrieve all pharmacy According to the City
            db.whereEqualTo("city", prescription.getCity()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot PharmacyList :queryDocumentSnapshots){
                            Pharmacy pharmacy = PharmacyList.toObject(Pharmacy.class);
                            pharmacies.add(pharmacy);
                        }

                        PharmacyAdpater pharmacyAdpater = new PharmacyAdpater(PharmacyList.this,pharmacies);
                        listView.setAdapter(pharmacyAdpater);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //when user click pharmacy display Alert
                                AlertDialog alertDialog = new AlertDialog.Builder(PharmacyList.this)
                                        .setTitle("Confirmation Selected Pharmacy")
                                        .setMessage("You Selected "+pharmacies.get(position).getCity() +"Pharmacy")
                                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //when user click continue intent to new Activity
                                                prescription.setPharmacy_name(pharmacies.get(position).getPname());
                                                Intent intent = new Intent(PharmacyList.this,PresOrderOverView.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(PharmacyList.this, "Please Select Pharmacy", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .create();
                                alertDialog.show();

                            }
                        });

                    }else{
                        Toast.makeText(PharmacyList.this, "No Pharmacy Found !", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PharmacyList.this, "Something wrong"+e, Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            //when passed null object
            Log.d("prescription","no object founds");
        }



















    }
}