package com.smsjuegos.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityFinalBinding;

public class FinalAct extends AppCompatActivity {

    ActivityFinalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_final);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.final_puzzel));
        binding.tvNext.setOnClickListener(v ->
                {
                    startActivity(new Intent(FinalAct.this,FinalPuzzelAct.class));
                }
                );
    }
}