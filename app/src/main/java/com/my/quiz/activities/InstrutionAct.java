package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.quiz.R;
import com.my.quiz.databinding.ActivityInstrutionBinding;

public class InstrutionAct extends AppCompatActivity {

    ActivityInstrutionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_instrution);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );

        binding.header.tvHeader.setText(getString(R.string.instruction));

    }
}