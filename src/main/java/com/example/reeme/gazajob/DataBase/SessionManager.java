package com.example.reeme.gazajob.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static String TAG=SessionManager.class.getSimpleName();
    // shared preferences
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    //shared pref mode
    int  PRIVATE_MODE=0;
    // shared preferences file name
    private static final String PREFERENCES_NAME="LoginCompany";
    private static final String PREFS_NAME="loginUser";
    private static final  String KEY_IS_LOGGEDINUser="isLoggedInUser";
    private static final  String KEY_IS_LOGGEDINCompany="isLoggedInCompany";

    //consructor

    public SessionManager(Context context){
         this.context=context;
         preferences=context.getSharedPreferences(PREFERENCES_NAME,PRIVATE_MODE);
         editor=preferences.edit();

     }


     public void logOutUser(){
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    editor = settings.edit();
    editor.remove(KEY_IS_LOGGEDINUser);
    editor.commit();
    }
    public void setLoginUser(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDINUser,isLoggedIn);
        editor.commit();
        Log.d(TAG,"User login session modified!");
     }


    public void setLoginCompany(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDINCompany,isLoggedIn);
        editor.commit();
        Log.d(TAG,"Company login session modified!");
    }

/**
    public void checkLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedIn);
        editor.commit();
        //commit the changes
         Log.d(TAG,"User login session modified!");
     }
*/
     public boolean  isLoggedInUser(){

        return preferences.getBoolean(KEY_IS_LOGGEDINUser,false);
     }
    public boolean  isLoggedInCompany(){

        return preferences.getBoolean(KEY_IS_LOGGEDINCompany,false);
    }
}
