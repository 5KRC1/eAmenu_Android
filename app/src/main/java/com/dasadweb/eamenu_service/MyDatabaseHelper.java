package com.dasadweb.eamenu_service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user_info";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USER = "username";
    private static final String COLUMN_PASS = "password";
    private static final String COLUMN_PREF_MEAL = "prefered_meal";
    private static final String COLUMN_DISLIKED_FOODS = "disliked_foods";
    private static final String COLUMN_DEF_MEAL = "default_meal";
    private static final String COLUMN_FAVOURITE_FOODS = "favourite_foods";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER + " TEXT, " +
                COLUMN_PASS + " TEXT, " +
                COLUMN_PREF_MEAL + " INTEGER, " +
                COLUMN_DISLIKED_FOODS + " TEXT, " +
                COLUMN_DEF_MEAL + " INTEGER, " +
                COLUMN_FAVOURITE_FOODS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void initializeDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);

        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            cv.put(COLUMN_USER, "");
            cv.put(COLUMN_PASS, "");
            cv.put(COLUMN_PREF_MEAL, 1);
            cv.put(COLUMN_DISLIKED_FOODS, "");
            cv.put(COLUMN_DEF_MEAL, 1);
            cv.put(COLUMN_FAVOURITE_FOODS, "");
            long result = db.insert(TABLE_NAME, null, cv);
            if  (result == -1) {
                Toast.makeText(context, "Error uploading to db", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Success uploading to db", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void updatePrefMeal(int prefMeal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PREF_MEAL, prefMeal);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {"1"});
        if  (result == -1) {
            Toast.makeText(context, "Error updating db", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success updating db", Toast.LENGTH_SHORT).show();
        }
    }

    void updateDefMeal(int defMeal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DEF_MEAL, defMeal);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {"1"});
        if  (result == -1) {
            Toast.makeText(context, "Error updating db", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success updating db", Toast.LENGTH_SHORT).show();
        }
    }

    void updateDislikedFoods(String dislikedFoods){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DISLIKED_FOODS, dislikedFoods);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {"1"});
        if  (result == -1) {
            Toast.makeText(context, "Error updating db", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success updating db", Toast.LENGTH_SHORT).show();
        }
    }

    void updateFavouriteFoods(String favouriteFoods){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FAVOURITE_FOODS, favouriteFoods);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {"1"});
        if  (result == -1) {
            Toast.makeText(context, "Error updating db", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success updating db", Toast.LENGTH_SHORT).show();
        }
    }

    void updateUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER, username);
        cv.put(COLUMN_PASS, password);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {"1"});
        if  (result == -1) {
            Toast.makeText(context, "Error updating db", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success updating db", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
