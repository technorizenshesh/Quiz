package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.databinding.ActivitySignupBinding;
import com.my.quiz.model.SuccessResSignup;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.Constant;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.isValidEmail;
import static com.my.quiz.retrofit.Constant.showToast;

public class SignupAct extends AppCompatActivity {

    ActivitySignupBinding binding;
    private String strEmail="",strPass="",strConfirmPass="";
    private QuizInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.btnSignup.setOnClickListener(v ->
                {
                    strEmail = binding.etEmail.getText().toString().trim();
                    strPass = binding.etPassword.getText().toString().trim();
                    strConfirmPass = binding.etConfPass.getText().toString().trim();

                    if (isValid()) {
                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                            signup();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
                );
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupAct.this,LoginAct.class));
            }
        });
    }

    private void signup()
    {

        DataManager.getInstance().showProgressMessage(SignupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        map.put("password",strPass);
        map.put("register_id","");
        Call<SuccessResSignup> signupCall = apiInterface.signup(map);

        signupCall.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    if (data.status.equals("1")) {
                        showToast(SignupAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(SignupAct.this).putString(Constant.USER_ID,data.getResult().getId());
                        startActivity(new Intent(SignupAct.this, LoginAct.class));
                    } else if (data.status.equals("0")) {
                        showToast(SignupAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        }  else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strPass.equalsIgnoreCase("")) {
            binding.etPassword.setError(getString(R.string.please_enter_pass));
            return false;
        } else if (strConfirmPass.equalsIgnoreCase("")) {
            binding.etConfPass.setError(getString(R.string.please_enter_con_pass));
            return false;
        } else if (!strPass.equalsIgnoreCase(strConfirmPass)) {
            binding.etConfPass.setError(getString(R.string.pass_and_confirm_pass_not_macthed));
            return false;
        }
        return true;
    }
}