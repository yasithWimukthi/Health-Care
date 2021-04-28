package com.androidmatters.healthcare;

public class Pharmacy {
    private String Pname;
    private String city;
    private String description;

    public Pharmacy() {
    }

    public Pharmacy(String pname, String city, String description) {
        Pname = pname;
        this.city = city;
        this.description = description;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
