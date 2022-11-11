package com.my.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.adapter.CompletedEventAdapter;
import com.my.quiz.adapter.ListAdapter;
import com.my.quiz.databinding.FragmentCompletedPuzzelBinding;
import com.my.quiz.model.SuccessResGetCompletedPuzzel;
import com.my.quiz.model.SuccessResGetMyEvents;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedPuzzelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CompletedPuzzelFragment extends Fragment {

    private QuizInterface apiInterface;
    FragmentCompletedPuzzelBinding  binding;
    private ArrayList<SuccessResGetMyEvents.Result> eventList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CompletedEventAdapter completedEventAdapter;
    private ArrayList<SuccessResGetMyEvents.Result> myEventList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletedPuzzelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedPuzzelFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static CompletedPuzzelFragment newInstance(String param1, String param2) {
        CompletedPuzzelFragment fragment = new CompletedPuzzelFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_completed_puzzel, container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
//        completedEventAdapter = new CompletedEventAdapter(getActivity(),eventList);
        binding.header.tvHeader.setText(R.string.finished_puzzles);
        binding.header.imgHeader.setOnClickListener(view1 ->
                {
                    getActivity().onBackPressed();
                }
        );

//        binding.rvEvents.setHasFixedSize(true);
//        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rvEvents.setAdapter(completedEventAdapter);

        getMyEvents();

        return binding.getRoot();
    }

    private void getMyEvents()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetMyEvents> call = apiInterface.getMyFinishEvent(map);
        call.enqueue(new Callback<SuccessResGetMyEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetMyEvents> call, Response<SuccessResGetMyEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetMyEvents data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        eventList.clear();
                        myEventList.clear();
                        myEventList.addAll(data.getResult());

                        for (SuccessResGetMyEvents.Result result:myEventList)
                        {
                            if(result.getEventStatus().equalsIgnoreCase("END"))
                            {
                                eventList.add(result);
                            }
                        }

                        binding.rvEvents.setHasFixedSize(true);
                        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvEvents.setAdapter(new CompletedEventAdapter(getActivity(),eventList));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        eventList.clear();
                        binding.rvEvents.setHasFixedSize(true);
                        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvEvents.setAdapter(new CompletedEventAdapter(getActivity(),eventList));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetMyEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}