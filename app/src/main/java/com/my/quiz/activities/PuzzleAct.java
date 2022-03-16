package com.my.quiz.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.my.quiz.R;
import com.my.quiz.databinding.ActivityPuzzleBinding;
public class PuzzleAct extends AppCompatActivity {
    ActivityPuzzleBinding binding;
    
    private Dialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_puzzle);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );
        binding.header.tvHeader.setText(getString(R.string.riddle));
        
        binding.btnAnswer.setOnClickListener(v -> 
                {
                    showDialog();
                }
                
        );
        
    }
    
    private void showDialog()
    {
        dialog = new Dialog(PuzzleAct.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_answer_success);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);


        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );



        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    
}