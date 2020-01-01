package com.example.myfirstgameapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class UserManager {

    // private  Context context;
    private Activity activity;
    private ArrayList<User> users;
    private String jsn;
    private SharedPreferences mPrefs;
    private Gson gson;


    public UserManager(Activity activity) {
        this.mPrefs = activity.getSharedPreferences(Consts.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.activity = activity;
        gson = new Gson();
        loadData();
    }


    public ArrayList getRecords() {
        return users;
    }


    public void addUser(User user) {
        users.add(user);
        Collections.sort(users);
        while (users.size() > Consts.ARRAY_MAX_SIZE) {
            users.remove(users.size() - 1);
        }
        saveData();
    }
    private void saveData() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        jsn = new Gson().toJson(users);
        prefsEditor.putString(Consts.USER_DETAILS, jsn);
        prefsEditor.commit();
    }

    private void loadData() {
        jsn = mPrefs.getString(Consts.USER_DETAILS, null);
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        users = gson.fromJson(jsn, type);

        if (users == null) {
            users = new ArrayList<>();
        }
    }

    public int getLastPlace() {
        if (users.size() > 0)
            return users.get(users.size() - 1).getScore();
        else
            return -1;
    }

}
