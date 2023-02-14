package com.example.contactapplication;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class ContactDataSourceImplementation implements ContactDataSource {
    private ContentResolver contentResolver;

    @Inject
    public ContactDataSourceImplementation(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    @Override
    public Single<Long> saveContact(Contact contact) {

        long contactId = 22;
        try {

            ContentValues contentValues = new ContentValues();
            Uri uri = contentResolver.insert(ContactsContract.Contacts.CONTENT_URI, contentValues);
            contactId = ContentUris.parseId(uri);
            contentValues.put(ContactsContract.Contacts.DISPLAY_NAME, contact.getFullName());
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);

            //Adding the phone number
//            contentValues.clear();
//            contentValues.put(ContactsContract.CommonDataKinds.Phone.CONTACT_ID, contactId);
//            contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getContactNumber());
//            contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
//            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
//
//            //Adding email
//            contentValues.clear();
//            contentValues.put(ContactsContract.CommonDataKinds.Email.CONTACT_ID, contactId);
//            contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, contact.getEmail());
//            contentValues.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
//            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
//
//            //Adding company information
//            contentValues.clear();
//            contentValues.put(ContactsContract.CommonDataKinds.Organization.CONTACT_ID, contactId);
//            contentValues.put(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.getCompanyInformation());
//            contentValues.put(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK);
//            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);

            //        if (contact.getImage() != null) {
            //            contentValues.clear();
            //            contentValues.put(ContactsContract.CommonDataKinds.Photo.CONTACT_ID, contactId);
            //            contentValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, getByteArrayFromUri(contact.getImage()));
            //            contentValues.put(ContactsContract.CommonDataKinds.Photo.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            //            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues);
            //        }
        } catch (UnsupportedOperationException e) {
            // Handle the exception by logging the error message
            Log.e("error in saving contact", e.getMessage());
        }
        return Single.just(contactId);
    }

    private byte[] getByteArrayFromUri(Uri uri) {
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressLint("Range")
    @Override
    public Single<List<Contact>> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phone = "";
                String email = "";
                String company = "";
                byte[] image = null;
                Contact contact = new Contact();
                contact.setFullName(name);

                Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        new String[]{id}, null);

                if (phoneCursor != null && phoneCursor.getCount() > 0) {
                    while (phoneCursor.moveToNext()) {
                        phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setContactNumber(phone);
                    }
                    phoneCursor.close();
                }

                Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                        new String[]{id}, null);

                if (emailCursor != null && emailCursor.getCount() > 0) {
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                        contact.setEmail(email);
                    }
                    emailCursor.close();
                }

                Cursor companyCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                        new String[]{id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE},
                        null);

                if (companyCursor != null && companyCursor.getCount() > 0) {
                    while (companyCursor.moveToNext()) {
                        company = companyCursor.getString(companyCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                        contact.setCompanyInformation(company);
                    }
                    companyCursor.close();
                }

                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
                Uri photo = Uri.withAppendedPath(uri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                if (photo == null) {
                    // profile picture not set
                } else {
                    // profile picture set
                    contact.setImage(photo);
                }
                contacts.add(contact);
            }
        }
        return Single.just(contacts);
    }

    @Override
    public Completable updateContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.DISPLAY_NAME, contact.getFullName());

        String where = ContactsContract.Contacts._ID + " = ?";
        String[] args = {contact.getId()};

        int result = contentResolver.update(ContactsContract.Contacts.CONTENT_URI, values, where, args);
        return Completable.complete();
    }

    @Override
    public Completable deleteContact(Contact contact) {
        String where = ContactsContract.Contacts._ID + " = ?";
        String[] args = {contact.getId()};

        int result = contentResolver.delete(ContactsContract.Contacts.CONTENT_URI, where, args);
        return Completable.complete();
    }
}
