package com.androidmatters.healthcare;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;


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
            //System.out.println(this.presList.get(position).getPres_image());
            //Picasso.get().load(this.presList.get(position).getPres_image()).into(holder.imageView);


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set verification
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle("Cancel Prescription ")
                            .setMessage("Are You Sure Cancel Prescription ?")
                            .setIcon(R.drawable.ic_check_pres)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete image from storage
                                    storage.getReferenceFromUrl(presList.get(position).getPres_image()).delete();
                                    //when user clicked cancel button start the progressDialog
                                    ProgressDialog progressDialog = displayDialog();
                                    progressDialog.show();
                                    db.document(presList.get(position).getDocumentId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            removeItem(position);
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            displayDialog().dismiss();
                                            Toast.makeText(context, "Something wrong !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    displayDialog().dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }
            });
            holder.viewdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     displayPrescription(position);
                }
            });

    }

  //display image when click view button
    private void displayPrescription(int position) {
       LayoutInflater layoutInflater = LayoutInflater.from(context);
       View view = layoutInflater.inflate(R.layout.view_upload_prescription,null);
       ImageView picImage = view.findViewById(R.id.pres_img_view);

       Picasso.get().load(presList.get(position).getPres_image()).fit().into(picImage);

       AlertDialog alertDialog = new AlertDialog.Builder(context)
               .setView(view)
               .create();
       alertDialog.show();


    }

    //remove data
    private void removeItem(int position) {
        this.presList.remove(position);
        notifyDataSetChanged();
    }
    //display progress dialog
    private ProgressDialog displayDialog(){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Deleting..");
        progressDialog.setMessage("Please wait until delete");
        return progressDialog;
    }

    @Override
    public int getItemCount() {
        return this.presList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView p_name , upload_date;
        Button viewdata,delete;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            p_name = itemView.findViewById(R.id.phamr_name);
            viewdata = itemView.findViewById(R.id.view_pres);
            upload_date = itemView.findViewById(R.id.upload_date);
            delete = itemView.findViewById(R.id.pres_delete);

        }
    }
}
