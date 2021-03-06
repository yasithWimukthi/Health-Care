package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.androidmatters.healthcare.Model.Appointment;
import com.androidmatters.healthcare.UI.AppointmentRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChannelList extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference appointmentCollection = db.collection("appointment");

    private List<Appointment> appointmentList;

    private RecyclerView recyclerview;
    private AppointmentRecyclerAdapter appointmentRecyclerAdapter;

    private String email = "";
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_channel_list);

        Intent intent = getIntent();
        email = intent.getStringExtra("DOCTOR_EMAIL");
        date = intent.getStringExtra("DATE");
        
        appointmentList = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        appointmentCollection
                .whereEqualTo("date",date)
                .whereEqualTo("doctorId",email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot appointments : queryDocumentSnapshots){
                                Appointment appointment = appointments.toObject(Appointment.class);
                                appointmentList.add(appointment);
                            }
                            appointmentRecyclerAdapter = new AppointmentRecyclerAdapter(ChannelList.this,appointmentList);
                            recyclerview.setAdapter(appointmentRecyclerAdapter);
                            appointmentRecyclerAdapter.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}