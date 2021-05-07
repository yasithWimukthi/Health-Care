package com.androidmatters.healthcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PatientCreateAppAdapter extends FirestoreRecyclerAdapter<Doctor,PatientCreateAppAdapter.doctorViewholder>  {

    private String doctorID;
    private Context context;
    private  Intent intent;

    public PatientCreateAppAdapter(
            @NonNull FirestoreRecyclerOptions<Doctor> options)
    {
        super(options);

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void
    onBindViewHolder(@NonNull doctorViewholder holder,
                     int position, @NonNull Doctor model)
    {
        //get doctor ID
        doctorID = getSnapshots().getSnapshot(position).getId();
        //get Context
        context = holder.itemView.getContext();
        //create firebase storage  instance
        FirebaseStorage fStorage = FirebaseStorage.getInstance();


        holder.hospital.setText("Hospital : "+model.getHospital());
        holder.name.setText("Name : "+model.getName());
        holder.spec.setText("specialization : "+model.getSpecialization());
        holder.phone.setText("phone : "+model.getMobile());

        //get images
        StorageReference fStorageRef = fStorage.getReferenceFromUrl("gs://health-care-8a5eb.appspot.com").child("doctors").child(doctorID);
        fStorageRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get()
                .load(String.valueOf(uri))
                .into(holder.image)).addOnFailureListener(exception -> Toast.makeText(context,"Images not Available ",Toast.LENGTH_SHORT).show());


       // to next activity
        holder.btn.setOnClickListener(view -> {
            intent  = new Intent(context,AppointmentCreate.class);
            intent.putExtra("doctor_ID",doctorID);
            intent.putExtra("doctor_Name",model.getName());
            context.startActivity(intent);
            });
    }

    //doctor View holder

    @NonNull
    @Override
    public doctorViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_create_appointment_card, parent, false);
        return new doctorViewholder(view);
    }

    static class doctorViewholder
            extends RecyclerView.ViewHolder {
        TextView hospital,name, spec,phone;
        FloatingActionButton btn;
        ImageView image;
        public doctorViewholder(@NonNull View itemView)
        {
            super(itemView);
           image =  itemView.findViewById(R.id.DocImage);
           hospital = itemView.findViewById(R.id.hospital);
           phone = itemView.findViewById(R.id.phone);
           name = itemView.findViewById(R.id.name);
           spec= itemView.findViewById(R.id.spec);
           btn = itemView.findViewById(R.id.deleteBtn);

        }
    }

}
