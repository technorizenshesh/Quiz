package com.my.quiz.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.activities.FinishTeamInfo;
import com.my.quiz.activities.HomeAct;
import com.my.quiz.adapter.PanlaltiesAdapter;
import com.my.quiz.adapter.TeamAdapter;
import com.my.quiz.databinding.FragmentTeamBinding;
import com.my.quiz.model.SuccessResGetFinalTime;
import com.my.quiz.model.SuccessResGetOtherUserData;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TeamFragment extends Fragment {

    FragmentTeamBinding binding;
    private String eventId,eventCode;
    private ArrayList<SuccessResGetFinalTime.Result> timePenalitiesList = new ArrayList<>();
    private PanlaltiesAdapter penaltiesAdapter;
    private TeamAdapter teamAdapter;
    private ArrayList<SuccessResGetOtherUserData.Result> otherResults = new ArrayList<>();

    private SuccessResGetFinalTime successResGetFinalTime;
    private QuizInterface apiInterface;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_team, container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.teams);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            eventId = bundle.getString("eventId");
            eventCode = bundle.getString("eventCode");
        }

        penaltiesAdapter = new PanlaltiesAdapter(getActivity(),timePenalitiesList);
        teamAdapter = new TeamAdapter(getActivity(),otherResults);
        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.teams);
        binding.rvTimePanalites.setHasFixedSize(true);
        binding.rvTimePanalites.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTimePanalites.setAdapter(penaltiesAdapter);
        binding.rvteam.setHasFixedSize(true);
        binding.rvteam.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvteam.setAdapter(teamAdapter);
        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.team_information));
        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.leader));
        binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);
        StringBuffer stringBuffer = new StringBuffer("Hello");
        stringBuffer.append(1);
        stringBuffer.append("Thnks");
        Log.d(TAG, "onCreateView: "+stringBuffer);
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getMyPuzzelFinishInfo();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if (currentTabSelected==0)
                {
                    binding.llInfo.setVisibility(View.VISIBLE);
                    binding.rvteam.setVisibility(View.GONE);
                    getMyPuzzelFinishInfo();
                }
                else
                {
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
        return binding.getRoot();
    }

    private void getMyPuzzelFinishInfo()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
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
                        showToast(getActivity(), data.message);
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

    private void setEventDetail()
    {
        binding.label.setText(successResGetFinalTime.getTeamName());
        String totalTime = successResGetFinalTime.getEventTotalTime();
        String teamDetail = getString(R.string.team_members_3_6)+" "+successResGetFinalTime.getTotalTicket()+"/6";
        binding.tvTotalPenalties.setText(successResGetFinalTime.getPenaltyTime()+" minutes");
        binding.tvTotalTime.setText(totalTime);
        binding.tvTeamDetail.setText(teamDetail);
        Glide.with(getActivity())
                .load(successResGetFinalTime.getEventDetails().getImage())
                .centerCrop()
                .into(binding.imgEvent);
    }

    private void getOtherFinishInfo()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
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
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        otherResults.clear();
                        otherResults.addAll(data.getResult());
                        teamAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

}