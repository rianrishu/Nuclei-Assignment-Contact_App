package com.example.contactapplication;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface ContactDataSource {

    Single<Long> saveContact(Contact contact);

    Single<List<Contact>> getContacts();

    Completable updateContact(Contact contact);

    Completable deleteContact(Contact contact);
}

