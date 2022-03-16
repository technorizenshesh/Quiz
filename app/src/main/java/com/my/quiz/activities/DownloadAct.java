package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.quiz.R;
import com.my.quiz.databinding.ActivityDownloadBinding;

public class DownloadAct extends AppCompatActivity {

    ActivityDownloadBinding binding;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_download);
        binding.imgHeader.setOnClickListener(v -> finish());
        binding.imgOptions.setOnClickListener(v ->
                {
                    showMainMenu();
                }
                );

    }

    private void showMainMenu()
    {

        TextView tvInstruction,tvMap,tvINventory,tvFinalPuzzel;
        mDialog = new Dialog(DownloadAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        mDialog.setContentView(R.layout.main_menu_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView ivCancel = mDialog.findViewById(R.id.ivCancel);

        tvInstruction = mDialog.findViewById(R.id.tvInstruction);
        tvFinalPuzzel = mDialog.findViewById(R.id.tvFinalPuzzel);
        tvINventory = mDialog.findViewById(R.id.tvINventory);
        tvMap = mDialog.findViewById(R.id.tvMap);

        ivCancel.setOnClickListener(v ->
                {
                    mDialog.dismiss();
                }
        );

        tvInstruction.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,InstrutionAct.class));
                }
        );

        tvMap.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,MapAct.class));
                }
                );

        tvINventory.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,InventoryAct.class));
                }
        );

        tvFinalPuzzel.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,FinalAct.class));
                }
        );

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }
}