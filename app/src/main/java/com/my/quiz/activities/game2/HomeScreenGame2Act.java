package com.my.quiz.activities.game2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.my.quiz.R;
import com.my.quiz.databinding.ActivityHomeScreenGame2Binding;
import com.my.quiz.model.SuccessResGetEvents;
import com.my.quiz.model.SuccessResGetInstruction;

public class HomeScreenGame2Act extends AppCompatActivity {

    ActivityHomeScreenGame2Binding binding;

    private SuccessResGetEvents.Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_screen_game2);

        binding.btnBegin.setOnClickListener(v ->
                {
                    startActivity(new Intent(HomeScreenGame2Act.this,WelcomeMessageActivity.class).putExtra("instructionID",result));
                }
                );

        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");

        Glide
                .with(HomeScreenGame2Act.this)
                .load(result.getImage())
                .centerCrop()
                .into(binding.ivGame);
    }
}