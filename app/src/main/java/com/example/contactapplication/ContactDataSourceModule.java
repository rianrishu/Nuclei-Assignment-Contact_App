package com.example.contactapplication;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactDataSourceModule {

    @Provides
    ContactDataSourceImplementation providesContactDataSourceImplementation(Context context){
        return new ContactDataSourceImplementation(context);
    }
}
