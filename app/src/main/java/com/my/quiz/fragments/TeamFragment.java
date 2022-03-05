package com.my.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.my.quiz.R;
import com.my.quiz.adapter.PanlaltiesAdapter;
import com.my.quiz.adapter.TeamAdapter;
import com.my.quiz.databinding.FragmentTeamBinding;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {

    FragmentTeamBinding binding;

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
        binding.rvTimePanalites.setHasFixedSize(true);
        binding.rvTimePanalites.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTimePanalites.setAdapter(new PanlaltiesAdapter(getActivity()));
        binding.rvteam.setHasFixedSize(true);
        binding.rvteam.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvteam.setAdapter(new TeamAdapter(getActivity()));
        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.team_information));
        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.leader));
        binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);
        StringBuffer stringBuffer = new StringBuffer("Hello");
        stringBuffer.append(1);
        stringBuffer.append("Thnks");
        Log.d(TAG, "onCreateView: "+stringBuffer);
        binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if (currentTabSelected==0)
                {
                 binding.llInfo.setVisibility(View.VISIBLE);
                 binding.rvteam.setVisibility(View.GONE);
                }
                else
                {
                    binding.llInfo.setVisibility(View.GONE);
                    binding.rvteam.setVisibility(View.VISIBLE);
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
}