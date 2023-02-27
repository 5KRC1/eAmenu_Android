package com.dasadweb.eamenu_service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;

public class DestinationActivity extends IntentService {
    public DestinationActivity() {
        super("DestinationActivity");
    }

    MyDatabaseHelper myDB;
    Context context;
    String username;
    String password;
    String prefMeal;
    String dislikedFoods;
    String defMeal;
    String favouriteFoods;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // get username, password from db
        context = getApplicationContext();
        myDB = new MyDatabaseHelper(context);
        getUser();
        // make request to service api
        HttpClient client = HttpClientBuilder.create().build();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost post = new HttpPost("https://eamenu.dasadweb.com/api/run_service");
        post.setEntity(new StringEntity(""));
        post.setHeader("username", username);
        post.setHeader("password", password);
        post.setHeader("disliked-foods", dislikedFoods);
        post.setHeader("preferred-menu", prefMeal);
        post.setHeader("favourite-foods", favouriteFoods);
        post.setHeader("default-menu", defMeal);
        try {
            client.execute(post, httpContext);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
    void getUser(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                username = cursor.getString(1);
                password = cursor.getString(2);
                prefMeal = cursor.getString(3);
                dislikedFoods = cursor.getString(4);
                defMeal = cursor.getString(5);
                favouriteFoods = cursor.getString(6);
            }
        }
    }
}
