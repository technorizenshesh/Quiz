package com.my.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.databinding.ActivityDownloadBinding;
import com.my.quiz.model.SuccessResGetEventDetail;
import com.my.quiz.model.SuccessResGetInstruction;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

public class DownloadAct extends AppCompatActivity {

    ActivityDownloadBinding binding;

    private Dialog mDialog;

    private QuizInterface apiInterface;

    private String eventId,eventCode;

    private SuccessResGetInstruction.Result instruction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_download);

        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.imgHeader.setOnClickListener(v -> finish());
        binding.imgOptions.setOnClickListener(v ->
                {
                    showMainMenu();
                }
                );

        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");
        getEventDetails();

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
                    startActivity(new Intent(DownloadAct.this,InstrutionAct.class).putExtra("eventId",eventId));
                }
        );

        tvMap.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,MapAct.class).putExtra("eventId",eventId).putExtra("eventCode",eventCode));
                }
                );

        tvINventory.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,InventoryAct.class).putExtra("eventId",eventId).putExtra("eventCode",eventCode));
                }
        );

        tvFinalPuzzel.setOnClickListener(v ->
                {
                    startActivity(new Intent(DownloadAct.this,FinalPuzzelAct.class).putExtra("eventId",eventId).putExtra("eventCode",eventCode));
                }
        );

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    private void getEventDetails()
    {


        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);

        Call<SuccessResGetInstruction> call = apiInterface.getInstruction(map);

        call.enqueue(new Callback<SuccessResGetInstruction>() {
            @Override
            public void onResponse(Call<SuccessResGetInstruction> call, Response<SuccessResGetInstruction> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInstruction data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);


                        binding.tvInstruction.setText(data.getResult().get(0).getInstructions());

                    } else if (data.status.equals("0")) {
                        showToast(DownloadAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetInstruction> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


}