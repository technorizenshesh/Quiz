package com.smsjuegos.quiz.activities;

import static android.content.ContentValues.TAG;

import static com.smsjuegos.quiz.SMSApp.getCities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.Locale;

public class SplashAct extends AppCompatActivity {
    GPSTracker gpsTracker;

    private boolean isUserLoggedIn;

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashAct.this).getBoolean(Constant.IS_USER_LOGGED_IN);
        boolean val = SharedPreferenceUtility.getInstance(SplashAct.this).getBoolean(Constant.SELECTED_LANGUAGE);
        if (!val) {
            updateResources(SplashAct.this, "en");
        } else {
            updateResources(SplashAct.this, "es");
        }
        if (isUserLoggedIn) {
            gpsTracker = new GPSTracker(SplashAct.this);
            getCities(SplashAct.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }
            finds();
    }

    private void finds() {

        new Handler().postDelayed(() -> {
            if (isUserLoggedIn) {
                 startActivity(new Intent(SplashAct.this, HomeAct.class));
                finish();
            } else {
                startActivity(new Intent(SplashAct.this, ChooseLanguage.class)
                        .putExtra("from", "login"));
                finish();
            }
        }, 3000);
    }

}