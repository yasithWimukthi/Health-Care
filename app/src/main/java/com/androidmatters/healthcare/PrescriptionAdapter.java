package com.androidmatters.healthcare;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.androidmatters.healthcare.util.PrescriptionBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            holder.p_status.setText(this.presList.get(position).getStatus());

            //System.out.println(this.presList.get(position).getPres_image());
            //Picasso.get().load(this.presList.get(position).getPres_image()).into(holder.imageView);

            if(presList.get(position).getStatus().equals("Canceled")){
                holder.removed.setVisibility(View.INVISIBLE); //IF IT IS ALREADY CANCELED
                holder.delete.setVisibility(View.VISIBLE);
            }
            else{
                holder.removed.setVisibility(View.VISIBLE);   //IF IT IS NOT CANCELED
                holder.delete.setVisibility(View.INVISIBLE);
            }


//update method implementation
            holder.removed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   AlertDialog UpdateAlet = new AlertDialog.Builder(context)
                           .setTitle("Order Canceling")
                           .setMessage("Are You Sure Cancel Uploaded Prescription to "+presList.get(position).getPharmacy_name())
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   holder.delete.setVisibility(View.VISIBLE);
                                   holder.removed.setVisibility(View.INVISIBLE);
                                   Map status = new HashMap<>();
                                   status.put("status","Canceled");
                                   ProgressDialog progressDialog = displayDialog("Please wait until it Cancel","Canceling");
                                   progressDialog.show();

                                   db.document(presList.get(position).getDocumentId()).update(status).addOnSuccessListener(new OnSuccessListener() {
                                       @Override
                                       public void onSuccess(Object o) {
                                           progressDialog.dismiss();
                                           UpdateData(position);
                                           Toast.makeText(context, "Order Canceled Successfully", Toast.LENGTH_SHORT).show();



                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           progressDialog.dismiss();
                                           Toast.makeText(context, "error"+e, Toast.LENGTH_SHORT).show();
                                       }
                                   });
                               }
                           })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                               }
                           })
                           .create();
                    UpdateAlet.show();
                }
            });


 //delete method implementation
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //set verification
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Prescription")
                            .setMessage("Are You Sure Delete Prescription ?")
                            .setIcon(R.drawable.ic_check_pres)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete image from storage
                                    storage.getReferenceFromUrl(presList.get(position).getPres_image()).delete();
                                    //when user clicked cancel button start the progressDialog
                                    ProgressDialog progressDialog = displayDialog("Please wait until it delete","Deleting");
                                    progressDialog.show();
                                    db.document(presList.get(position).getDocumentId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            holder.delete.setVisibility(View.VISIBLE);
                                            holder.removed.setVisibility(View.VISIBLE);
                                            removeItem(position);
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Something wrong !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Keep it", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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
    private ProgressDialog displayDialog(String messsage,String title){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(messsage);
        return progressDialog;
    }
    //update arrayList
    private void UpdateData(int position){
         this.presList.get(position).setStatus("Canceled");
         notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.presList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView p_name , upload_date,p_status;
        Button viewdata,delete,removed;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            p_name = itemView.findViewById(R.id.phamr_name);
            p_status = itemView.findViewById(R.id.status);
            viewdata = itemView.findViewById(R.id.view_pres);
            upload_date = itemView.findViewById(R.id.upload_date);
            delete = itemView.findViewById(R.id.pres_delete);
            removed = itemView.findViewById(R.id.remove);

        }
    }
}
