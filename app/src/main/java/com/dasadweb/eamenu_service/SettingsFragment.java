package com.dasadweb.eamenu_service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    MyDatabaseHelper myDB;
    int prefMeal;
    int defMeal;
    Context context;
    String dislikedFoods;
    String favouriteFoods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        prefMeal = 1;
        defMeal = 1;
        myDB = new MyDatabaseHelper(context);
        // click listener (add disliked foods)
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Button dislikedFoodsButton = (Button) rootView.findViewById(R.id.dislikedFoodsButton);
        dislikedFoodsButton.setOnClickListener(v -> {
            Log.println(Log.DEBUG, "Settings button", "clicked");
            // get disliked food
            EditText dislikedFoodField = (EditText)getView().findViewById(R.id.dislikedFoodsText);
            String dislikedFood = dislikedFoodField.getText().toString();

            if (dislikedFood.trim().isEmpty()) {
                Log.println(Log.DEBUG, "Settings", "Unable to add disliked food");
                return;
            }

            // store to db
            addDislikedFood(dislikedFood.trim());
            Log.println(Log.DEBUG, "Settings", "Disliked food added");
        });

        // click listener (add favourite foods)
        Button favouriteFoodsButton = (Button) rootView.findViewById(R.id.favouriteFoodsButton);
        favouriteFoodsButton.setOnClickListener(v -> {
            Log.println(Log.DEBUG, "Settings button", "clicked");
            // get disliked food
            EditText favouriteFoodField = (EditText)getView().findViewById(R.id.favouriteFoodsText);
            String favouriteFood = favouriteFoodField.getText().toString();

            if (favouriteFood.trim().isEmpty()) {
                Log.println(Log.DEBUG, "Settings", "Unable to add favourite food");
                return;
            }

            // store to db
            addFavouriteFood(favouriteFood.trim());
            Log.println(Log.DEBUG, "Settings", "Favourite food added");
        });

        Switch serviceSwitch = (Switch) rootView.findViewById(R.id.serviceSwitch);
        serviceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            if (isChecked) {
                // if switch is checked.
                // Start Alarm Service
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 10);  // initial delay

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                // if switch is unchecked.
                // Cancel Alarm Service
                alarmManager.cancel(pendingIntent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //your initial code is here.
        getPrefMeal();
        getDefMeal();
        getDislikedFoods();
        createDislikedFoods();
        createFavouriteFoods();
    }
    void getPrefMeal(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Log.println(Log.DEBUG, "PrefMeal", cursor.getString(3));
                prefMeal = Integer.parseInt(cursor.getString(3));
            }
        }
        createCards(prefMeal);
    }

    void getDefMeal(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Log.println(Log.DEBUG, "DefMeal", cursor.getString(5));
                defMeal = Integer.parseInt(cursor.getString(5));
            }
        }
        createCards1(defMeal);
    }

    public void createDislikedFoods(){
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.dislikedFoods);
        Gson gson = new Gson();
        String[] array = gson.fromJson(dislikedFoods, String[].class);
        if (!(array == null)) {
            for (String a : array) {
                Button button = new Button(context);
                button.setText(a);
                button.setOnClickListener(view -> {
                    // remove disliked food
                    linearLayout.removeView(button);

                    // remove from db
                    Gson gson1 = new Gson();
                    ArrayList<String> array1 = gson1.fromJson(dislikedFoods, ArrayList.class);
                    array1.remove(a);
                    String newDislikedFoods = gson1.toJson(array1);
                    myDB.updateDislikedFoods(newDislikedFoods);
                });
                linearLayout.addView(button);
            }
        }
    }
    public void createFavouriteFoods(){
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.favouriteFoods);
        Gson gson = new Gson();
        String[] array = gson.fromJson(favouriteFoods, String[].class);
        if (!(array == null)) {
            for (String a : array) {
                Button button = new Button(context);
                button.setText(a);
                button.setOnClickListener(view -> {
                    // remove favourite food
                    linearLayout.removeView(button);

                    // remove from db
                    Gson gson1 = new Gson();
                    ArrayList<String> array1 = gson1.fromJson(favouriteFoods, ArrayList.class);
                    array1.remove(a);
                    String newFavouriteFoods = gson1.toJson(array1);
                    myDB.updateFavouriteFoods(newFavouriteFoods);
                });
                linearLayout.addView(button);
            }
        }
    }
    void getDislikedFoods(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                dislikedFoods = cursor.getString(4);
            }
//            createDislikedFoods();
        }
    }

    void getFavouriteFoods(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                favouriteFoods = cursor.getString(6);
            }
//            createFavouriteFoods();
        }
    }

    // === Settings ===
    // disliked food
//    String dislikedFoods;
    public void addDislikedFood(String food) {
        getDislikedFoods();
        Context context = getContext();
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.dislikedFoods);

        Button button = new Button(context);
        button.setText(food);
        button.setOnClickListener(view -> {
            // remove disliked food
            linearLayout.removeView(button);

            // remove from db
            Gson gson = new Gson();
            ArrayList<String> array = gson.fromJson(dislikedFoods, ArrayList.class);
            array.remove(food);
            String newDislikedFoods = gson.toJson(array);
            myDB.updateDislikedFoods(newDislikedFoods);
        });
        // add to db
        Gson gson = new Gson();
        ArrayList<String> array = gson.fromJson(dislikedFoods, ArrayList.class);
        if (array == null) {
            array = new ArrayList<>();
        }
        array.add(food);
        String newDislikedFoods = gson.toJson(array);
        myDB.updateDislikedFoods(newDislikedFoods);
        linearLayout.addView(button);
    }

    // favourite food
//    String favouriteFoods;
    public void addFavouriteFood(String food) {
        getFavouriteFoods();
        Context context = getContext();
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.favouriteFoods);

        Button button = new Button(context);
        button.setText(food);
        button.setOnClickListener(view -> {
            // remove disliked food
            linearLayout.removeView(button);

            // remove from db
            Gson gson = new Gson();
            ArrayList<String> array = gson.fromJson(favouriteFoods, ArrayList.class);
            array.remove(food);
            String newFavouriteFoods = gson.toJson(array);
            myDB.updateFavouriteFoods(newFavouriteFoods);
        });
        // add to db
        Gson gson = new Gson();
        ArrayList<String> array = gson.fromJson(favouriteFoods, ArrayList.class);
        if (array == null) {
            array = new ArrayList<>();
        }
        array.add(food);
        String newFavouriteFoods = gson.toJson(array);
        myDB.updateFavouriteFoods(newFavouriteFoods);
        linearLayout.addView(button);
    }

    @SuppressLint("SetTextI18n")
    public void createCards(int prefMeal){
        Context context = getActivity();
        LinearLayout cardLayout = (LinearLayout) getView().findViewById(R.id.cardLayout);
        cardLayout.removeAllViews();
        // get pref meal from db => color the one where i == pref meal
        int[] meals = {1, 2, 3, 4, 5, 6};
        for (int i = 0; i < 6; i++){
            int meal = meals[i];

            CardView newCard = new CardView(Objects.requireNonNull(context));
            LinearLayout newLinearLayout = new LinearLayout(context);
            ImageView newImageView = new ImageView(context);
            TextView newTextView = new TextView(context);
            newTextView.setText("meal " + meal);
            newLinearLayout.addView(newTextView);
            newLinearLayout.addView(newImageView);
            newCard.addView(newLinearLayout);
            newCard.setMinimumHeight(250);
            newCard.setMinimumWidth(250);
            newCard.setOnClickListener(view -> {
                // change in db
                myDB.updatePrefMeal(meal);
                // reload cards
                createCards(meal);
            });

            if (i == prefMeal - 1) {
                newCard.setBackgroundColor(Color.parseColor("#ffa500"));
            }
            cardLayout.addView(newCard);
        }
    }

    @SuppressLint("SetTextI18n")
    public void createCards1(int defMeal){
        Context context = getActivity();
        LinearLayout cardLayout = (LinearLayout) getView().findViewById(R.id.cardLayout1);
        cardLayout.removeAllViews();
        // get pref meal from db => color the one where i == pref meal
        int[] meals = {1, 2, 3, 4, 5, 6};
        for (int i = 0; i < 6; i++){
            int meal = meals[i];

            CardView newCard = new CardView(Objects.requireNonNull(context));
            LinearLayout newLinearLayout = new LinearLayout(context);
            ImageView newImageView = new ImageView(context);
            TextView newTextView = new TextView(context);
            newTextView.setText("meal " + meal);
            newLinearLayout.addView(newTextView);
            newLinearLayout.addView(newImageView);
            newCard.addView(newLinearLayout);
            newCard.setMinimumHeight(250);
            newCard.setMinimumWidth(250);
            newCard.setOnClickListener(view -> {
                // change in db
                myDB.updateDefMeal(meal);
                // reload cards
                createCards1(meal);
            });

            if (i == defMeal - 1) {
                newCard.setBackgroundColor(Color.parseColor("#ffa500"));
            }
            cardLayout.addView(newCard);
        }
    }
}