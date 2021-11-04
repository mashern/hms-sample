package za.org.famba.exampleapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static String TAG = "SessionManager";
    private static SessionManager sInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;

    private SessionManager(Context context) {
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static synchronized SessionManager getInstance(Context context) {
        if(sInstance == null) sInstance = new SessionManager(context);
        return sInstance;
    }

    public static boolean exist(String key) {
        try {
            if (key != null && sharedPreferences != null && sharedPreferences.contains(key)) {
                Log.d(TAG, "sharedPreferencesEditor key : " + key + " exists");
                return true;
            } else {
                Log.d(TAG, "sharedPreferencesEditor key : " + key + " NOT exists");
                return false;
            }

        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    public static void removeKey(String key) {
        try {
            if(sharedPreferences != null && key != null) {
                sharedPreferencesEditor = sharedPreferences.edit();
                if (sharedPreferences.contains(key)) {
                    sharedPreferencesEditor.remove(key);
                    sharedPreferencesEditor.apply();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }


    public static void setLong(String key, long value) {
        try {
            if(sharedPreferences != null && key != null) {
                sharedPreferencesEditor = sharedPreferences.edit();
                if (sharedPreferences.contains(key)) sharedPreferencesEditor.remove(key);
                sharedPreferencesEditor.putLong(key, value);
                sharedPreferencesEditor.apply();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static long getLong(String key) {
        long keyValue = 1;
        try {
            keyValue = sharedPreferences.getLong(key, 1);
            if (keyValue > 0) return keyValue;
            else return keyValue;
        }catch (Exception e){e.printStackTrace();}
        return keyValue;
    }

    public static void setString(String key, String value) {
        try {
            if(sharedPreferences != null && key != null) {
                sharedPreferencesEditor = sharedPreferences.edit();
                if (sharedPreferences.contains(key)) sharedPreferencesEditor.remove(key);
                sharedPreferencesEditor.putString(key, value);
                sharedPreferencesEditor.apply();
            }
        }catch (NullPointerException e){e.printStackTrace();}
    }

    public static String getString(String key) {
        String keyValue = "";
        try {
            if(key != null && sharedPreferences != null) {
                keyValue = sharedPreferences.getString(key, "");
                if (keyValue.equals(false)) return keyValue;
                else {
                    return keyValue;
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return keyValue;
    }

    public static void setInt(String key, int value) {
        try {
            if(sharedPreferences != null && key != null) {
                sharedPreferencesEditor = sharedPreferences.edit();
                if (sharedPreferences.contains(key)) sharedPreferencesEditor.remove(key);
                sharedPreferencesEditor.putInt(key, value);
                sharedPreferencesEditor.apply();
            }
        }catch (NullPointerException e){e.printStackTrace();}
    }

    public static int getInt(String key) {
        int keyValue = 0;
        try {
            if (sharedPreferences.contains(key)) {
                keyValue = sharedPreferences.getInt(key, 0);
                if (keyValue > 0) return keyValue;
                else {
                    return keyValue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return keyValue;
    }

    public static void setBoolean(String key, boolean value) {
        try {
            if(sharedPreferences != null && key != null) {
                sharedPreferencesEditor = sharedPreferences.edit();
                if (sharedPreferences.contains(key)) sharedPreferencesEditor.remove(key);
                sharedPreferencesEditor.putBoolean(key, value);
                sharedPreferencesEditor.apply();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static Boolean getBoolean(String key) {
        try {
            Boolean keyValue = sharedPreferences.getBoolean(key, false);
            if (keyValue.equals(false)) return keyValue;
            else {
                return keyValue;
            }
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
}
