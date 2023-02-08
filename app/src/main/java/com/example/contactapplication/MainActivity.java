package com.example.contactapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactDataSourceComponent component = DaggerContactDataSourceComponent.builder().build();

        Single<List<Contact>>  contacts = component.get(this).getContacts();

        contacts.subscribe(contacts1 -> {
            for(Contact contact : contacts1){
                Log.d("fullname",contact.getFullName());
                Log.d("number",contact.getContactNumber());
                Log.d("email",contact.getEmail());
                Log.d("company",contact.getCompanyInformation());
                Log.d("image",contact.getImage().toString());
            }
        });
        Log.d("Contacts", component.get(this).getContacts().toString());




    }

}