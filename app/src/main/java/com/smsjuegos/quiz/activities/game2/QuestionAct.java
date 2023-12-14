package com.smsjuegos.quiz.activities.game2;

import static com.smsjuegos.quiz.retrofit.Constant.EVENT_CODE;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityQuestionBinding;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.model.SuccessResGetVirusEvent;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAct extends AppCompatActivity {

    TextView tvSHowHint1, tvSHowHint2, tvSHowHint3, tvHint1, tvHint2, tvHint3;
    private QuizInterface apiInterface;
    private final ArrayList<SuccessResGetVirusEvent.Result> instructionList = new ArrayList<>();
    private SuccessResGetEvents.Result result;
    private ActivityQuestionBinding binding;
    private int position = 0;
    private Dialog dialog;
    private boolean hint1 = false, hint2 = false, hint3 = false;
    // on the stopwatch.
    private int seconds = 0;

    // Is the stopwatch running?
    private boolean running = true;

    private boolean wasRunning;

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    private void runTimer(int mnts) {

        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60 + mnts;
                int secs = seconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                Log.e("TAG", "run: " + time);
                binding.timeView.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    // Save the state of the stopwatch
    // if it's about to be destroyed.
    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);
        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }

        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        position = 0;
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        getInstruction();
        runTimer(0);
        binding.ivshare.setOnClickListener(v -> {
            startActivity(getOpenFacebookIntent());

        });
        if (position == 18) {
            binding.ivshare.setVisibility(View.VISIBLE);
        } else {
            binding.ivshare.setVisibility(View.GONE);
        }
        binding.tvHint.setOnClickListener(v ->
                {
                    showHints();
                }
        );


        binding.tvGiveup.setOnClickListener(v ->
                {
                    final Dialog dialogq = new Dialog(QuestionAct.this);
                    dialogq.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogq.getWindow().getAttributes().windowAnimations
                            = android.R.style.Widget_Material_ListPopupWindow;
                    dialogq.setContentView(R.layout.dialog_give_up);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = dialogq.getWindow();
                    lp.copyFrom(window.getAttributes());
                    Button ivSubmit = dialogq.findViewById(R.id.btnSubmit);
                    ivSubmit.setOnClickListener(D ->
                            {
                                addHintPanalties(3);
                                dialogq.dismiss();
                            }
                    );
                    Button ivCancel = dialogq.findViewById(R.id.btncncel);
                    ivCancel.setOnClickListener(D ->
                            {
                                dialogq.dismiss();
                            }
                    );
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                    dialogq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogq.show();


                }
        );


        binding.etAnswer.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        binding.btnSubmit.setOnClickListener(v ->
                {
                    if (!binding.etAnswer.getText().toString().equalsIgnoreCase("")) {
                        submitAnswer(binding.etAnswer.getText().toString().trim().toUpperCase());
                    }
                }
        );
        binding.btnNext.setOnClickListener(v ->
                {
                    if (position == instructionList.size() - 1) {
                        startActivity(new Intent(QuestionAct.this,
                                MissionCompletedAct.class).putExtra("instructionID", result));
                        finishAffinity();
                    } else {
                        position = position + 1;
                        setEventQuestions(position);
                    }
                }
        );
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/smsjuegos"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/smsjuegos"));
        }
    }

    private void submitAnswer(String answer) {
        DataManager.getInstance().showProgressMessage(QuestionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        String event_code = SharedPreferenceUtility.getInstance(this).getString(EVENT_CODE);

        if (instructionList.get(position).getEventId().equalsIgnoreCase("49")) {

            binding.ivshare.setVisibility(View.VISIBLE);
        } else {
            binding.ivshare.setVisibility(View.GONE);
        }

        map.put("event_game_id", instructionList.get(position).getId());
        map.put("ans", answer);
        map.put("event_id", instructionList.get(position).getEventId());
        map.put("user_id", userId);
        map.put("event_code", event_code);
        map.put("custom_type", "no");
        /*   map.put("event_game_id", instructionList.get(position).getId());
        map.put("ans", answer.toUpperCase());
        map.put("event_id", instructionList.get(position).getEventId());
        map.put("user_id", userId);
        map.put("event_code", event_code);
        if (custom) {
            map.put("custom_type", "custom");
        } else {
            map.put("custom_type", "no");
        }*/
        Call<ResponseBody> call = apiInterface.submitVirusAnswer(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("result");
                    if (data.equals("1")) {
                        binding.etAnswer.setText("");
                        if (position == instructionList.size() - 1) {
                            startActivity(new Intent(QuestionAct.this,
                                    MissionCompletedAct.class).putExtra("instructionID", result));
                            finishAffinity();
                        } else {
                            position = position + 1;
                            setEventQuestions(position);
                        }
                    } else if (data.equals("0")) {
                        showToast(QuestionAct.this, message);
                    } else if (data.equals("2")) {
                        binding.etAnswer.setText("");
                        if (position == instructionList.size() - 1) {
                            startActivity(new Intent(QuestionAct.this,
                                    MissionCompletedAct.class).putExtra("instructionID", result));
                            finishAffinity();
                        } else {
                            position = position + 1;
                            setEventQuestions(position);
                        }
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

    private void getInstruction() {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(getApplicationContext()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";

        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        Map<String, String> map = new HashMap<>();

        map.put("event_id", result.getId());
        map.put("lang", lang);
        Call<SuccessResGetVirusEvent> call = apiInterface.getVirusEvent(map);
        call.enqueue(new Callback<SuccessResGetVirusEvent>() {
            @Override
            public void onResponse(Call<SuccessResGetVirusEvent> call, Response<SuccessResGetVirusEvent> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetVirusEvent data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        instructionList.clear();
                        instructionList.addAll(data.getResult());
                        setEventQuestions(17);
                    } else if (data.status.equals("0")) {
                        showToast(QuestionAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetVirusEvent> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setEventQuestions(int count) {
        hint1 = false;
        hint2 = false;
        hint3 = false;
        Glide.with(QuestionAct.this)
                .load(instructionList.get(position).getImage())
                .fitCenter()
                .into(binding.ivPuzzel);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        binding.webView.loadDataWithBaseURL("", instructionList.get(position).getInstructions(), mimeType, encoding, "");
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);

        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setDisplayZoomControls(false);

        if (instructionList.get(position).getOptionAns().equalsIgnoreCase("None")) {
            binding.rlBottom.setVisibility(View.GONE);
            binding.llButtonHint.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
        } else {
            binding.rlBottom.setVisibility(View.VISIBLE);
            binding.llButtonHint.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
        }

    }

    private void showHints() {
        seconds = seconds + 3;
        AppCompatButton btnCancel;
        dialog = new Dialog(QuestionAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_hint);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        tvSHowHint1 = dialog.findViewById(R.id.tvShowHint1);
        tvSHowHint2 = dialog.findViewById(R.id.tvShowHint2);
        tvSHowHint3 = dialog.findViewById(R.id.tvShowHint3);
        tvHint1 = dialog.findViewById(R.id.tvHint1);
        tvHint2 = dialog.findViewById(R.id.tvHint2);
        tvHint3 = dialog.findViewById(R.id.tvHint3);
        tvHint1.setText(instructionList.get(position).getInstructionsHint1());
        tvHint2.setText(instructionList.get(position).getInstructionsHint2());
        tvHint3.setText(instructionList.get(position).getInstructionsHint3());
        btnCancel = dialog.findViewById(R.id.btnSubmit);
        btnCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        if (hint1) {
            tvHint1.setVisibility(View.VISIBLE);
        }
        if (hint2) {
            tvHint2.setVisibility(View.VISIBLE);
        }
        if (hint3) {
            tvHint3.setVisibility(View.VISIBLE);
        }
        tvSHowHint1.setOnClickListener(v ->
                {
                    addHintPanalties(1);
                }
        );
        tvSHowHint2.setOnClickListener(v ->
                {
                    addHintPanalties(2);
                }
        );
        tvSHowHint3.setVisibility(View.GONE);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addHintPanalties(int penalty) {
        String penaltyTime = "";
        if (penalty == 1) {
            penaltyTime = "2";
        } else if (penalty == 2) {
            penaltyTime = "5";
        } else if (penalty == 3) {
            penaltyTime = "10";
        }

        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(QuestionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", instructionList.get(position).getEventId());
        map.put("event_instructions_id", instructionList.get(position).getId());
        map.put("time", penaltyTime);
        map.put("hint_type", penalty + "");
        map.put("user_id", userId);
        Call<ResponseBody> call = apiInterface.addVirusPanalties(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                        if (penalty == 1) {
                            hint1 = true;
                            tvHint1.setVisibility(View.VISIBLE);
                        } else if (penalty == 2) {
                            hint2 = true;
                            tvHint2.setVisibility(View.VISIBLE);
                        } else if (penalty == 3) {
                            binding.etAnswer.setText(instructionList.get(position).getOptionAns());
                        }
                    } else if (data.equals("0")) {
                        showToast(QuestionAct.this, message);
                    } else if (data.equals("2")) {
                        showToast(QuestionAct.this, jsonObject.getString("result"));
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