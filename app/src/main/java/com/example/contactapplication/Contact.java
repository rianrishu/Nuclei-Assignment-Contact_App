package com.example.contactapplication;

import android.net.Uri;

import javax.inject.Inject;

public class Contact{
    private String id = null;
    private String fullName = null;
    private String contactNumber = null;
    private String email = null;
    private String companyInformation = null;
    private Uri image = null;

    @Inject
    public Contact(){

    }

    Contact(String id, String fullName, String contactNumber, String email, String companyInformation, Uri image) {
        this.id = id;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.companyInformation = companyInformation;
        this.image = image;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompanyInformation(String companyInformation) {
        this.companyInformation = companyInformation;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getFullName(){
        return this.fullName;
    }

    public String getContactNumber(){
        return this.contactNumber;
    }

    public String getEmail(){
        return this.email;
    }

    public String getCompanyInformation(){
        return this.companyInformation;
    }

    public Uri getImage(){
        return this.image;
    }

    public String getId(){
        return this.id;
    }
}
