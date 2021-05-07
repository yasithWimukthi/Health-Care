package com.androidmatters.healthcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class PatientViewAppointments extends AppCompatActivity {

    private PatAppointmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //remove action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_appointment_list);

        Context context;
        context = getApplicationContext();
        TextView back = findViewById(R.id.backText3);
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        Query query = fStore.collection("appointment").whereEqualTo("patientId",email );
        FirestoreRecyclerOptions<AppointmnetDetails> options
                = new  FirestoreRecyclerOptions.Builder<AppointmnetDetails>()
                .setQuery(query,AppointmnetDetails.class)
                .build();

        adapter = new PatAppointmentAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.re2);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);


        back.setOnClickListener(v -> {
            Intent n = new Intent( PatientViewAppointments.this, home.class);
            startActivity(n);
        });


    }





    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }




    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }




}