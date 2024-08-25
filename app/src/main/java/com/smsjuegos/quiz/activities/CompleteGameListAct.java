package com.smsjuegos.quiz.activities;

import static android.content.ContentValues.TAG;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.FinishEventAdapter;
import com.smsjuegos.quiz.databinding.ActivityCompleteGameListBinding;
import com.smsjuegos.quiz.model.EventFinishResultModel;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteGameListAct extends AppCompatActivity {
    ActivityCompleteGameListBinding binding;

    private QuizInterface apiInterface;
    ArrayList<EventFinishResultModel.Result>arrayList;
    FinishEventAdapter finishEventAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declimar);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_game_list);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        initViews();

    }

    private void initViews() {
        arrayList = new ArrayList<>();

        binding.imgHeader.setOnClickListener(view -> finish());

        finishEventAdapter = new FinishEventAdapter(this,arrayList);
        binding.rvEventCompleteList.setAdapter(finishEventAdapter);

        getAllFinishEventResult();
    }


    private void getAllFinishEventResult() {

        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<ResponseBody> call = apiInterface.getAllFinishEventResultApi(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.e(TAG, "All Finish Event List Response = " + stringResponse);

                    if (status.equals("1")) {
                        EventFinishResultModel eventFinishResultModel = new Gson().fromJson(stringResponse, EventFinishResultModel.class);
                        binding.noData.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(eventFinishResultModel.getResult());
                        finishEventAdapter.notifyAdapter(arrayList);
                    } else if (status.equals("0")) {
                        arrayList.clear();
                        binding.noData.setVisibility(View.VISIBLE);
                        finishEventAdapter.notifyAdapter(arrayList);
                    } else if (status.equals("2")) {
                       // showToast(CompleteGameListAct.this, jsonObject.getString("result"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.noData.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                binding.noData.setVisibility(View.VISIBLE);

            }
        });
    }



}
