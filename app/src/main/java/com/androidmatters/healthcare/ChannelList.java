package com.androidmatters.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidmatters.healthcare.Model.Appointment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChannelList extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference appointmentCollection = db.collection("appointment");

    private List<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
    }
}