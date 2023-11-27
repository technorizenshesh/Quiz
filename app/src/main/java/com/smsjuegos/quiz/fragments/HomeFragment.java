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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.HomeAdapter;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    List<SlideModel> bannersList = new LinkedList<>();
    List<SuccessResGetEvents.Result> eventsList = new LinkedList<>();
    GPSTracker gpsTracker;
    // private SliderAdapter adapter;
    String city_id = "1";
    private HomeAdapter homeAdapter;
    private QuizInterface apiInterface;

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
        binding.spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                Log.e("TAG", "onItemSelected: " + i);
                Log.e("TAG", "onItemSelected: " + o);
                Log.e("TAG", "onItemSelected: " + i1);
                Log.e("TAG", "onItemSelected: " + t1);
                SuccessResGetEvents datadd = SharedPreferenceUtility.getInstance(getActivity())
                        .getSuccessResGetEvents("SuccessResGetEvents") ;

                switch (i1) {

                    case 0:
                        city_id = "1";
                            eventsList.clear();
                            for (SuccessResGetEvents.Result result : datadd.result){
                                if (result.city_id.equalsIgnoreCase( city_id)){
                                    eventsList.add(result);
                                }
                            }
                            homeAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        city_id = "2";
                            eventsList.clear();
                            for (SuccessResGetEvents.Result result : datadd.result){
                                if (result.city_id.equalsIgnoreCase( city_id)){
                                    eventsList.add(result);
                                }
                            }
                            homeAdapter.notifyDataSetChanged();

                        break;
                    default:
                        break;

                }

            }

        });
        getLocation();

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            if (SharedPreferenceUtility.getInstance(getActivity())
                    .getSuccessResGetEvents("SuccessResGetEvents") != null) {
                SuccessResGetEvents data = SharedPreferenceUtility.getInstance(getActivity())
                        .getSuccessResGetEvents("SuccessResGetEvents");
                if (data.status.equals("1")) {
                    eventsList.clear();
                    eventsList.addAll(data.getResult());
                    homeAdapter.notifyDataSetChanged();
                    binding.rvUpcomingEvents.hideShimmerAdapter();
                } else if (data.status.equals("0")) {
                    showToast(getActivity(), data.message);
                }
                getEventsImages2();

            } else {
                getEventsImages();
                getEventsImages();
            }
            if (SharedPreferenceUtility.getInstance(getActivity())
                    .getSuccessResGetBanner("SuccessResGetBanner") != null) {
                SuccessResGetBanner datax = SharedPreferenceUtility.getInstance(getActivity())
                        .getSuccessResGetBanner("SuccessResGetBanner");
                if (datax.status.equals("1")) {
                    bannersList.clear();
                    for (SuccessResGetBanner.Result res : datax.getResult()) {
                        bannersList.add(new SlideModel(res.getImage(), ScaleTypes.FIT));
                    }
                    binding.imageSlider.setImageList(bannersList);
                    binding.imageSlider.setDrawingCacheEnabled(true);
                    binding.imageSlider.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                }
                getBannerList2();
            } else {
                getBannerList();
            }

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
            if (gpsTracker.canGetLocation()) {
                SharedPreferenceUtility.getInstance(requireActivity()).putString(Constant.LATITUDE, gpsTracker.getLatitude() + "");
                SharedPreferenceUtility.getInstance(requireActivity()).putString(Constant.LONGITUDE, gpsTracker.getLongitude() + "");
                String lon = SharedPreferenceUtility.getInstance(getContext()).getString(LONGITUDE);
                String lat = SharedPreferenceUtility.getInstance(getContext()).getString(LATITUDE);

                Log.e("TAG", "getLocation:  latlatlat  " + lat);
                Log.e("TAG", "getLocation:lonlon " + lon);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
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
        map.put("city_id", city_id);
        Call<SuccessResGetEvents> call = apiInterface.getEventsList(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResGetEvents> call, @NonNull Response<SuccessResGetEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEvents data = response.body();
                    assert data != null;

                    Log.e("data", "-----------------" + String.valueOf(response.body()));
                    if (data.status.equals("1")) {
                        SharedPreferenceUtility.getInstance(getActivity()).putSuccessResGetEvents("SuccessResGetEvents", data);
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
            public void onFailure(@NonNull Call<SuccessResGetEvents> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void getEventsImages2() {
        boolean val = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";
        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("lang", lang);
        map.put("city_id", city_id);
        Call<SuccessResGetEvents> call = apiInterface.getEventsList(map);
        call.enqueue(new Callback<SuccessResGetEvents>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResGetEvents> call, @NonNull Response<SuccessResGetEvents> response) {
                try {
                    SuccessResGetEvents data = response.body();
                    if (data.status.equals("1")) {
                        SharedPreferenceUtility.getInstance(getActivity()).putSuccessResGetEvents("SuccessResGetEvents", data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResGetEvents> call, @NonNull Throwable t) {
                call.cancel();
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
                        SharedPreferenceUtility.getInstance(getActivity())
                                .putSuccessResGetBanner("SuccessResGetBanner", data);

                        bannersList.clear();
                        for (SuccessResGetBanner.Result res :
                                data.getResult()) {
                            bannersList.add(new SlideModel(res.getImage(), ScaleTypes.FIT));
                        }
                        binding.imageSlider.setImageList(bannersList);
                        binding.imageSlider.setDrawingCacheEnabled(true);
                        binding.imageSlider.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
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

    private void getBannerList2() {
        Call<SuccessResGetBanner> call = apiInterface.getBanners();
        call.enqueue(new Callback<SuccessResGetBanner>() {
            @Override
            public void onResponse(Call<SuccessResGetBanner> call, Response<SuccessResGetBanner> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetBanner data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        SharedPreferenceUtility.getInstance(getActivity())
                                .putSuccessResGetBanner("SuccessResGetBanner", data);
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