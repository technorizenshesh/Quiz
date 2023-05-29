package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityPrivacyPolicyBinding;
import com.smsjuegos.quiz.model.SuccessResGetPP;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrivacyPolicyAct extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;
    private QuizInterface apiInterface;
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.RRback.setOnClickListener(v -> onBackPressed());

        getPrivacyPolicy();


    }


    public void getPrivacyPolicy() {

        DataManager.getInstance().showProgressMessage(PrivacyPolicyAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        boolean val = SharedPreferenceUtility.getInstance(getApplicationContext())
                .getBoolean(Constant.SELECTED_LANGUAGE);
        if (!val) {
            map.put("lang", "en");
        } else {
            map.put("lang", "sp");
        }
        Call<SuccessResGetPP> call = apiInterface.getPrivacyPolicy(map);

        call.enqueue(new Callback<SuccessResGetPP>() {
            @Override
            public void onResponse(Call<SuccessResGetPP> call, Response<SuccessResGetPP> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPP data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        description = data.getResult().getDescription();
                        setWebView();
                    } else if (data.status.equals("0")) {
                        showToast(PrivacyPolicyAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPP> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadData(description, "text/html; charset=utf-8", "UTF-8");
    }


}