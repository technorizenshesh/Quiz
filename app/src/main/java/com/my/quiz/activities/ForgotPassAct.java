package com.my.quiz.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.databinding.ActivityForgotPassBinding;
import com.my.quiz.model.SuccessResForgetPassword;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.isValidEmail;
import static com.my.quiz.retrofit.Constant.showToast;

public class ForgotPassAct extends AppCompatActivity {

    ActivityForgotPassBinding binding;
    private String strEmail = "";
    private QuizInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_pass);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.ivBack.setOnClickListener(v ->
                {
                    finish();
                }
                );
        binding.btnSubmit.setOnClickListener(v ->
                {
                    strEmail = binding.etEmail.getText().toString().trim();
                    if(isValid())
                    {
                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                            forgotPass();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void forgotPass() {
        DataManager.getInstance().showProgressMessage(ForgotPassAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        Call<SuccessResForgetPassword> call = apiInterface.forgotPassword(map);
        call.enqueue(new Callback<SuccessResForgetPassword>() {
            @Override
            public void onResponse(Call<SuccessResForgetPassword> call, Response<SuccessResForgetPassword> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResForgetPassword data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        Toast.makeText(ForgotPassAct.this,"Please check mail",Toast.LENGTH_SHORT).show();
                        binding.etEmail.setText("");
                    } else if (data.status.equals("0")) {
                        showToast(ForgotPassAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResForgetPassword> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError("Please enter email.");
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError("Please enter valid email.");
            return false;
        }
        return true;
    }


}