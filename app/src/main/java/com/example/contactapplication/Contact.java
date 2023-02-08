package com.example.contactapplication;

import android.net.Uri;

import javax.inject.Inject;

public class Contact{
    private String id;
    private String fullName;
    private String contactNumber;
    private String email;
    private String companyInformation;
    private Uri image;

    @Inject
    Contact(){

    }

    Contact(String id, String fullName, String contactNumber, String email, String companyInformation, Uri image) {
        this.id = id;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.companyInformation = companyInformation;
        this.image = image;
    }

    void setId(String id){
        this.id = id;
    }
    void setFullName(String fullName) {
        this.fullName = fullName;
    }

    void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    void setEmail(String email) {
        this.email = email;
    }

    void setCompanyInformation(String companyInformation) {
        this.companyInformation = companyInformation;
    }

    void setImage(Uri image) {
        this.image = image;
    }

    String getFullName(){
        return this.fullName;
    }

    String getContactNumber(){
        return this.contactNumber;
    }

    String getEmail(){
        return this.email;
    }

    String getCompanyInformation(){
        return this.companyInformation;
    }

    Uri getImage(){
        return this.image;
    }

    String getId(){
        return this.id;
    }
}
