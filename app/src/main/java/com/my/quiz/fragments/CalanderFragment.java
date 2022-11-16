package com.my.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.adapter.HomeAdapter;
import com.my.quiz.databinding.FragmentCalanderBinding;
import com.my.quiz.model.SuccessResGetEvents;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.activities.HomeAct.navView;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalanderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalanderFragment extends Fragment {

    FragmentCalanderBinding binding;
    private HomeAdapter homeAdapter;
    List<SuccessResGetEvents.Result> eventsList = new LinkedList<>();
    private QuizInterface apiInterface;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalanderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalanderFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static CalanderFragment newInstance(String param1, String param2) {
        CalanderFragment fragment = new CalanderFragment();
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
//        navView.getMenu().setGroupCheckable(0, true, true);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calander, container, false);
        homeAdapter = new HomeAdapter(getActivity(),eventsList,"cal");
        apiInterface = ApiClient.getClient().create(QuizInterface.class);


        binding.rvUpcomingEvents.setHasFixedSize(true);
        binding.rvUpcomingEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvUpcomingEvents.setAdapter(homeAdapter);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getEventsImages();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.imgSearch.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_searchEventsFragment);
                }
                );

        binding.tvViewAll.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_viewAllEventsFragment);
                }
        );

        binding.tvViewAll1.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_viewAllEventsFragment);
                }

        );

        return binding.getRoot();
    }

    private void getEventsImages()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        HashMap<String,String> map = new HashMap<>();

        map.put("lang","sp");

        Call<SuccessResGetEvents> call = apiInterface.getEventsList(map);

        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetEvents> call, Response<SuccessResGetEvents> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        homeAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}