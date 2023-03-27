package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.LATITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.LONGITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.LoginAct;
import com.smsjuegos.quiz.adapter.HomeAdapter;
import com.smsjuegos.quiz.adapter.SliderAdapter;
import com.smsjuegos.quiz.databinding.FragmentHomeBinding;
import com.smsjuegos.quiz.model.SuccessResGetBanner;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.NetworkAvailablity;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    List<SuccessResGetBanner.Result> bannersList = new LinkedList<>();
    List<SuccessResGetEvents.Result> eventsList = new LinkedList<>();
    private HomeAdapter homeAdapter;
    private QuizInterface apiInterface;
    private SliderAdapter adapter;
GPSTracker gpsTracker ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
                container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        gpsTracker = new GPSTracker(requireActivity());
        homeAdapter = new HomeAdapter(getActivity(), eventsList, "home");
        binding.rvUpcomingEvents.setHasFixedSize(true);
        binding.rvUpcomingEvents.setLayoutManager(new GridLayoutManager(getActivity(),
                2));
        binding.rvUpcomingEvents.setAdapter(homeAdapter);
        binding.tvViewAll.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id
                            .action_navigation_home_to_viewAllEventsFragment);
                }
        );

        getLocation();
        adapter = new SliderAdapter(getContext(), bannersList);
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
            getEventsImages();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            if (gpsTracker.canGetLocation()){
                SharedPreferenceUtility.getInstance(requireActivity()).putString(Constant.LATITUDE, gpsTracker.getLatitude()+"");
                SharedPreferenceUtility.getInstance(requireActivity()).putString(Constant.LONGITUDE, gpsTracker.getLongitude()+"");
                String lon = SharedPreferenceUtility.getInstance(getContext()).getString(LONGITUDE);
                String lat = SharedPreferenceUtility.getInstance(getContext()).getString(LATITUDE);

                Log.e("TAG", "getLocation:  latlatlat  "+lat );
                Log.e("TAG", "getLocation:lonlon "+lon );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    private void getEventsImages() {
        binding.rvUpcomingEvents.showShimmerAdapter();
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";

        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("lang", lang);
        Call<SuccessResGetEvents> call = apiInterface.getEventsList(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetEvents> call, Response<SuccessResGetEvents> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        homeAdapter.notifyDataSetChanged();
                        binding.rvUpcomingEvents.hideShimmerAdapter();

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
                    Log.e("data", data.status);
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