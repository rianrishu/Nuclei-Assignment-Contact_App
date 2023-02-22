package com.example.contactapplication;

import android.content.Context;

import javax.inject.Inject;

import dagger.Component;

@Component
public interface ContactDataSourceComponent {

    MainActivity mainActivity = null;
    void inject(MainActivity mainActivity);

    @Inject
    default ContactDataSourceImplementation get(Context context) {
        return new ContactDataSourceImplementation(context);
    }
}
