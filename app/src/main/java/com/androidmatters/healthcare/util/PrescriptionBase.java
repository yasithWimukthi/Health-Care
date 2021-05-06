package com.androidmatters.healthcare.util;

import android.net.Uri;


public class PrescriptionBase {
    private String Username;
    private String uid;
    private String city;
    private String postalCode;
    private String address;
    private String phone;
    private String uploadedDate;
    private String Pharmacy_name;
    private String pres_image;
    private String documentId;
    private String status;
    private static PrescriptionBase prescriptionBase;

    private PrescriptionBase(){}

    public static PrescriptionBase getInstaceBase(){
        if(prescriptionBase == null){
            prescriptionBase = new PrescriptionBase();
            return prescriptionBase;
        }
        return prescriptionBase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getPharmacy_name() {
        return Pharmacy_name;
    }

    public void setPharmacy_name(String pharmacy_name) {
        Pharmacy_name = pharmacy_name;
    }

    public String getPres_image() {
        return pres_image;
    }

    public void setPres_image(String pres_image) {
        this.pres_image = pres_image;
    }
}
