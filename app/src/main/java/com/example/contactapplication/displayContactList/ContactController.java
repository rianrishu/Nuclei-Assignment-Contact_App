package com.example.contactapplication.displayContactList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluelinelabs.conductor.Controller;
import com.example.contactapplication.Contact;
import com.example.contactapplication.ContactDataSourceComponent;
import com.example.contactapplication.DaggerContactDataSourceComponent;
import com.example.contactapplication.R;
import com.example.contactapplication.displayContactList.ContactAdapter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactController extends Controller {

    private List<Contact> contact_list;
    ContactDataSourceComponent component = DaggerContactDataSourceComponent.builder().build();

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.controller_container, container, false);
        return view;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        fetchContacts();
    }

    private void fetchContacts() {
        Single<List<Contact>> contacts = component.get(getApplicationContext()).getContacts();
        disposable.add(
                contacts
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(contact -> {
                            this.contact_list = contact;
                            displayContacts();
                        }, throwable -> {
                            Log.d("Error", "error in fetchContacts");
                        })
        );
    }

    private void displayContacts() {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        ContactAdapter adapter = new ContactAdapter(contact_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}

