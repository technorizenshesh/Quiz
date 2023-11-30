package com.smsjuegos.quiz;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.smsjuegos.quiz.activities.ForgotPassAct;
import com.smsjuegos.quiz.model.SuccessResCity;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SMSApp extends Application implements Application.ActivityLifecycleCallbacks {
    public static final String APP_TAG = "SMSApp";
    GPSTracker gpsTracker;
    Context context;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private SMSApp smsApp;

    public static void getCities(Context context, double latitude, double longitude) {
        QuizInterface apiInterface = ApiClient.getClient().create(QuizInterface.class);
        final SuccessResCity[] data = {new SuccessResCity()};
        Call<SuccessResCity> call = apiInterface.get_city();
        call.enqueue(new Callback<SuccessResCity>() {
            @Override
            public void onResponse(Call<SuccessResCity> call
                    , Response<SuccessResCity> response) {
                try {
                    data[0] = response.body();
                    assert data[0] != null;
                    if (data[0].status.equals("1")) {
                        Log.e("TAG", "onResponse:sorted " + data[0].result.toString());
                        ArrayList<SuccessResCity.Result> sorted =
                                sortLocationsByDistance(data[0].result, new LatLng(latitude, longitude));
                        Log.e("TAG", "onResponse:sorted " + sorted.toString());
                        if (!sorted.isEmpty()) {
                            SharedPreferenceUtility.getInstance(context).putSuccessResCity("SuccessResCity", sorted);
                        } else {
                            SharedPreferenceUtility.getInstance(context).putSuccessResCity("SuccessResCity", data[0].result);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SharedPreferenceUtility.getInstance(context).putSuccessResCity("SuccessResCity", data[0].result);

                }
            }

            @Override
            public void onFailure(Call<SuccessResCity> call, Throwable t) {
                call.cancel();
            }
        });

    }

    public static ArrayList<SuccessResCity.Result>
    sortLocationsByDistance(ArrayList<SuccessResCity.Result> unsortedLocations, final LatLng myLocation) {
        // Calculate distances
        for (SuccessResCity.Result location : unsortedLocations) {
            float[] results = new float[1];
            Location.distanceBetween(
                    myLocation.latitude, myLocation.longitude,
                    location.getLatitude(), location.getLongitude(),
                    results
            );
            location.setDistance(results[0]);
        }

        Collections.sort(unsortedLocations, (location1, location2) -> Float.compare(location1.getDistance(), location2.getDistance()));

        // Return the sorted ArrayList
        return unsortedLocations;
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

    public SMSApp getInstance() {
        return smsApp;
    }

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

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            ShowAppLog(APP_TAG, "App enters foreground", 1);

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
            ShowAppLog(APP_TAG, "App enters background", 1);

        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ShowAppLog(APP_TAG, "onActivityDestroyed1", 1);
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            ShowAppLog(APP_TAG, "App enters background", 1);

        }
    }
}
