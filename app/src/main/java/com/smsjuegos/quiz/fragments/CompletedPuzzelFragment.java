package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.CompletedEventAdapter;
import com.smsjuegos.quiz.databinding.FragmentCompletedPuzzelBinding;
import com.smsjuegos.quiz.model.SuccessResGetMyEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletedPuzzelFragment extends Fragment {
    FragmentCompletedPuzzelBinding binding;
    private QuizInterface apiInterface;
    private final ArrayList<SuccessResGetMyEvents.Result> eventList = new ArrayList<>();
    private CompletedEventAdapter completedEventAdapter;
    private final ArrayList<SuccessResGetMyEvents.Result> myEventList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed_puzzel, container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.header.tvHeader.setText(R.string.finished_puzzles);
        binding.header.imgHeader.setOnClickListener(view1 -> {getActivity().onBackPressed();});
        getMyEvents();
        return binding.getRoot();}
    private void getMyEvents() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResGetMyEvents> call = apiInterface.getMyFinishEvent(map);
        call.enqueue(new Callback<SuccessResGetMyEvents>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResGetMyEvents> call, @NonNull Response<SuccessResGetMyEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {SuccessResGetMyEvents data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventList.clear();
                        myEventList.clear();
                        myEventList.addAll(data.getResult());
                        for (SuccessResGetMyEvents.Result result : myEventList) {if (result.getEventStatus().equalsIgnoreCase("END")) {eventList.add(result);}}
                        binding.rvEvents.setHasFixedSize(true);
                        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvEvents.setAdapter(new CompletedEventAdapter(getActivity(), eventList));
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        eventList.clear();
                        binding.rvEvents.setHasFixedSize(true);
                        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvEvents.setAdapter(new CompletedEventAdapter(getActivity(), eventList));
                    }} catch (Exception e) {e.printStackTrace();}}
            @Override
            public void onFailure(@NonNull Call<SuccessResGetMyEvents> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}