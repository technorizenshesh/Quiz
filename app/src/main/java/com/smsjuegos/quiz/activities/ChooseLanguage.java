package com.smsjuegos.quiz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityChooseLanguageBinding;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.Locale;

public class ChooseLanguage extends AppCompatActivity {
    ActivityChooseLanguageBinding binding;
    String from = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);

        boolean val = SharedPreferenceUtility.getInstance(ChooseLanguage.this).getBoolean(Constant.SELECTED_LANGUAGE);

        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");

        }
        if (!val) {
            updateResources(ChooseLanguage.this, "en");
            binding.radio1.setChecked(true);
            binding.radio3.setChecked(false);
        } else {
            updateResources(ChooseLanguage.this, "es");
            binding.radio3.setChecked(true);
            binding.radio1.setChecked(false);
        }

        binding.radio1.setOnClickListener(v ->
                {
                    updateResources(ChooseLanguage.this, "en");
                    binding.radio3.setChecked(false);
                    SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, false);
                }
        );

        binding.radio3.setOnClickListener(v ->
                {
                    updateResources(ChooseLanguage.this, "es");
                    binding.radio1.setChecked(false);
                    SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, true);
                }
        );

        binding.btnNext.setOnClickListener(v ->
                {

                    int id = binding.radioGroup.getCheckedRadioButtonId();

                    if (binding.radio1.getId() == id) {
                        SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, false);
                    } else {
                        SharedPreferenceUtility.getInstance(ChooseLanguage.this).putBoolean(Constant.SELECTED_LANGUAGE, true);
                    }

                    if (from.equalsIgnoreCase("login")) {
                        startActivity(new Intent(ChooseLanguage.this, LoginAct.class));
                        finish();
                    } else {
                        startActivity(new Intent(ChooseLanguage.this, HomeAct.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
        );
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}