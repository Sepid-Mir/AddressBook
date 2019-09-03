package com.example.address_book;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class MyAddrTableHandler  {

    // Database table
    public static final String TABLE_ADDR= "addrtable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_FNAME = "firstname";
    public static final String COLUMN_LNAME= "lastname";
    public static final String COLUMN_ADDRESS= "addressline";
    public static final String COLUMN_PROVINCE= "province";
    public static final String COLUMN_COUNTRY= "country";
    public static final String COLUMN_POSTCODE= "postcode";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ADDR
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_FNAME + " text not null,"
            + COLUMN_LNAME + " text not null,"
            + COLUMN_ADDRESS + " text not null,"
            + COLUMN_PROVINCE + " text not null,"
            + COLUMN_COUNTRY + " text not null,"
            + COLUMN_POSTCODE + " text not null"
            + ");";



    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MyAddrTableHandler.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDR);
        onCreate(database);
    }


}
