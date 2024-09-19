package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.PanlaltiesAdapter;
import com.smsjuegos.quiz.databinding.ActivityShowResultBinding;
import com.smsjuegos.quiz.model.SuccessResGetFinalTime;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowResultAct extends AppCompatActivity {
    ActivityShowResultBinding binding;

    private QuizInterface apiInterface;
    private  ArrayList<SuccessResGetFinalTime.Result> timePenalitiesList = new ArrayList<>();
    private PanlaltiesAdapter penaltiesAdapter;
    private String eventId, eventCode;
    private SuccessResGetFinalTime successResGetFinalTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declimar);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_result);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        initViews();

    }

    private void initViews() {
      try {
          if(getIntent()!=null){
            //  eventId = getIntent().getExtras().getString("eventId");
            //  eventCode = getIntent().getExtras().getString("eventCode");
              eventId = getIntent().getStringExtra("eventId");
              eventCode = getIntent().getStringExtra("eventCode");
          }

          timePenalitiesList = new ArrayList<>();

          binding.imgHeader.setOnClickListener(view -> finish());

          penaltiesAdapter = new PanlaltiesAdapter(this, timePenalitiesList);
          binding.rvTimePanalites.setAdapter(penaltiesAdapter);

          getMyPuzzelFinishInfo();
      }catch (Exception e){
          e.printStackTrace();
      }

    }


    private void getMyPuzzelFinishInfo() {

        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        Call<SuccessResGetFinalTime> call = apiInterface.myPuzzelCompletedTime(map);
        call.enqueue(new Callback<SuccessResGetFinalTime>() {
            @Override
            public void onResponse(Call<SuccessResGetFinalTime> call, Response<SuccessResGetFinalTime> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetFinalTime data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        successResGetFinalTime = data;
                        setEventDetail();
                        timePenalitiesList.clear();
                        timePenalitiesList.addAll(data.getResult());
                        penaltiesAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(ShowResultAct.this, data.message);
                        timePenalitiesList.clear();
                        penaltiesAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetFinalTime> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void setEventDetail() {
        binding.label.setText(successResGetFinalTime.getEventDetails().getEventName());
        binding.etTeamName.setText(successResGetFinalTime.getTeamName());
        String totalTime = successResGetFinalTime.getEventTotalTime();
        String teamDetail = getString(R.string.team_members_3_6) + " " + successResGetFinalTime.getTotalTicket() + "/6";
        binding.tvTotalPenalties.setText(successResGetFinalTime.getPenaltyTime() + getString(R.string.minuts));
        binding.tvTotalTime.setText(totalTime);
        binding.tvTeamDetail.setText(teamDetail);
        Glide.with(ShowResultAct.this).load(successResGetFinalTime.getEventDetails().getImage()).centerCrop().into(binding.imgEvent);
    }

}
