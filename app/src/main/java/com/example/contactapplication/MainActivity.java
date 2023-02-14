package com.example.contactapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.contactapplication.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private Router router;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        router = Conductor.attachRouter(this, activityMainBinding.controllerContainer, savedInstanceState)
                .setPopRootControllerMode(Router.PopRootControllerMode.NEVER);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new ContactController()));
        }

        activityMainBinding.getRoot().findViewById(R.id.create_new_contact_section).setOnClickListener(view -> {
            findViewById(R.id.create_new_contact_section).setVisibility(View.GONE);
            router.pushController(RouterTransaction.with(new ContactAddNewContactController()));
        });
    }


    //TODO : Fix problem with restart main activity
    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
            router.getActivity();
        }
    }

}