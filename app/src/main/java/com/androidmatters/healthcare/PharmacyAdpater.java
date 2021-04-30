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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyAdpater extends ArrayAdapter {
   private Activity context;
   private ArrayList<Pharmacy> pharmaciesList;


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

        TextView name = view.findViewById(R.id.phar_name);
        TextView description = view.findViewById(R.id.par_description);

        name.setText(pharmaciesList.get(position).getCity());
        description.setText(pharmaciesList.get(position).getDescription());


        return view;

    }
}

