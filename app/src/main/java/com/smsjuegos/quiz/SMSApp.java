package com.smsjuegos.quiz;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.Objects;

public class SMSApp extends Application implements Application.ActivityLifecycleCallbacks {
    public static final String APP_TAG = "SMSApp";
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private SMSApp smsApp;

    public SMSApp getInstance() {
        return smsApp;
    }

    GPSTracker gpsTracker;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        smsApp = this;
        context = getBaseContext();
        if (SharedPreferenceUtility.getInstance(this)
                .getBoolean(Constant.IS_USER_LOGGED_IN)) {
            gpsTracker = new GPSTracker(context);
        }

    }

    @Override
    public void onTerminate() {
        if (gpsTracker != null) {
            gpsTracker.stopListener();
            gpsTracker = null;
        }
        super.onTerminate();
    }

    public static void SMSAppShowLog(String track, String message, int type) {
        try{
        int WTF = 1;
        int VERBOSE = 2;
        int DEBUG = 3;
        int INFO = 4;
        int WARN = 5;
        int ERROR = 6;
        if (type == ERROR) Log.e(APP_TAG, track + "-------------:  " + message);
        if (type == VERBOSE) Log.v(APP_TAG, track + "-------------:  " + message);
        if (type == INFO) Log.i(APP_TAG, track + "-------------:  " + message);
        if (type == WARN) Log.w(APP_TAG, track + "-------------:  " + message);
        if (type == DEBUG) Log.d(APP_TAG, track + "-------------:  " + message);
        if (type == WTF) Log.wtf(APP_TAG, track + "-------------:  " + message);
    }catch (Exception e){
            System.out.println("Exception  ---  "+Objects.requireNonNull(e.getCause()).getMessage());
            System.out.println("Exception  ---  "+Objects.requireNonNull(e.getMessage()));
            System.out.println("Exception  ---  "+Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            SMSAppShowLog(APP_TAG,"App enters foreground",1);

        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            SMSAppShowLog(APP_TAG,"App enters background",1);

        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SMSAppShowLog(APP_TAG,"onActivityDestroyed1",1);
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            SMSAppShowLog(APP_TAG,"App enters background",1);

        }
    }
}
