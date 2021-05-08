package com.androidmatters.healthcare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PharmacyAdpater extends ArrayAdapter {
   private Activity context;
   private ArrayList<Pharmacy> pharmaciesList;
   FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
   CollectionReference prescriptiondb = firebaseFirestore.collection("prescription");
    CollectionReference Pharmacydb = firebaseFirestore.collection("pharmacy");

    public PharmacyAdpater(@NonNull Activity context, @NonNull ArrayList<Pharmacy> pharmaciesList) {
        super(context, R.layout.pharmacy_rawdata, pharmaciesList);
        this.pharmaciesList = pharmaciesList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.pharmacy_rawdata,null,true);
        ImageView imageView = view.findViewById(R.id.phar_image);
        TextView name = view.findViewById(R.id.phar_name);
        TextView description = view.findViewById(R.id.par_description);
        ProgressBar progressBar = view.findViewById(R.id.raiting);

        name.setText(pharmaciesList.get(position).getPname());
        description.setText(pharmaciesList.get(position).getDescription());
        progressBar.setProgress(Integer.parseInt(pharmaciesList.get(position).getRating()));

        Picasso.get().load(pharmaciesList.get(position).getImageUrl()).resize(100,100).into(imageView);

        return view;

    }

}

