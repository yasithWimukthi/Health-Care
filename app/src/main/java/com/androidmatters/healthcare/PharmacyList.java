package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PharmacyList extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference db = firebaseFirestore.collection("pharmacy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);
        recyclerView = findViewById(R.id.pharmacy_list);
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();

        db.whereEqualTo("city","Galle").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                  if(!queryDocumentSnapshots.isEmpty()){
                      for(QueryDocumentSnapshot PharmacyList :queryDocumentSnapshots){
                          Pharmacy pharmacy = PharmacyList.toObject(Pharmacy.class);
                          pharmacies.add(pharmacy);
                          System.out.println(pharmacy.getCity());

                      }
                      PharmacyAdpater pharmacyAdpater = new PharmacyAdpater(PharmacyList.this, pharmacies
                      );
                      recyclerView.setAdapter(pharmacyAdpater);
                      recyclerView.setLayoutManager(new LinearLayoutManager(PharmacyList.this));


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
}