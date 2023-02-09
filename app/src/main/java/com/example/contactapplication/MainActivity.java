package com.example.contactapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.example.contactapplication.databinding.ActivityMainBinding;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Router router;
    private MainActivity mainActivity;

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

        findViewById(R.id.create_new_contact_section).setOnClickListener(view -> {
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