package com.smsjuegos.quiz.activities.game2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.game4.Game4HomeAct;
import com.smsjuegos.quiz.databinding.ActivityGame2InstructionBinding;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
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

import static com.smsjuegos.quiz.retrofit.Constant.EVENT_CODE;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

public class Game2InstructionAct extends AppCompatActivity {

    ActivityGame2InstructionBinding binding;
    private SuccessResGetEvents.Result result;
    private QuizInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game2_instruction);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
        );
        binding.header.tvHeader.setText(getString(R.string.instruction));
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra
                ("instructionID");
        Glide.with(this)
                .load(result.getImage())
                .centerCrop()
                .into(binding.ivGame);
        if (result.getEvent_instructions1() != null) {

            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            binding.tvInstruction.loadDataWithBaseURL("", result.getEvent_instructions1()
                    , mimeType, encoding, "");

        } else {
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            binding.tvInstruction.loadDataWithBaseURL("", result.getEventInstructions()
                    , mimeType, encoding, "");
        }
        binding.btnStart.setOnClickListener(v ->
                {
                    if (result.getType().equalsIgnoreCase("crime")) {
                        addCrimeEventStartTime();
                    } else {
                        addeventStartTime();
                    }

                }
        );
    }

    public void addeventStartTime() {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        String event_code = SharedPreferenceUtility.getInstance(this).getString(EVENT_CODE);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("user_id", userId);
        map.put("event_code", event_code);
        Call<ResponseBody> call = apiInterface.addVirusStartTime(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                        startActivity(new Intent(
                                Game2InstructionAct.this, QuestionAct.class)
                                .putExtra("instructionID", result));
                    } else if (data.equals("0")) {
                        showToast(Game2InstructionAct.this, message);
                    } else if (data.equals("2")) {
                        showToast(Game2InstructionAct.this,
                                jsonObject.getString("message"));
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

    public void addCrimeEventStartTime() {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("user_id", userId);
        Call<ResponseBody> call = apiInterface.addCrimeEventStartTime(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                        startActivity(new Intent(Game2InstructionAct.this, Game4HomeAct.class).putExtra("eventId", result.getId()));
                    } else if (data.equals("0")) {
                        showToast(Game2InstructionAct.this, message);
                    } else if (data.equals("2")) {
                        showToast(Game2InstructionAct.this, jsonObject.getString("result"));
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


}