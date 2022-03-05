package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.my.quiz.R;
import com.my.quiz.adapter.InventoryAdapter;
import com.my.quiz.databinding.ActivityInventoryBinding;

public class InventoryAct extends AppCompatActivity {

    ActivityInventoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_inventory);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.inventory));
        binding.rvINventory.setHasFixedSize(true);
        binding.rvINventory.setLayoutManager(new LinearLayoutManager(InventoryAct.this));
        binding.rvINventory.setAdapter(new InventoryAdapter(InventoryAct.this));
    }
}