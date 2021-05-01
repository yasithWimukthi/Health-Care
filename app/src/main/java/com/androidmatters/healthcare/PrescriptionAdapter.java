package com.androidmatters.healthcare;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {
     private Context context;
     private ArrayList<PrescriptionBase> presList;
     CollectionReference db;
     FirebaseStorage storage = FirebaseStorage.getInstance();


    public PrescriptionAdapter(Context context, ArrayList<PrescriptionBase> presList,CollectionReference db) {
        this.context = context;
        this.presList = presList;
        this.db = db;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.prescription_rawdata,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.p_name.setText(this.presList.get(position).getPharmacy_name());
            holder.upload_date.setText(this.presList.get(position).getUploadedDate());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Prescription")
                            .setMessage("Are You Sure Delete Prescription ?")
                            .setIcon(R.drawable.ic_check_pres)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.document(presList.get(position).getUsername()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //set verification
                                            removeItem(position);
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Something wrong !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //after click cancel
                                }
                            }).create();
                    alertDialog.show();

                }
            });
            //click view button
            holder.viewdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //i have to create
                    Toast.makeText(context, "View Button Clicked", Toast.LENGTH_SHORT).show();
                }
            });

    }
    //remove data
    private void removeItem(int position) {
        this.presList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.presList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView p_name , upload_date;
        Button viewdata,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            p_name = itemView.findViewById(R.id.phamr_name);
            upload_date = itemView.findViewById(R.id.upload_date);
            viewdata = itemView.findViewById(R.id.viewbtn);
            delete = itemView.findViewById(R.id.pres_delete);

        }
    }
}
