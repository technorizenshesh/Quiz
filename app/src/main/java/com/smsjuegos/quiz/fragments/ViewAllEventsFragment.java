package com.smsjuegos.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.AllEventsAdapter;
import com.smsjuegos.quiz.databinding.FragmentViewAllEventsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAllEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllEventsFragment extends Fragment {

    FragmentViewAllEventsBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAllEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAllEventsFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ViewAllEventsFragment newInstance(String param1, String param2) {
        ViewAllEventsFragment fragment = new ViewAllEventsFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_all_events, container, false);
        binding.header.tvHeader.setText(R.string.all_events);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.rvAllEvents.setHasFixedSize(true);
        binding.rvAllEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAllEvents.setAdapter(new AllEventsAdapter(getActivity()));
        return binding.getRoot();
    }
}