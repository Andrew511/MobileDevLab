package com.example.wregea63.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    int USERNAME_LENGTH = 4;
    int PASSWORD_LENGTH = 6;


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
        ((Button)findViewById(R.id.loginSubmit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }

    public void onRegistering(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        if (checked) {
            ((Button)findViewById(R.id.loginSubmit)).setText("Register");
            ((Button)findViewById(R.id.loginSubmit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    register();
                }
            });
        }
        else {
            ((Button)findViewById(R.id.loginSubmit)).setText("Log in");
            ((Button)findViewById(R.id.loginSubmit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    login();
                }
            });
        }
    }

    private boolean isRegistering() {
        return ((CheckBox) findViewById(R.id.checkboxRegister)).isChecked();
    }

    public void register() {
        String username = ((EditText)findViewById(R.id.inputLogin)).getText().toString();
        String pass = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
        Intent login = new Intent(getBaseContext(), MainActivity.class);

        //Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,}");
        //Matcher match = pattern.matcher(pass);
        if (username.length() < USERNAME_LENGTH) {
            Toast.makeText(getApplicationContext(), "Username must be 4 characters or more.", Toast.LENGTH_SHORT).show();
        }
        else if (pass.length() < PASSWORD_LENGTH) {
            Toast.makeText(getApplicationContext(), "Password must be 6 characters or more.", Toast.LENGTH_SHORT).show();
        }
        else if (/*!match.matches()/*("pass").matches()*/!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,}")) {
            Toast.makeText(getApplicationContext(), pass, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Password must contain a capital letter, a lowercase letter, and a number.", Toast.LENGTH_SHORT).show();
        }
        else {
            login.putExtra("USERNAME", username);
            startActivity(login);
        }
    }

    public void login() {
        String username = ((EditText)findViewById(R.id.inputLogin)).getText().toString();
        String pass = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
        if (!username.equals("Erik")) {
            Toast.makeText(getApplicationContext(), "Username is incorrect", Toast.LENGTH_SHORT).show();
        }
        else if (!pass.equals("Krohn1")) {
            Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent login = new Intent(getBaseContext(), MainActivity.class);
            login.putExtra("USERNAME", username);
            startActivity(login);
        }
    }
}

