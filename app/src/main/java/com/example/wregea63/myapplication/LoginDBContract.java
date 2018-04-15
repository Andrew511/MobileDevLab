package com.example.wregea63.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Andrew on 4/14/2018.
 */



public final class LoginDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LoginDBContract() {}

    /* Inner class that defines the table contents */
    public static class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "Login";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";



    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LoginEntry.TABLE_NAME + " (" +
                    LoginEntry._ID + " INTEGER PRIMARY KEY," +
                    LoginEntry.COLUMN_NAME_USERNAME + TEXT_TYPE
                    + COMMA_SEP +
                    LoginEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LoginEntry.TABLE_NAME;

    public static class LoginDBHelper extends
            SQLiteOpenHelper {
        //if the schema changes, the version must change
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME =
                "MyTest.db";

        public LoginDBHelper(Context context) {
            super(context, DATABASE_NAME,
                    null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            //onUpgrade, just start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db,
                                int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}


