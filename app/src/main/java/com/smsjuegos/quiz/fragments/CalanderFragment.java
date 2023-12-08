package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.AccAdapter;
import com.smsjuegos.quiz.adapter.HomeAdapter;
import com.smsjuegos.quiz.databinding.FragmentCalanderBinding;
import com.smsjuegos.quiz.model.SuccessResAcc;
import com.smsjuegos.quiz.model.SuccessResAcc;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.NetworkAvailablity;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalanderFragment extends Fragment {

    FragmentCalanderBinding binding;
    List<SuccessResAcc.Result> eventsList = new LinkedList<>();
    private AccAdapter homeAdapter;
    private QuizInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calander, container, false);
        homeAdapter = new AccAdapter(getActivity(), eventsList, "cal");
        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.rvUpcomingEvents.setHasFixedSize(true);
        // binding.rvUpcomingEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvUpcomingEvents.setLayoutManager(new GridLayoutManager(getActivity(),
                3));
        binding.rvUpcomingEvents.setAdapter(homeAdapter);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
          //  getEventsImages();
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
        get_Accomplishments();
        return binding.getRoot();
    }

    private void get_Accomplishments() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(requireContext())
                .getBoolean(Constant.SELECTED_LANGUAGE);
        Call<SuccessResAcc> call = apiInterface.get_Accomplishments();
        call.enqueue(new Callback<SuccessResAcc>() {
            @Override
            public void onResponse(Call<SuccessResAcc> call, Response<SuccessResAcc> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAcc data = response.body();
                    Log.e("data", data.getStatus());
                    if (data.getStatus().equals("1")) {
                        eventsList.clear();
                        eventsList.addAll(data.getResult());
                        homeAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAcc> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}
/*package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.HomeAdapter;
import com.smsjuegos.quiz.databinding.FragmentCalanderBinding;
import com.smsjuegos.quiz.model.SuccessResAcc;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.NetworkAvailablity;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalanderFragment extends Fragment {

    FragmentCalanderBinding binding;
    List<SuccessResAcc.Result> eventsList = new LinkedList<>();
    private HomeAdapter homeAdapter;
    private QuizInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calander, container, false);
        homeAdapter = new HomeAdapter(getActivity(), eventsList, "cal");
        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.rvUpcomingEvents.setHasFixedSize(true);
        // binding.rvUpcomingEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvUpcomingEvents.setLayoutManager(new GridLayoutManager(getActivity(),
                2));
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

    private void getEventsImages() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(requireContext())
                .getBoolean(Constant.SELECTED_LANGUAGE);

        HashMap<String, String> map = new HashMap<>();
        if (!val) {
            map.put("lang", "en");
        } else {
            map.put("lang", "sp");
        }
        Call<SuccessResAcc> call = apiInterface.getEventsList(map);

        call.enqueue(new Callback<SuccessResAcc>() {
            @Override
            public void onResponse(Call<SuccessResAcc> call, Response<SuccessResAcc> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAcc data = response.body();
                    Log.e("data", data.status);
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
            public void onFailure(Call<SuccessResAcc> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}*/

/*<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    >
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".fragments.CalanderFragment"
    android:orientation="vertical"
    >
    <RelativeLayout
        android:id="@+id/header"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/appcolor"
        >

        <ImageView
            android:layout_width="@dimen/_40sdp"
            android:layout_height="@dimen/_40sdp"
            android:fontFamily="@font/poppins_bold"
            android:src="@drawable/app_icon"
            android:scaleType="fitXY"
            android:layout_marginTop="@dimen/_10sdp"
            android:layout_marginBottom="@dimen/_10sdp"
            android:layout_marginLeft="@dimen/_10sdp"
            />

     <!--   <TextView
            android:id="@+id/tvtitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:fontFamily="@font/poppins_bold"
            android:text="LOGO"
            android:textColor="@color/white"
            android:textSize="@dimen/_12sdp" />-->

        <TextView
            android:id="@+id/tvHeader"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:fontFamily="@font/poppins_bold"
            android:text="@string/events"
            android:textColor="@color/white"
            android:textSize="@dimen/_12sdp" />

        <ImageView
            android:id="@+id/imgSearch"
            android:layout_width="@dimen/_20sdp"
            android:layout_height="@dimen/_20sdp"
            android:layout_alignParentRight="true"
            android:padding="@dimen/_2sdp"
            android:layout_marginRight="@dimen/_10sdp"
            android:layout_centerVertical="true"
            android:src="@drawable/ic_search_white" />
    </RelativeLayout>

    <androidx.core.widget.NestedScrollView
        android:id="@+id/nestedScroll"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fillViewport="true"
        android:layout_below="@id/header"
        >
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="@dimen/_15sdp"
        >
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_5sdp"
            android:visibility="gone"
            >
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Upcoming Events"
                android:fontFamily="@font/poppins_medium"
                android:textColor="@color/black"
                android:textSize="@dimen/_12sdp"
                />
            <TextView
                android:id="@+id/tvViewAll"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/view_all"
                android:textAllCaps="true"
                android:fontFamily="@font/poppins_medium"
                android:layout_alignParentRight="true"
                android:textColor="@color/black"
                android:textSize="@dimen/_12sdp"
                />
        </RelativeLayout>

        <androidx.recyclerview.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_8sdp"
            android:id="@+id/rvUpcomingEvents"/>

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_5sdp"
            android:visibility="gone"
            >
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Recommended Events"
                android:fontFamily="@font/poppins_medium"
                android:textColor="@color/black"
                android:textSize="@dimen/_12sdp"
                />
            <TextView
                android:id="@+id/tvViewAll1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/view_all"
                android:textAllCaps="true"
                android:fontFamily="@font/poppins_medium"
                android:layout_alignParentRight="true"
                android:textColor="@color/black"
                android:textSize="@dimen/_12sdp"
                />
        </RelativeLayout>

        <androidx.recyclerview.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_8sdp"
            android:id="@+id/rvReomendedEvents"/>

    </LinearLayout>
    </androidx.core.widget.NestedScrollView>
</RelativeLayout>
</layout>*/