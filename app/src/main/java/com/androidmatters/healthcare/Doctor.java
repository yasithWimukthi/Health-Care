package com.androidmatters.healthcare;



public class Doctor {

    private String hospital;

    private String  mobile;

    private String name;

    private String email;

    private String  specialization;

    private String  userId;

    public Doctor() {

    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHospital() {
        return hospital;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getUserId() {
        return userId;
    }


}
