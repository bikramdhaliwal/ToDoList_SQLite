package com.example.assignment2_part2;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ToDoTableHandler {
    // Database table
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESIGNATION = "designation";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PROVINCE = "province";
    public static final String COLUMN_COUNRTY = "country";
    public static final String COLUMN_POSTALCODE = "postalcode";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DESIGNATION + " text not null, "
            + COLUMN_FIRSTNAME + " text not null,"
            + COLUMN_LASTNAME + " text not null,"
            + COLUMN_ADDRESS + " text not null,"
            + COLUMN_PROVINCE + " text not null,"
            + COLUMN_COUNRTY + " text not null,"
            + COLUMN_POSTALCODE + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(ToDoTableHandler.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(database);
    }
}

