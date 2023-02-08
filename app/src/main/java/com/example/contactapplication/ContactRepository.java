package com.example.contactapplication;

import javax.inject.Inject;

public class ContactRepository {
    private final ContactDataSource contactDataSource;

    @Inject
    ContactRepository(ContactDataSource contactDataSource){
        this.contactDataSource = contactDataSource;
    }


}
