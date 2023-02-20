package com.example.contactapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.contactapplication.addNewContact.ContactAddNewContactController;
import com.example.contactapplication.databinding.ActivityMainBinding;
import com.example.contactapplication.displayContactList.ContactController;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Router router;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        requestPermissions(activityMainBinding, savedInstanceState);
    }

    private void getContacts(ActivityMainBinding activityMainBinding, Bundle savedInstanceState){
        router = Conductor.attachRouter(this, activityMainBinding.controllerContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new ContactController()));
        }
    }



    private void requestPermissions(ActivityMainBinding activityMainBinding, Bundle savedInstanceState) {
        Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.READ_CONTACTS,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.WRITE_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            getContacts(activityMainBinding, savedInstanceState);

                            activityMainBinding.getRoot().findViewById(R.id.create_new_contact_section).setOnClickListener(view -> {
                                findViewById(R.id.create_new_contact_section).setVisibility(View.GONE);
                                router.pushController(RouterTransaction.with(new ContactAddNewContactController()));
                            });
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                .onSameThread().check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Need Permissions");

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    public interface OnBackPressedListener{
        public void onBackPressed();
    }



}