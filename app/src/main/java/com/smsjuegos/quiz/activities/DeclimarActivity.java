package com.smsjuegos.quiz.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.GameAztecStartVideoAct;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityDeclimarBinding;
import com.smsjuegos.quiz.model.SuccessResGetEventDetail;

public class DeclimarActivity extends AppCompatActivity {
    final String encoding = "UTF-8";
    final String mimeType = "text/html";
    ActivityDeclimarBinding binding;
    private String eventId, eventCode, disclaimer;
    private SuccessResGetEventDetail.Result eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declimar);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_declimar);
        if (getIntent().getExtras() != null) {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            eventDetails = (SuccessResGetEventDetail.Result) bundle.getSerializable("instructionID");
            eventId = eventDetails.getId();
            eventCode = getIntent().getExtras().getString("eventCode");
            disclaimer = eventDetails.getDisclaimer();
            Log.e("TAG", "eventDetailseventDetailseventDetails: " + eventId);
            Log.e("TAG", "eventDetailseventDetailseventDetails: " + eventCode);
            // binding.tvInstruction.setText(data.getResult().get(0).getInstructions());
            binding.tvInstruction.loadDataWithBaseURL("", disclaimer,
                    mimeType, encoding, "");
            binding.tvInstruction.getSettings().setBuiltInZoomControls(true);
            binding.tvInstruction.getSettings().setDisplayZoomControls(false);

        }
        //  binding.tvInstruction.setText(getString(R.string.desclemer) );
        binding.btnDownload.setOnClickListener(v -> {

             /*startActivity(new Intent(getApplicationContext(),
                     GameAztecStartVideoAct.class)
                     // .putExtras(bundle )
                     .putExtra("videoUrl", eventDetails.getVideo())
                     .putExtra("eventId", eventId)
                     .putExtra("eventId", eventId)
                     .putExtra("eventCode", eventCode));*/

            final Dialog dialogq = new Dialog(DeclimarActivity.this);
            dialogq.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogq.getWindow().getAttributes().windowAnimations
                    = android.R.style.Widget_Material_ListPopupWindow;
            dialogq.setContentView(R.layout.dialog_intro);
            // dialogq.setCancelable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialogq.getWindow();
            lp.copyFrom(window.getAttributes());
            WebView tv_intro = dialogq.findViewById(R.id.tv_intro);
            ImageView intro_image = dialogq.findViewById(R.id.image_intro);
            ImageView imgHeader = dialogq.findViewById(R.id.imgHeader);
            tv_intro.loadDataWithBaseURL("", eventDetails.getIntro(),
                    mimeType, encoding, "");
            tv_intro.getSettings().setBuiltInZoomControls(true);
            tv_intro.getSettings().setDisplayZoomControls(false);

            Glide.with(getApplicationContext()).load(eventDetails.intro_image).into(intro_image);
            Button ivSubmit = dialogq.findViewById(R.id.btnDownload);
            imgHeader.setOnClickListener(D ->
                    {
                        // addHintPanalties(3);

                        dialogq.dismiss();

                    }
            );
            ivSubmit.setOnClickListener(D ->
                    {
                        // addHintPanalties(3);

                        dialogq.dismiss();
                        startActivity(new Intent(getApplicationContext(),
                                GameAztecStartVideoAct.class)
                                // .putExtras(bundle )
                                .putExtra("videoUrl", eventDetails.getVideo())
                                .putExtra("eventId", eventId)
                                .putExtra("eventId", eventId)
                                .putExtra("eventCode", eventCode));
                    }
            );
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            dialogq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogq.show();


        });
    }


}