package com.my.quiz.fragments;

import android.graphics.Color;
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
import com.my.quiz.adapter.SliderAdapter;
import com.my.quiz.databinding.FragmentHomeBinding;
import com.my.quiz.databinding.HomeItemBinding;
import com.my.quiz.model.SuccessResGetBanner;
import com.my.quiz.model.SuccessResGetEvents;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.activities.HomeAct.navView;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    List<SuccessResGetBanner.Result> bannersList = new LinkedList<>();
    List<SuccessResGetEvents.Result> eventsList = new LinkedList<>();

    private HomeAdapter homeAdapter;
    private QuizInterface apiInterface;

    private SliderAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
//        binding.rvReomendedEvents.setHasFixedSize(true);
//        binding.rvReomendedEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.rvReomendedEvents.setAdapter(new HomeAdapter(getActivity(),true));

        homeAdapter = new HomeAdapter(getActivity(),eventsList,true);

        binding.rvUpcomingEvents.setHasFixedSize(true);
        binding.rvUpcomingEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvUpcomingEvents.setAdapter(homeAdapter);

        binding.tvViewAll.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_viewAllEventsFragment);
                }
                );

//        binding.tvViewAll1.setOnClickListener(v ->
//                {
//                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_viewAllEventsFragment);
//                }
//        );

        adapter = new SliderAdapter(getContext(),bannersList);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(3);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();


        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getBannerList();
            getEventsImages();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }


        return binding.getRoot();
    }

    private void getEventsImages()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Call<SuccessResGetEvents> call = apiInterface.getEventsList();

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

    private void getBannerList() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Call<SuccessResGetBanner> call = apiInterface.getBanners();

        call.enqueue(new Callback<SuccessResGetBanner>() {
            @Override
            public void onResponse(Call<SuccessResGetBanner> call, Response<SuccessResGetBanner> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetBanner data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        bannersList.clear();

                        bannersList.addAll(data.getResult());
                        adapter.notifyDataSetChanged();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetBanner> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}