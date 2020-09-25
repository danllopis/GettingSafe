package com.dllopis.gettingsafe.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Preferencias {
    private static Preferencias preferences;
    private static Context myContext;

    private SharedPreferences prefs;

    private Preferencias(Context context){
        myContext = context;
    }

    public static Preferencias init(Context context){
        if(preferences == null)
            preferences = new Preferencias(context);
        return preferences;
    }

    public void initPrefs(){
        prefs = myContext.getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

    public ArrayList<String> getUserData(){
        String userData = prefs.getString("UserData", null);
        return new Gson().fromJson(userData, new TypeToken<List<String>>(){}.getType());
    }

    public void setUserData(ArrayList<String> userData){
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        SharedPreferences.Editor editorUserData = prefs.edit();
        editorUserData.putString("UserData",json);
        editorUserData.apply();
    }
}
