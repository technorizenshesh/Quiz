package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.my.quiz.R;
import com.my.quiz.adapter.PanlaltiesAdapter;
import com.my.quiz.adapter.TeamAdapter;
import com.my.quiz.databinding.ActivityFinishTeamInfoBinding;


import static android.content.ContentValues.TAG;

public class FinishTeamInfo extends AppCompatActivity {

    ActivityFinishTeamInfoBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_finish_team_info);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(R.string.teams);
        binding.rvTimePanalites.setHasFixedSize(true);
        binding.rvTimePanalites.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTimePanalites.setAdapter(new PanlaltiesAdapter(this));
        binding.rvteam.setHasFixedSize(true);
        binding.rvteam.setLayoutManager(new LinearLayoutManager(this));
        binding.rvteam.setAdapter(new TeamAdapter(this));
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
    }
}