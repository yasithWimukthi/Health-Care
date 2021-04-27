package com.androidmatters.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyAdpater extends RecyclerView.Adapter<PharmacyAdpater.ViewHolder> {
   private Context context;
   private ArrayList<Pharmacy> pharmaciesList;

    public PharmacyAdpater(Context context, ArrayList<Pharmacy> pharmaciesList) {
        this.context = context;
        this.pharmaciesList = pharmaciesList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pharmacy_rawdata,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(this.pharmaciesList.get(position).getCity());
        holder.desc.setText(this.pharmaciesList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        System.out.println(pharmaciesList.size());
        return pharmaciesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             name = itemView.findViewById(R.id.phar_name);
             desc = itemView.findViewById(R.id.par_description);


        }
    }
}
