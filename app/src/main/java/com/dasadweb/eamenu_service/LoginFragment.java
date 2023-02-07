package com.dasadweb.eamenu_service;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends Fragment {
    MyDatabaseHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // click listener (login)
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            try {
                login();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        myDB = new MyDatabaseHelper(getContext());
        myDB.initializeDB();
        // Inflate the layout for this fragment
        return rootView;
    }

    // === Login ===
    public void login() throws InterruptedException {
        // get data from ui
        EditText usernameField = (EditText)getView().findViewById(R.id.editTextTextPersonName);
        String username = usernameField.getText().toString();
        EditText passwordField = (EditText)getView().findViewById(R.id.editTextTextPassword);
        String password = passwordField.getText().toString();
        // login
        Login loginFunc = new Login();
        loginFunc.setParams(username.trim(), password);
        Thread thread = new Thread(loginFunc);
        thread.start();
        thread.join();
        boolean loginBool = loginFunc.getLogin();
        Log.println(Log.DEBUG, "Login", String.valueOf(loginBool));
        if (!loginBool){
            // error unable to login
            TextView tv = getView().findViewById(R.id.textView3);
            tv.setText("Unable to login!");
            Log.println(Log.DEBUG, "Login", "Unable to login");
            return;
        }
        // TODO:
        // save data to db
        myDB.updateUser(username, password);
        TextView tv = getView().findViewById(R.id.textView3);
        tv.setText("Login successful!");
        Log.println(Log.DEBUG, "Login", "Login successful");
    }
}