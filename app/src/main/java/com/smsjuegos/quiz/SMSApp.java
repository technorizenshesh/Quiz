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

    public static void ShowAppLog(String trace, String message, int type) {
        try {
            String logMessage = trace + "-------------:  " + message;
            switch (type) {
                case 1: // WTF
                    Log.wtf(APP_TAG, logMessage);
                    break;
                case 2: // VERBOSE
                    Log.v(APP_TAG, logMessage);
                    break;
                case 3: // DEBUG
                    Log.d(APP_TAG, logMessage);
                    break;
                case 4: // INFO
                    Log.i(APP_TAG, logMessage);
                    break;
                case 5: // WARN
                    Log.w(APP_TAG, logMessage);
                    break;
                case 6: // ERROR
                    Log.e(APP_TAG, logMessage);
                    break;
                default:
                    // Handle unexpected log type
                    break;
            }
        } catch (Exception e) {
            Log.e(APP_TAG, "Exception: " + Log.getStackTraceString(e));
        }
    }
    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            ShowAppLog(APP_TAG,"App enters foreground",1);

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
            ShowAppLog(APP_TAG,"App enters background",1);

        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ShowAppLog(APP_TAG,"onActivityDestroyed1",1);
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            ShowAppLog(APP_TAG,"App enters background",1);

        }
    }
}
