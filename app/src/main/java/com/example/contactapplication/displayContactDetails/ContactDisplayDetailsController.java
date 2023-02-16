package com.example.contactapplication.displayContactDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;
import com.example.contactapplication.Contact;
import com.example.contactapplication.ContactDataSourceComponent;
import com.example.contactapplication.DaggerContactDataSourceComponent;
import com.example.contactapplication.R;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ContactDisplayDetailsController extends Controller {


    ContactDataSourceComponent component = DaggerContactDataSourceComponent.builder().build();
    private TextView name;
    private TextView phoneNumber;
    private TextView email;
    private TextView company;
    private ImageView imageView;
    private Button editbtn;
    private Button deletebtn;

    private Contact contact;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public ContactDisplayDetailsController() {

    }

    public ContactDisplayDetailsController(Contact contact) {
        this.contact = contact;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.activity_contact_details, container, false);
        initializeView(view);
        attachListeners();
        return view;
    }

    private void initializeView(View view) {
        name = view.findViewById(R.id.tv_name);
        phoneNumber = view.findViewById(R.id.tv_contact_number);
        email = view.findViewById(R.id.tv_email);
        company = view.findViewById(R.id.tv_company);
        imageView = view.findViewById(R.id.profile_pic);
        editbtn = view.findViewById(R.id.btn_edit_contact);
        deletebtn = view.findViewById(R.id.btn_delete_contact);
    }

    private void attachListeners() {
        name.setText(contact.getFullName());
        phoneNumber.setText(contact.getContactNumber());
        email.setText(contact.getEmail());
        company.setText(contact.getCompanyInformation());
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

}
