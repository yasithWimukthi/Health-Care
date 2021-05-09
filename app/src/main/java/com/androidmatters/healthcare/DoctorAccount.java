package com.androidmatters.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidmatters.healthcare.Model.Doctor;
import com.androidmatters.healthcare.UI.DatePickerFragment;
import com.androidmatters.healthcare.util.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorAccount extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ImageView profilePicture;
    private ImageView imagePicker;
    private EditText appointmentDate;
    private Button viewAppointments;
    private Button updateProfile;
    private Button logout;
    private Button deleteProfile;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private CollectionReference doctorsCollection = db.collection("doctors");
    FirebaseUser user ;

    private Doctor currentLoggedDoctor ;
    String userId ="";
    String email = "";
    String dpPath = "";

    private static final int GALLERY_CODE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor_account);

        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL_TO_DOCTOR");
        userId = intent.getStringExtra("USER_ID_TO_DOCTOR");
        dpPath = intent.getStringExtra("DP");

        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG);

        profilePicture = findViewById(R.id.doctor_profile_img);
        imagePicker = findViewById(R.id.doctor_image_picker);
        appointmentDate = findViewById(R.id.appointmentDate);
        viewAppointments = findViewById(R.id.viewAppointmentBtn);
        updateProfile = findViewById(R.id.updateProfileBtn);
        logout = findViewById(R.id.logoutBtn);
        deleteProfile = findViewById(R.id.deleteProfileBtn);
        currentLoggedDoctor = new Doctor();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(!dpPath.equals("not added") ){
            getProfilePicture(dpPath);
        }

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ALERT MESSAGE
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        DoctorAccount.this);
                alertDialog2.setTitle("Confirm Update...");

                alertDialog2.setMessage("Do you want to update profile picture ?");

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                startActivityForResult(galleryIntent,GALLERY_CODE);
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog2.show();

            }
        });

        viewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(appointmentDate.getText().toString().trim())){
                    Intent intent = new Intent(getApplicationContext(), ChannelList.class);
                    intent.putExtra("DOCTOR_EMAIL", email);
                    intent.putExtra("DATE", appointmentDate.getText().toString().trim());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter a date.",Toast.LENGTH_LONG).show();
                }
            }
        });

        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"get date");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.getData();
                profilePicture.setImageURI(imageUri);

                StorageReference filePath = storageReference.child("doctors").child(user.getUid());
                filePath.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(),"Profile picture successfully updated.",Toast.LENGTH_LONG).show();
                                filePath.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                getProfilePicture(uri.toString());

                                                Map<String, String> data = new HashMap<>();
                                                data.put("profilePicture", uri.toString());

                                                doctorsCollection.document(email)
                                                        .set(data, SetOptions.merge());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something went wrong. Try again.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void getProfilePicture(String dpPath){
        Picasso.get()
                .load(dpPath)
                .placeholder(R.drawable.doctor_illustration)
                .fit()
                .into(profilePicture);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String CurrentDay = DateFormat.getDateInstance().format(calendar.getTime());
        appointmentDate.setText(CurrentDay);
    }
}