package com.my.quiz.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.quiz.R;
import com.my.quiz.databinding.ActivityPuzzleBinding;
public class PuzzleAct extends AppCompatActivity {
    ActivityPuzzleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_puzzle);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );
        binding.header.tvHeader.setText("Riddle (Puzzle)");
    }
}