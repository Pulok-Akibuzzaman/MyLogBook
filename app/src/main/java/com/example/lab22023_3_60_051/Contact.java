package com.example.lab22023_3_60_051;

public class Contact {
    String id;
    String name;
    String email;
    String phone;
    String dob;
    String presentAddress;
    String permanentAddress;
    String imageUri;

    public Contact(String id, String name, String email, String phone, String dob, String presentAddress, String permanentAddress, String imageUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.presentAddress = presentAddress;
        this.permanentAddress = permanentAddress;
        this.imageUri = imageUri;
    }
}
