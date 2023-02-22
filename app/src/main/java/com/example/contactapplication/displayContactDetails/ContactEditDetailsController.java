package com.example.contactapplication.displayContactDetails;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;
import com.example.contactapplication.Contact;
import com.example.contactapplication.ContactDataSourceComponent;
import com.example.contactapplication.DaggerContactDataSourceComponent;
import com.example.contactapplication.MainActivity;
import com.example.contactapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ContactEditDetailsController extends Controller {
    ContactDataSourceComponent component = DaggerContactDataSourceComponent.builder().build();
    private EditText name;
    private EditText phoneNumber;
    private EditText email;
    private EditText company;
    private ImageView imageView;
    private Button savebtn;

    private Contact contact;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public ContactEditDetailsController() {

    }

    public ContactEditDetailsController(Contact contact) {
        this.contact = contact;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.activity_edit_contact_details, container, false);
        initializeView(view);
        attachListeners();
        return view;
    }

    private void initializeView(View view) {
        name = view.findViewById(R.id.et_name);
        phoneNumber = view.findViewById(R.id.et_contact_number);
        email = view.findViewById(R.id.et_email);
        company = view.findViewById(R.id.et_company);
        imageView = view.findViewById(R.id.profile_pic);
        savebtn = view.findViewById(R.id.btn_save_contact);
    }

    private void attachListeners() {
        name.setText(contact.getFullName());
        phoneNumber.setText(contact.getContactNumber());
        email.setText(contact.getEmail());
        company.setText(contact.getCompanyInformation());

        savebtn.setOnClickListener(view -> {
//            contact.setFullName(name.getText().toString());
//            contact.setContactNumber(phoneNumber.getText().toString());
//            contact.setEmail(email.getText().toString());
//            contact.setCompanyInformation(company.getText().toString());
//            component.get(getApplicationContext()).updateContact(contact);
//            getRouter().popCurrentController();
            String st_name = name.getText().toString();
            String st_phoneNumber = phoneNumber.getText().toString();
            String st_email = email.getText().toString();
            String st_companyInfo = company.getText().toString();

            if (st_name.length() == 0) {
                Toast.makeText(getActivity(), "Name field is empty", Toast.LENGTH_SHORT).show();
            } else if (st_phoneNumber.length() != 10) {
                Toast.makeText(getActivity(), "Phone number invalid", Toast.LENGTH_SHORT).show();
            } else if (!validateEmail(st_email)) {
                Toast.makeText(getActivity(), "Email is invalid", Toast.LENGTH_SHORT).show();
            } else if (st_companyInfo.length() == 0) {
                Toast.makeText(getActivity(), "Company field is empty", Toast.LENGTH_SHORT).show();
            } else {
                contact.setFullName(st_name);
                contact.setContactNumber(st_phoneNumber);
                contact.setEmail(st_email);
                contact.setCompanyInformation(st_companyInfo);

//                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
//                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//                contactIntent
//                        .putExtra(ContactsContract.Intents.Insert.NAME, contact.getFullName())
//                        .putExtra(ContactsContract.Intents.Insert.PHONE, contact.getContactNumber())
//                        .putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail())
//                        .putExtra(ContactsContract.Intents.Insert.COMPANY, contact.getCompanyInformation());
//                startActivityForResult(contactIntent, 1);

                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contact.getId()));

// Create an Intent to open the contact edit screen
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(contactUri);

// Start the Contacts app to edit the contact
                startActivity(intent);
            }
        });
    }

    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), "Contact has been updated.", Toast.LENGTH_SHORT).show();
                getRouter().popCurrentController();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled update Contact",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
