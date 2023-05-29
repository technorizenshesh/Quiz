package com.smsjuegos.quiz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.HomeAct;
import com.smsjuegos.quiz.databinding.FragmentBookingSuccessBinding;

public class BookingSuccessFragment extends Fragment {
    FragmentBookingSuccessBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_success,
                container, false);
        binding.btnGoHome.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), HomeAct.class));
                    getActivity().finish();
                }
        );
        return binding.getRoot();
    }
}