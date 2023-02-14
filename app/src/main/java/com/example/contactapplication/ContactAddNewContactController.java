package com.example.contactapplication;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import android.app.Activity;

import java.io.File;

public class ContactAddNewContactController extends Controller {

    ContactDataSourceComponent component = DaggerContactDataSourceComponent.builder().build();
    private EditText firstname;
    private EditText lastname;
    private EditText phoneNumber;
    private EditText email;
    private EditText company;
    private ImageView imageView;
    private Button savebtn;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.activity_add_new_contact, container, false);
        initializeView(view);
        attachListeners();
        return view;
    }

    private void initializeView(View view) {
        firstname = view.findViewById(R.id.et_first_name);
        lastname = view.findViewById(R.id.et_last_name);
        phoneNumber = view.findViewById(R.id.et_contact_number);
        email = view.findViewById(R.id.et_email);
        company = view.findViewById(R.id.et_company);
        imageView = view.findViewById(R.id.profile_pic);
        savebtn = view.findViewById(R.id.btn_save_contact);
    }

    private void attachListeners() {
        Contact contact = new Contact();
        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String st_firstname = firstname.getText().toString();
                contact.setFullName(st_firstname);
            }
        });
        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String st_lastname = lastname.getText().toString();
                String st_firstname = contact.getFullName();
                contact.setFullName(st_firstname + " " + st_lastname);
            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String st_phonenumber = phoneNumber.getText().toString();
                contact.setContactNumber(st_phonenumber);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String st_email = email.getText().toString();
                contact.setEmail(st_email);
            }
        });
        company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String st_company = company.getText().toString();
                contact.setCompanyInformation(st_company);
            }
        });

        imageView.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            Activity activity = (Activity) v.getContext();
            activity.startActivityForResult(pickImageIntent, 1);
        });

        savebtn.setOnClickListener(v -> {
            contact.setImage(Uri.fromFile(new File(String.valueOf(imageView.getTag()))));
            component.get(getApplicationContext()).saveContact(contact);
            Log.d("saved contacts", contact.getContactNumber() + " " + contact.getFullName()
            + " " + contact.getEmail() + " " + contact.getCompanyInformation() + " " + contact.getImage().toString());
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

}
