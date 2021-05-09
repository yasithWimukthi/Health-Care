//package com.androidmatters.healthcare;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class SelectDoctorAppointment extends AppCompatActivity {
//
//    private PatientCreateAppAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        //remove action bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.display_doctor_list);
//        Context context;
//        context = getBaseContext();
//
//
//
//        TextView back = findViewById(R.id.backText1);
//        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
//
//        //back button
//        back.setOnClickListener(v -> {
//            Intent n = new Intent( SelectDoctorAppointment.this,home.class);
//            startActivity(n);
//        });
//
//
//        CollectionReference subjectsRef = fStore.collection("doctors");
//        Spinner spinner = findViewById(R.id.spinner);
//        List<String> subjects = new ArrayList<>();
//        ArrayAdapter<String> ad = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
//        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(ad);
//        subjectsRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                    String subject = document.getString("specialization");
//                    subjects.add(subject);
//                }
//                ad.notifyDataSetChanged();
//            }
//        });
//
//
//
//
//      //on spinner change set recycler
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String text = spinner.getSelectedItem().toString();
//                //query
//                Query query = fStore.collection("doctors").whereEqualTo("specialization",text);
//
//                FirestoreRecyclerOptions<Doctor> options
//                        = new  FirestoreRecyclerOptions.Builder<Doctor>()
//                        .setQuery(query, Doctor.class)
//                        .build();
//
//                adapter = new PatientCreateAppAdapter(options);
//                RecyclerView recyclerView = findViewById(R.id.re1);
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                recyclerView.setAdapter(adapter);
//                adapter.startListening();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//
//
//    }
//
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if (adapter != null) {
//            adapter.stopListening();
//        }
//    }
//
//
//
//
//}