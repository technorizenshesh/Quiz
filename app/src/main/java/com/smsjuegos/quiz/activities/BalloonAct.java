package com.smsjuegos.quiz.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.smsjuegos.quiz.R;

public class BalloonAct extends AppCompatActivity {
    private Dialog balloonDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balloon);

      /*  ImageView balloon = findViewById(R.id.balloon);

        // Create the float animation
        TranslateAnimation floatAnim = new TranslateAnimation(
                0, 0, // Start X, End X
                0, -1000); // Start Y, End Y

        floatAnim.setDuration(2000); // Duration of the animation
        floatAnim.setRepeatCount(Animation.INFINITE); // Repeat infinitely
        floatAnim.setRepeatMode(Animation.REVERSE); // Reverse the animation

        balloon.startAnimation(floatAnim);*/







            // Create the custom dialog
            balloonDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            balloonDialog.setContentView(R.layout.dialog_balloon);

            // Get the balloon ImageView
            ImageView balloon = balloonDialog.findViewById(R.id.balloon);

            // Load and start the animation
            Animation floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation);
            balloon.startAnimation(floatAnimation);

            // Set up the dialog to close on touch
           /* balloonDialog.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Dismiss the dialog on touch
                    balloonDialog.dismiss();
                    return true;
                }
            });*/

            // Show the dialog
        balloonDialog.show();
        }



}