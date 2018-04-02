package com.example.wregea63.myapplication;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currLay;
        if(getResources().getConfiguration().orientation==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_login_portrait);
            currLay = R.layout.activity_login_portrait;
        } else{
            setContentView(R.layout.activity_login_portrait);
            currLay = R.layout.activity_login_portrait;
        }
    }
}

