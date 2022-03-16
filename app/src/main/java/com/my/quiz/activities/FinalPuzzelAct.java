package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.my.quiz.R;
import com.my.quiz.adapter.FinalPuzzelAdapter;
import com.my.quiz.databinding.ActivityFinalPuzzelBinding;

public class FinalPuzzelAct extends AppCompatActivity {

    ActivityFinalPuzzelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_final_puzzel);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.final_puzzel));
        binding.rvObjects.setHasFixedSize(true);
        binding.rvObjects.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this,3));
        binding.rvObjects.setAdapter(new FinalPuzzelAdapter(FinalPuzzelAct.this));
        binding.rvPeople.setHasFixedSize(true);
        binding.rvPeople.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this,3));
        binding.rvPeople.setAdapter(new FinalPuzzelAdapter(FinalPuzzelAct.this));
        binding.rvPlaces.setHasFixedSize(true);
        binding.rvPlaces.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this,3));
        binding.rvPlaces.setAdapter(new FinalPuzzelAdapter(FinalPuzzelAct.this));
        binding.btnFinsh.setOnClickListener(v ->
                {
                    startActivity(new Intent(FinalPuzzelAct.this,FinishTeamInfo.class));
                }
                );
    }

}