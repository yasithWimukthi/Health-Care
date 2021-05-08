package com.androidmatters.healthcare;

public class AppointmnetDetails {


    private String date;

    private String  doctorId;

    private String doctorName;

    private String name;

    private long  number;

    private String  patientId;


    public AppointmnetDetails() {
    }

    public AppointmnetDetails(String date, String doctorId, String doctorName, String name, long number, String patientId) {
        this.date = date;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.name = name;
        this.number = number;
        this.patientId = patientId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDate() {
        return date;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getName() {
        return name;
    }

    public long getNumber() {
        return number;
    }

    public String getPatientId() {
        return patientId;
    }
}
