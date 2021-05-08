package com.androidmatters.healthcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatAppointmentAdapter extends FirestoreRecyclerAdapter<AppointmnetDetails,PatAppointmentAdapter.appointmentViewholder>  {

    private FirebaseFirestore db;

    public PatAppointmentAdapter(
            @NonNull FirestoreRecyclerOptions<AppointmnetDetails> options)
    {

        super(options);

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void
    onBindViewHolder(@NonNull appointmentViewholder holder,
                     int position, @NonNull AppointmnetDetails model)
    {

        holder.date.setText("date : "+model.getDate());
        holder.doctorName.setText("doctor : "+model.getDoctorName());
        holder.number.setText("number : "+model.getNumber());
        db = FirebaseFirestore.getInstance();
        Context context = holder.itemView.getContext();




        // delete appointments
        holder.btn.setOnClickListener(view -> {

            Query query4 =  db.collection("appointment").whereEqualTo("date", model.getDate()).whereEqualTo("patientId",model.getPatientId());
            query4.get().addOnCompleteListener(task1 -> {

                if (task1.isSuccessful()) {

                    if(task1.getResult().isEmpty())
                    {
                        Toast.makeText(context,"No appointment found",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (QueryDocumentSnapshot document : task1.getResult()) {

                          //check is  date older than today
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate now = LocalDate.now();
                            String today = dtf.format(now);
                            now = LocalDate.parse(today, dtf);
                            LocalDate date = LocalDate.parse(model.getDate(), dtf);
                            if(now.isAfter(date))
                            {
                                document.getReference().delete();
                            }
                            else
                            {
                                Toast.makeText(context,"Only visited appointments can be deleted",Toast.LENGTH_SHORT).show();
                            }



                        }
                    }

                }

            });



        });
    }


    @NonNull
    @Override
    public appointmentViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_appointments, parent, false);
        return new appointmentViewholder(view);
    }


    class appointmentViewholder
            extends RecyclerView.ViewHolder {
        TextView date, doctorName, number;
        FloatingActionButton btn;
        public appointmentViewholder(@NonNull View itemView)
        {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            doctorName = itemView.findViewById(R.id.dName);
            number = itemView.findViewById(R.id.pNumber);
            btn = itemView.findViewById(R.id.deleteBtn);



        }
    }





}
