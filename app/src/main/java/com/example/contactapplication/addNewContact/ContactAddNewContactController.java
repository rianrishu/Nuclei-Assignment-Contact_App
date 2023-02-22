package com.example.contactapplication.addNewContact;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import com.example.contactapplication.Contact;
import com.example.contactapplication.ContactDataSourceComponent;
import com.example.contactapplication.DaggerContactDataSourceComponent;
import com.example.contactapplication.MainActivity;
import com.example.contactapplication.R;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactAddNewContactController extends Controller {

    ContactDataSourceComponent component = DaggerContactDataSourceComponent.builder().build();
    private EditText firstname;
    private EditText lastname;
    private EditText phoneNumber;
    private EditText email;
    private EditText company;
    private ImageView imageView;
    private Button savebtn;
    private TextView addPicture;
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
        addPicture = view.findViewById(R.id.tv_add_picture);
    }

    private void attachListeners() {
        Contact contact = new Contact();

        imageView.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(pickImageIntent, 1);
        });

        savebtn.setOnClickListener(v -> {

            String st_fullname = firstname.getText().toString() + " " + lastname.getText().toString();
            String st_phoneNumber = phoneNumber.getText().toString();
            String st_email = email.getText().toString();
            String st_companyInfo = company.getText().toString();

            if (st_fullname.length() == 1) {
                Toast.makeText(getActivity(), "Name field is empty", Toast.LENGTH_SHORT).show();
            } else if (st_phoneNumber.length() != 10) {
                Toast.makeText(getActivity(), "Phone number invalid", Toast.LENGTH_SHORT).show();
            } else if (!validateEmail(st_email)) {
                Toast.makeText(getActivity(), "Email is invalid", Toast.LENGTH_SHORT).show();
            } else if (st_companyInfo.length() == 0) {
                Toast.makeText(getActivity(), "Company field is empty", Toast.LENGTH_SHORT).show();
            } else {
                contact.setFullName(st_fullname);
                contact.setContactNumber(st_phoneNumber);
                contact.setEmail(st_email);
                contact.setCompanyInformation(st_companyInfo);


                Drawable drawable = imageView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                String bitmapPath = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "title", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                contact.setImage(bitmapUri);

                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, contact.getFullName())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, contact.getContactNumber())
                        .putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail())
                        .putExtra(ContactsContract.Intents.Insert.COMPANY, contact.getCompanyInformation());

                Uri profilePictureUri = contact.getImage();
                String TAG = "profilepic";
                if (profilePictureUri != null) {
                    try {
                        InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(profilePictureUri);
                        Bitmap profilePicture = BitmapFactory.decodeStream(inputStream);
                        if (profilePicture != null) {
                            byte[] byteArray = compressToSize(profilePicture, 200);
                            if (byteArray != null) {
                                contactIntent.putExtra(ContactsContract.CommonDataKinds.Photo.PHOTO, byteArray);
                                Log.d(TAG, "Successfully added photo to contact intent.");
                            } else {
                                Log.e(TAG, "Failed to compress profile picture.");
                            }
                        } else {
                            Log.e(TAG, "Failed to decode profile picture.");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error opening profile picture input stream: " + e.getMessage());
                    }
                } else {
                    Log.d(TAG, "Profile picture is null.");
                }

                startActivityForResult(contactIntent, 2);
            }
        });
    }

    public static byte[] compressToSize(Bitmap bitmap, long targetFileSize) {
        int quality = 100;
        int retryCount = 0;
        byte[] byteArray = null;

        do {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
                quality -= 10;
            } catch (IOException e) {

            }

            if (byteArray.length <= targetFileSize) {
                break;
            }

            retryCount++;
        } while (retryCount < 10);
        return byteArray;
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
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
                addPicture.setVisibility(View.GONE);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled add photo",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "cannot load image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), "Contact has been added.", Toast.LENGTH_SHORT).show();
                getRouter().popCurrentController();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled Added Contact",
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
