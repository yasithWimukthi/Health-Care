package com.androidmatters.healthcare.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidmatters.healthcare.Model.Appointment;
import com.androidmatters.healthcare.Model.Patient;
import com.androidmatters.healthcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder>{
    private Context context;
    private List <Appointment> appointmentList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patientCollection = db.collection("patients");

    public AppointmentRecyclerAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_details_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerAdapter.ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        //Patient[] currentPatient = new Patient[1];

        patientCollection.whereEqualTo("email", appointment.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Patient currentPatient = new Patient();
                            for(QueryDocumentSnapshot patient : queryDocumentSnapshots){
                                currentPatient = patient.toObject(Patient.class);
                            }
                            holder.name.setText("Name : " + currentPatient.getFirstName() + " " +currentPatient.getLastName());
                            holder.age.setText("Age : " + Integer.toString(currentPatient.getAge()));
                            holder.mobile.setText("Mobile :" + currentPatient.getMobile());
                            holder.number.setText("Number : " +Integer.toString(appointment.getNumber()));

                            Picasso.get()
                                    .load(currentPatient.getProfilePicture())
                                    .placeholder(R.drawable.doctor_illustration)
                                    .fit()
                                    .into(holder.dp);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("onBindViewHolder", "onFailure: " + e.getMessage());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView dp;
        public TextView name;
        public TextView age;
        public TextView mobile;
        public TextView number;

        public ViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;
            dp = view.findViewById(R.id.dp);
            name = view.findViewById(R.id.patient_name);
            age = view.findViewById(R.id.patient_age);
            mobile = view.findViewById(R.id.patient_mobile);
            number = view.findViewById(R.id.patient_number);
        }
    }
}
