package com.my.quiz.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.my.quiz.R;
public class LoginAct extends AppCompatActivity {

TextView tvSignup;
    TextView tvForgotPass;
 AppCompatButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvSignup = findViewById(R.id.tvSiguplink);
        tvForgotPass =  findViewById(R.id.tvForgotPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view ->
                {
                    startActivity(new Intent(LoginAct.this,HomeAct.class));
                }
                );
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,ForgotPassAct.class));
            }
        });
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,SignupAct.class));
            }
        });

    }
}