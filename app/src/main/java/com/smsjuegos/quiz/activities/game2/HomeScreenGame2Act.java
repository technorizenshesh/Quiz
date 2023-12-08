package com.smsjuegos.quiz.activities.game2;

import static com.smsjuegos.quiz.retrofit.Constant.GAME_LAVEL;
import static com.smsjuegos.quiz.retrofit.Constant.LATITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.LONGITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.LevelAdapter;
import com.smsjuegos.quiz.databinding.ActivityHomeScreenGame2Binding;
import com.smsjuegos.quiz.model.EventCodeResSuccess;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.model.SuccessResGetLevel;
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

public class HomeScreenGame2Act extends AppCompatActivity implements LevelAdapter.LevelInterface{

    ActivityHomeScreenGame2Binding binding;
    boolean IsCodeAvailable;
    private QuizInterface apiInterface;
    private SuccessResGetEvents.Result result;

    LevelAdapter levelAdapter;
    SuccessResGetLevel leveldata;
    public static String mode = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen_game2);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        result = (SuccessResGetEvents.Result)getIntent().getSerializableExtra("instructionID");
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
                                , WelcomeMessageActivity.class).putExtra(
                                "instructionID", result));
                    } else {

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
        EditText editText = dialog.findViewById(R.id.etName);

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        btnSubmit.setOnClickListener(v ->
                {
                    String strCode = editText.getText().toString().trim();
                    if (!strCode.equalsIgnoreCase("")) {
                      //  getLevels(strCode);
                        Apply_code(strCode,"1");

                        dialog.dismiss();

                    } else {
                        showToast(HomeScreenGame2Act.this, getString(R.string.please_enter_code));
                    }
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getLevels(String event_code ) {
        String userId = SharedPreferenceUtility.getInstance(HomeScreenGame2Act.this).getString(USER_ID);
        boolean val = SharedPreferenceUtility.getInstance(HomeScreenGame2Act.this).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("event_code",event_code );
        map.put("user_id", userId);
        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        map.put("lang", lang);
        Call<SuccessResGetLevel> call = apiInterface.get_level(map);
        call.enqueue(new Callback<SuccessResGetLevel>() {
            @Override
            public void onResponse(Call<SuccessResGetLevel> call, Response<SuccessResGetLevel> response) {

                // DataManager.getInstance().hideProgressMessage();
                try {
                    leveldata = response.body();

                    if (leveldata.getResult().size() > 0) {
                        leveldata.getResult().get(0).setSelected(true);
                        pickLevel(event_code);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetLevel> call, Throwable t) {
                call.cancel();
                //DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void pickLevel(String  code ) {
        final Dialog dialogq = new Dialog(HomeScreenGame2Act.this);
        dialogq.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogq.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialogq.setContentView(R.layout.dialog_choose_level);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogq.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView imgHeader = dialogq.findViewById(R.id.imgHeader);
        Button ivSubmit = dialogq.findViewById(R.id.btnDownload);
        RadioGroup radioGroup = dialogq.findViewById(R.id.radioGroup);
        RecyclerView level_list = dialogq.findViewById(R.id.level_list);
        levelAdapter = new LevelAdapter(HomeScreenGame2Act.this, leveldata.getResult(), this::selectedLevelInterface);
        level_list.setHasFixedSize(true);
        level_list.setAdapter(levelAdapter);
        imgHeader.setOnClickListener(D -> dialogq.dismiss());
        ivSubmit.setOnClickListener(D -> {
            Apply_code(code,mode);

        });
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialogq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogq.show();
    }

    private void Apply_code(String code,String mode) {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("user_id", userId);
        map.put("event_code", code);
        map.put("level", mode);
        Call<ResponseBody> call = apiInterface.virus_event_apply_code(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    assert response.body() != null;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {

                        SharedPreferenceUtility.getInstance(HomeScreenGame2Act.this).putString(Constant.EVENT_CODE, code);
                        startActivity(new Intent(HomeScreenGame2Act.this, WelcomeMessageActivity.class).putExtra("instructionID", result));
                    } else
                        if (data.equals("0")) {showToast(HomeScreenGame2Act.this, message);}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void is_Apply() {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_id", result.getId());
        boolean val = SharedPreferenceUtility.getInstance(getApplicationContext())
                .getBoolean(Constant.SELECTED_LANGUAGE);

        if (!val) {
            map.put("lang", "en");
        } else {
            map.put("lang", "sp");
        }
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
                        IsCodeAvailable = true;
                    } else {
                        IsCodeAvailable = false;
                        showToast(HomeScreenGame2Act.this, response.body().getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<EventCodeResSuccess> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void selectedLevelInterface(int position, SuccessResGetLevel.Result level) {
        for (int i = 0; i <= leveldata.getResult().size(); i++) {
            leveldata.getResult().get(i).setSelected(false);
        }
        leveldata.getResult().get(position).setSelected(true);
        //levelAdapter.notifyDataSetChanged();
    }

}