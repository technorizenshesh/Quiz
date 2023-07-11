package com.smsjuegos.quiz.activities.game2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityWelcomeMessageBinding;
import com.smsjuegos.quiz.model.SuccessResGetEvents;

public class WelcomeMessageActivity extends AppCompatActivity {

    ActivityWelcomeMessageBinding binding;
    private SuccessResGetEvents.Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome_message);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.btnPlay.setOnClickListener(v ->
                {
                    startActivity(new Intent(WelcomeMessageActivity.this
                            , Game2StartVideoAct.class).putExtra("instructionID", result));
                }
        );
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        binding.tvInstruction.setText(result.getDescription());
        Glide.with(this).load(result.getDescription_image())
                .into(binding.descriptionImage);
        binding.header.tvHeader.setText(getString(R.string.welcome_to_virus)+" "+result.getEventName());


    }
}