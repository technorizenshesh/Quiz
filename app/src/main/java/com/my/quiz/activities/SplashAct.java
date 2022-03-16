package com.my.quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.my.quiz.R;
import com.my.quiz.retrofit.Constant;
import com.my.quiz.utility.SharedPreferenceUtility;


public class SplashAct extends AppCompatActivity {
    private boolean isUserLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashAct.this).getBoolean(Constant.IS_USER_LOGGED_IN);

        finds();
    }


    private void finds() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {
                    startActivity(new Intent(SplashAct.this, HomeAct.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAct.this, LoginAct.class));
                    finish();
                }
            }
        },3000);
    }


}