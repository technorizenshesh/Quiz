package com.smsjuegos.quiz.activities.game2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityHomeScreenGame2Binding;
import com.smsjuegos.quiz.model.EventCodeResSuccess;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

public class HomeScreenGame2Act extends AppCompatActivity {

    ActivityHomeScreenGame2Binding binding;
    private QuizInterface apiInterface;
boolean IsCodeAvailable;
    private SuccessResGetEvents.Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_screen_game2);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);



        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        is_Apply();
        Glide
                .with(HomeScreenGame2Act.this)
                .load(result.getImage())
                .centerCrop()
                .into(binding.ivGame);
        binding.btnBegin.setOnClickListener(v ->
                {
                    if (IsCodeAvailable) {

                        startActivity(new Intent(HomeScreenGame2Act.this
                                ,WelcomeMessageActivity.class).putExtra(
                                "instructionID",result));
                    }else {

                        showImageSelection();

                    }

                }
        );

    }


    public void showImageSelection() {
        final Dialog dialog = new Dialog(HomeScreenGame2Act.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_use_code);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        EditText editText =  dialog.findViewById(R.id.etName);

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        btnSubmit.setOnClickListener(v ->
                {
                    String strCode = editText.getText().toString().trim();
                    if(!strCode.equalsIgnoreCase(""))
                    {
                        Apply_code(strCode);
                        dialog.dismiss();

                    }
                    else
                    {
                        showToast(HomeScreenGame2Act.this,getString(R.string.please_enter_code));
                    }
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Apply_code(String code)  {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("user_id", userId);
        map.put("event_code", code);
        Call<ResponseBody> call = apiInterface.virus_event_apply_code(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        SharedPreferenceUtility.getInstance(HomeScreenGame2Act
                                .this).putString(Constant.EVENT_CODE,
                                code);
                        startActivity(new Intent(HomeScreenGame2Act.this
                                ,WelcomeMessageActivity.class).putExtra(
                                "instructionID",result));
                    } else if (data.equals("0")) {
                        showToast(HomeScreenGame2Act.this, message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    private void is_Apply()  {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_id", result.getId());
        map.put("lang","en");
        Call<EventCodeResSuccess> call = apiInterface.get_event_code(map);
        call.enqueue(new Callback<EventCodeResSuccess>() {
            @Override
            public void onResponse(Call<EventCodeResSuccess> call, Response<EventCodeResSuccess> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.body().getResult().getStatus().equalsIgnoreCase("Start")) {
                        SharedPreferenceUtility.getInstance(HomeScreenGame2Act
                                .this).putString(Constant.EVENT_CODE,
                                response.body().getResult().getEventCode());
                        IsCodeAvailable= true;
                    } else  {
                        IsCodeAvailable= false;
                        showToast(HomeScreenGame2Act.this, response.body().getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<EventCodeResSuccess> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}