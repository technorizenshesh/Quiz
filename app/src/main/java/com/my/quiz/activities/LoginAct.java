package com.my.quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.databinding.ActivityLoginBinding;
import com.my.quiz.model.SuccessResLogin;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.Constant;
import com.my.quiz.retrofit.NetworkAvailablity;

import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.my.quiz.retrofit.QuizInterface;
import static android.content.ContentValues.TAG;
import static com.my.quiz.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    private QuizInterface apiInterface;
    ActivityLoginBinding binding;

    private String strEmail="",strPassword="",deviceToken="";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        mAuth = FirebaseAuth.getInstance();
        getToken();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        
        binding.btnLogin.setOnClickListener(view ->
                {
//                    startActivity(new Intent(LoginAct.this,HomeAct.class));

                    strEmail = binding.etName.getText().toString().trim();
                    strPassword = binding.etPass.getText().toString().trim();
                    if (isValid()) {
                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                            login();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
                );

        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,ForgotPassAct.class));
            }
        });

        binding.tvSiguplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,SignupAct.class));
            }
        });
    }

    private void login() {

        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();

        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", strEmail);
        map.put("password", strPassword);
        map.put("register_id", deviceToken);

        Call<SuccessResLogin> call = apiInterface.login(map);

        call.enqueue(new Callback<SuccessResLogin>() {
            @Override
            public void onResponse(Call<SuccessResLogin> call, Response<SuccessResLogin> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResLogin data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID, data.getResult().getId());
                        Toast.makeText(LoginAct.this,""+getResources().getString(R.string.logged_in_success), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginAct.this, HomeAct.class));
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResLogin> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etName.setError(getString(R.string.enter_email));
            return false;
        } else if (strPassword.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            return false;
        }
        return true;
    }
    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, ""+getString(R.string.fetching_fcm_token_failed), task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginAct.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}