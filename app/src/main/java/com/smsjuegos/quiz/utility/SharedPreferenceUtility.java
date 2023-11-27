package com.smsjuegos.quiz.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.smsjuegos.quiz.model.SuccessResGetBanner;
import com.smsjuegos.quiz.model.SuccessResGetEvents;

import java.util.ArrayList;


public class SharedPreferenceUtility {
    private static SharedPreferences mPref;
    private static SharedPreferenceUtility mRef;
    private Editor mEditor;

    private SharedPreferenceUtility() {
    }

    /**
     * Singleton method return the instance
     **/
    public static SharedPreferenceUtility getInstance(Context context) {
        if (mRef == null) {
            mRef = new SharedPreferenceUtility();
            mPref = context.getApplicationContext().getSharedPreferences(
                    "MyPref", 0);
            return mRef;
        }
        return mRef;
    }

    /**
     * Put long value into shared preference
     **/
    public void putLong(String key, long value) {
        try {
            mEditor = mPref.edit();
            mEditor.putLong(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get long value from shared preference
     **/
    public long getLong(String key) {
        try {
            long lvalue;
            lvalue = mPref.getLong(key, 0);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put int value into shared preference
     **/
    public void putInt(String key, int value) {
        try {
            mEditor = mPref.edit();
            mEditor.putInt(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get int value from shared preference
     **/
    public int getInt(String key) {
        try {
            int lvalue;
            lvalue = mPref.getInt(key, 0);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put String value into shared preference
     **/
    public void putString(String key, String value) {
        try {
            mEditor = mPref.edit();
            mEditor.putString(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get String value from shared preference
     **/
    public String getString(String key) {
        try {
            String lvalue;
            lvalue = mPref.getString(key, "");
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Put String value into shared preference
     **/
    public void putBoolean(String key, Boolean value) {
        try {
            mEditor = mPref.edit();
            mEditor.putBoolean(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void putSuccessResGetEvents(String key, SuccessResGetEvents value) {
        try {
            Gson gson = new Gson();
            String jsonText = gson.toJson(value);
            mEditor = mPref.edit();
            mEditor.putString(key, jsonText);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SuccessResGetEvents getSuccessResGetEvents(String key) {
        try {
            String lvalue;
            lvalue = mPref.getString(key, "");
            Gson gson = new Gson();
            SuccessResGetEvents text = gson.fromJson(lvalue, SuccessResGetEvents.class);  //EDIT: gso to gson
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void putSuccessResGetBanner(String key, SuccessResGetBanner value) {
        try {
            Gson gson = new Gson();
            String jsonText = gson.toJson(value);
            mEditor = mPref.edit();
            mEditor.putString(key, jsonText);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SuccessResGetBanner getSuccessResGetBanner(String key) {
        try {
            String lvalue;
            lvalue = mPref.getString(key, "");
            Gson gson = new Gson();
            SuccessResGetBanner text = gson.fromJson(lvalue, SuccessResGetBanner.class);  //EDIT: gso to gson
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get String value from shared preference.
     **/
    public Boolean getBoolean(String key) {
        try {
            Boolean lvalue;
            lvalue = mPref.getBoolean(key, false);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put String value into shared preference
     **/
    public void putStringClear(String key, String value) {
        try {
            mEditor = mPref.edit();
            mEditor.putString(key, value);
            mEditor.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get String value from shared preference
     **/
    public String getStringClear(String key) {
        try {
            String lvalue;
            lvalue = mPref.getString(key, "");
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method is used to get clean shareprefrence
     */
    public void getClearPref() {
        try {
            mPref.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}