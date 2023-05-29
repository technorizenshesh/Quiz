package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.PanlaltiesAdapter;
import com.smsjuegos.quiz.adapter.TeamAdapter;
import com.smsjuegos.quiz.databinding.ActivityFinishTeamInfoBinding;
import com.smsjuegos.quiz.model.SuccessResGetFinalTime;
import com.smsjuegos.quiz.model.SuccessResGetOtherUserData;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.NetworkAvailablity;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishTeamInfo extends AppCompatActivity {

    ActivityFinishTeamInfoBinding binding;
    private QuizInterface apiInterface;
    private String eventId, eventCode;
    private final ArrayList<SuccessResGetFinalTime.Result> timePenalitiesList = new ArrayList<>();
    private PanlaltiesAdapter penaltiesAdapter;
    private TeamAdapter teamAdapter;
    private final ArrayList<SuccessResGetOtherUserData.Result> otherResults = new ArrayList<>();
    private SuccessResGetFinalTime successResGetFinalTime;
    private String fromWhere = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finish_team_info);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        penaltiesAdapter = new PanlaltiesAdapter(this, timePenalitiesList);
        teamAdapter = new TeamAdapter(this, otherResults);
        binding.ivHome.setOnClickListener(v ->
                {
                    startActivity(new Intent(FinishTeamInfo.this, HomeAct.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
        );
        binding.header.imgHeader.setOnClickListener(v ->
                startActivity(new Intent(FinishTeamInfo.this, HomeAct.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK))
               );
        binding.header.tvHeader.setText(R.string.teams);
        binding.rvTimePanalites.setHasFixedSize(true);
        binding.rvTimePanalites.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTimePanalites.setAdapter(penaltiesAdapter);
        binding.rvteam.setHasFixedSize(true);
        binding.rvteam.setLayoutManager(new LinearLayoutManager(this));
        binding.rvteam.setAdapter(teamAdapter);
        //  fromWhere="1";
        fromWhere = getIntent().getExtras().getString("from");
        if (fromWhere.equalsIgnoreCase("1")) {
            binding.tabLayoutEventDay.setVisibility(View.VISIBLE);
            //  eventId = "1";
            //  eventCode ="986524";
            eventId = getIntent().getExtras().getString("eventId");
            eventCode = getIntent().getExtras().getString("eventCode");
            binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab()
                    .setText(R.string.team_information));
            binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab()
                    .setText(R.string.leader));
            binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);

            binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int currentTabSelected = tab.getPosition();
                    if (currentTabSelected == 0) {
                        binding.llInfo.setVisibility(View.VISIBLE);
                        binding.rvteam.setVisibility(View.GONE);
                        getMyPuzzelFinishInfo();
                    } else {
                        binding.llInfo.setVisibility(View.GONE);
                        binding.rvteam.setVisibility(View.VISIBLE);
                        getOtherFinishInfo();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getMyPuzzelFinishInfo();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        } else if (fromWhere.equalsIgnoreCase("4")) {
            binding.tabLayoutEventDay.setVisibility(View.GONE);
            eventId = getIntent().getExtras().getString("eventId");
            eventCode = getIntent().getExtras().getString("eventCode");
            binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.team_information));
            binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);

            binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int currentTabSelected = tab.getPosition();
                    if (currentTabSelected == 0) {
                        binding.llInfo.setVisibility(View.VISIBLE);
                        binding.rvteam.setVisibility(View.GONE);
                        getGame4FinishInfo();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getGame4FinishInfo();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }

        } else if (fromWhere.equalsIgnoreCase("5")) {

            binding.tabLayoutEventDay.setVisibility(View.GONE);

            eventId = getIntent().getExtras().getString("eventId");
            eventCode = getIntent().getExtras().getString("eventCode");

            binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.team_information));
            binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);

            binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int currentTabSelected = tab.getPosition();
                    if (currentTabSelected == 0) {
                        binding.llInfo.setVisibility(View.VISIBLE);
                        binding.rvteam.setVisibility(View.GONE);
                        getGame4FinishInfo();
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getGame4FinishInfo();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }

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
                        showToast(FinishTeamInfo.this, data.message);
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

    private void getGame4FinishInfo() {
        binding.tvTeamDetail.setVisibility(View.GONE);
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("user_id", userId);
        Call<SuccessResGetFinalTime> call = apiInterface.myGame4CompletedTime(map);
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
                        showToast(FinishTeamInfo.this, data.message);
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
        Glide.with(FinishTeamInfo.this).load(successResGetFinalTime.getEventDetails().getImage()).centerCrop().into(binding.imgEvent);
    }

    private void getOtherFinishInfo() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        Call<SuccessResGetOtherUserData> call = apiInterface.otherPuzzelCompletedTime(map);
        call.enqueue(new Callback<SuccessResGetOtherUserData>() {
            @Override
            public void onResponse(Call<SuccessResGetOtherUserData> call, Response<SuccessResGetOtherUserData> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetOtherUserData data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "teamAdapter teamAdapter teamAdapter" + dataResponse);

                        otherResults.clear();
                        otherResults.addAll(data.getResult());
                        teamAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(FinishTeamInfo.this, data.message);
                        otherResults.clear();
                        teamAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetOtherUserData> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FinishTeamInfo.this, HomeAct.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        super.onBackPressed();
    }
}