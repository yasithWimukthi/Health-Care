//package com.androidmatters.healthcare;
//
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Objects;
//
//public class AppointmentCreate extends AppCompatActivity {
//    private final Calendar myDate = Calendar.getInstance();
//    private EditText dateText;
//    private TextView docName;
//    private TextView patName;
//    private String doctorID,dat,name;
//    private FirebaseFirestore db;
//    private boolean lock,lock2;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_appointment_create);
//
//        //get doctor data from intent
//        doctorID = getIntent().getStringExtra("doctor_ID");
//        String doctorName = getIntent().getStringExtra("doctor_Name");
//        TextView back = findViewById(R.id.backText2);
//        docName = findViewById(R.id.DocName);
//        patName = findViewById(R.id.PatName);
//        dateText =  findViewById(R.id.AppDate);
//        Button savBtn = findViewById(R.id.SaveBtn);
//
//        //set doctor name in text view
//        docName.setText(doctorName);
//
//        // create map for insert
//        Map<String, Object> user_data = new HashMap<>();
//
//        //get patient email
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        String email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
//
//        db = FirebaseFirestore.getInstance();
//
//        //get patient name
//        Query query =  db.collection("patients").whereEqualTo("email",email);
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                    name = document.getString("firstName")+" "+document.getString("lastName");
//                    patName.setText(name);
//                }
//            }
//        });
//
//        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
//
//            myDate.set(Calendar.YEAR,year);
//            myDate.set(Calendar.MONTH, monthOfYear);
//            myDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            dat =  updateDate();
//
//            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            LocalDate now = LocalDate.now();
//            String today = dtf.format(now);
//            now = LocalDate.parse(today, dtf);
//            LocalDate test = LocalDate.parse(dat, dtf);
//            if(now.isBefore(test))
//            {
//            //check patient already exists
//            Query query1 =  db.collection("appointment").whereEqualTo("date", dat).whereEqualTo("doctorId",doctorID).whereEqualTo("patientId",email);
//            query1.get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    if(task.getResult().isEmpty())
//                    {
//                        lock = false;
//                        lock2 = false;
//                        //check patient number for appointment
//                        Query query2 =  db.collection("appointment").whereEqualTo("date", dat).whereEqualTo("doctorId",doctorID).orderBy("number", Query.Direction.DESCENDING).limit(1);
//                        query2.get().addOnCompleteListener(task1 -> {
//
//
//
//                            if (task1.isSuccessful()) {
//                                 //set data if no appointment
//                                if(task1.getResult().isEmpty())
//                                {
//                                    user_data.put("date",dat);
//                                    user_data.put("doctorId",doctorID);
//                                    user_data.put("doctorName", docName.getText().toString().trim());
//                                    user_data.put("name",name);
//                                    user_data.put("patientId",email);
//                                    user_data.put("number",1);
//                                }
//                                else
//                                {//set data  when there are  appointments
//                                    for (QueryDocumentSnapshot document : task1.getResult()) {
//
//                                        if(document.getLong("number") !=null)
//                                        {
//                                            if(document.getLong("number")<20)
//                                            {   long a = (document.getLong("number"));
//                                                user_data.put("date",dat);
//                                                user_data.put("doctorId",doctorID);
//                                                user_data.put("doctorName", docName.getText().toString().trim());
//                                                user_data.put("name", name);
//                                                user_data.put("patientId", email);
//                                                user_data.put("number",a+1);
//                                            }
//                                            else
//                                            {
//                                                lock2 = true;
//                                            }
//
//                                        }
//
//                                    }
//                                }
//
//                            }
//
//                        });
//                    }
//                    else
//                    {
//                        AlertDialog alertDialog = new AlertDialog.Builder(AppointmentCreate.this).create();
//                        alertDialog.setTitle("\uD83D\uDEAB");
//                        alertDialog.setMessage("Already Created an Appointment");
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                (dialog, which) -> {
//                                    lock = true;
//                                    user_data.clear();
//                                    dateText.setText("");
//                                    dialog.dismiss();
//                                });
//                        alertDialog.show();
//                    }
//                    }
//
//                });}
//
//            else
//            {
//                AlertDialog alertDialog = new AlertDialog.Builder(AppointmentCreate.this).create();
//                alertDialog.setTitle("\uD83D\uDEAB");
//                alertDialog.setMessage("Cannot choice old dates");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        (dialog, which) ->{
//                            dateText.setText("");
//                            lock = true;
//                            dialog.dismiss();
//                        });
//                alertDialog.show();
//
//            }
//
//        };
//
//        dateText.setOnClickListener(v -> new DatePickerDialog(AppointmentCreate.this, date, myDate
//                .get(Calendar.YEAR), myDate.get(Calendar.MONTH),
//                myDate.get(Calendar.DAY_OF_MONTH)).show());
//
//        // on button click insert data
//        savBtn.setOnClickListener(v -> {
//
//            AlertDialog alertDialog = new AlertDialog.Builder(AppointmentCreate.this).create();
//            if(!user_data.isEmpty() && !lock && !lock2)
//            {
//                alertDialog.setTitle("Confirm");
//                alertDialog.setMessage("Confirm Appointment Creation");
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                        (dialog, which) -> {
//                            DocumentReference ref=db.collection("appointment").document();
//                            ref.set(user_data);
//
//                            AlertDialog alertDialog1 = new AlertDialog.Builder(AppointmentCreate.this).create();
//                            alertDialog1.setMessage("Go to Home Page");
//                            alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                                    (dialog1, which1) -> {
//                                        Intent n = new Intent( AppointmentCreate.this,home.class);
//                                        startActivity(n);
//                                    });
//                            alertDialog1.show();
//                        });
//                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
//                        (dialog, which) -> {
//                            dateText.setText("");
//                            lock = true;
//                            dialog.dismiss();
//                        });
//            }
//
//            else
//            {
//                if(lock2)
//                {
//                    alertDialog.setTitle("\uD83D\uDEAB");
//                    alertDialog.setMessage("Daily Appointment Limit Reached");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            (dialog, which) -> dialog.dismiss());
//                }
//                else
//                {
//                    alertDialog.setTitle("\uD83D\uDEAB");
//                    alertDialog.setMessage("Date Cannot be empty");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            (dialog, which) -> dialog.dismiss());
//                }
//
//            }
//            alertDialog.show();
//        });
//
//        //back button
//        back.setOnClickListener(v -> {
//            Intent n = new Intent( AppointmentCreate.this,SelectDoctorAppointment.class);
//            startActivity(n);
//        });
//
//    }
//
//    private String updateDate() {
//        String myFormat = "dd-MM-yyyy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
//        dateText.setText(sdf.format(myDate.getTime()));
//        return sdf.format(myDate.getTime());
//    }
//
//}