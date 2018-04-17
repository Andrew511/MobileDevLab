package com.example.wregea63.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    int USERNAME_LENGTH = 4;
    int PASSWORD_LENGTH = 6;
    LoginDBContract.LoginDBHelper loginDBHelper;
    SQLiteDatabase rdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currLay;
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        if(getResources().getConfiguration().orientation==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_login_portrait);
            currLay = R.layout.activity_login_portrait;
        } else{
            setContentView(R.layout.activity_login_portrait);
            currLay = R.layout.activity_login_portrait;
        }
        if (sharedPref.getBoolean("REGISTERING", false)) {
            ((CheckBox) findViewById(R.id.checkboxRegister)).setChecked(true);
            onRegistering(findViewById(R.id.checkboxRegister));
        }
        else
        ((Button)findViewById(R.id.loginSubmit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
        ((TextView)findViewById(R.id.inputLogin)).setText(sharedPref.getString("LOGINUSER", ""));
        ((TextView)findViewById(R.id.inputPassword)).setText(sharedPref.getString("LOGINPASS", ""));


        loginDBHelper = new
                LoginDBContract.LoginDBHelper(getApplicationContext());
        rdb = loginDBHelper.getReadableDatabase();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String username = ((EditText)findViewById(R.id.inputLogin)).getText().toString();
        String pass = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
        editor.putBoolean("REGISTERING", ((CheckBox) findViewById(R.id.checkboxRegister)).isChecked());

        if (username != "") {
            editor.putString("LOGINUSER", username);
        }
        if (pass != "") {
            editor.putString("LOGINPASS", pass);
        }
        editor.commit();
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

        String[] selectionArgs = { username };
        String[] projection = {LoginDBContract.LoginEntry.COLUMN_NAME_USERNAME};

        Cursor cursor = rdb.query(
                LoginDBContract.LoginEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                LoginDBContract.LoginEntry.COLUMN_NAME_USERNAME + " = ?",              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        if (username.length() < USERNAME_LENGTH) {
            Toast.makeText(getApplicationContext(), "Username must be 4 characters or more.", Toast.LENGTH_SHORT).show();
        }
        else if (pass.length() < PASSWORD_LENGTH) {
            Toast.makeText(getApplicationContext(), "Password must be 6 characters or more.", Toast.LENGTH_SHORT).show();
        }
        else if (!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,}")) {
            Toast.makeText(getApplicationContext(), "Password must contain a capital letter, a lowercase letter, and a number.", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.moveToFirst()) //check if cursor is empty, if not, username is taken
        {
            Toast.makeText(getApplicationContext(), "Username is already taken", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = loginDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(LoginDBContract.LoginEntry.COLUMN_NAME_USERNAME,
                    username);
            values.put(LoginDBContract.LoginEntry.COLUMN_NAME_PASSWORD,
                    pass);
            db.insert(
                    LoginDBContract.LoginEntry.TABLE_NAME,
                    null,
                    values);
            login.putExtra("USERNAME", username);
            startActivity(login);
        }
    }

    public void login() {
        String username = ((EditText)findViewById(R.id.inputLogin)).getText().toString();
        String pass = ((EditText)findViewById(R.id.inputPassword)).getText().toString();

        String[] selectionArgs = { username };
        String[] projection = {LoginDBContract.LoginEntry.COLUMN_NAME_USERNAME, LoginDBContract.LoginEntry.COLUMN_NAME_PASSWORD};

        Cursor cursor = rdb.query(
                LoginDBContract.LoginEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                LoginDBContract.LoginEntry.COLUMN_NAME_USERNAME + " = ?",              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (!cursor.moveToFirst()) //check if username exists in db
        {
            Toast.makeText(getApplicationContext(), "Account with Username " + username + " does not exist.", Toast.LENGTH_SHORT).show();
        }
        else if (!pass.equals(cursor.getString(cursor.getColumnIndex(LoginDBContract.LoginEntry.COLUMN_NAME_PASSWORD)))) {
            Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent login = new Intent(getBaseContext(), MainActivity.class);
            login.putExtra("USERNAME", username);
            startActivity(login);
        }
    }
}

