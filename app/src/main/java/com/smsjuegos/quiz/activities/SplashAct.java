package com.smsjuegos.quiz.activities;


import static com.smsjuegos.quiz.SMSApp.getCities;
import static com.smsjuegos.quiz.retrofit.Constant.LATITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.LONGITUDE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

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
            getLocation();
        }
            finds();
    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(SplashAct.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SplashAct.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashAct.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {

            try {
                gpsTracker = new GPSTracker(SplashAct.this);
                getCities(SplashAct.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
            }catch (Exception e){
                e.printStackTrace();
            }


        }
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




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==  Constant.LOCATION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                try {
                    gpsTracker = new GPSTracker(SplashAct.this);
                    getCities(SplashAct.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}