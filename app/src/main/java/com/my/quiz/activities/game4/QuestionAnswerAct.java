package com.my.quiz.activities.game4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.databinding.ActivityQuestionAnswerBinding;
import com.my.quiz.model.SuccessResAddAnswer;
import com.my.quiz.model.SuccessResGetInstruction;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

public class QuestionAnswerAct extends AppCompatActivity {

    ActivityQuestionAnswerBinding binding;
    private Dialog dialog;
    private QuizInterface apiInterface;
    private Dialog hintDialog;
    private SuccessResGetInstruction.Result result=null;
    private String selectedAnswer = "";
    private boolean hint1 = false, hint2=false, hint3 =false;
    private Context context;
    private SuccessResAddAnswer.Result answerResult  = null;
    private String eventCode;
    private RadioButton selectedRadioButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_question_answer);
        context = QuestionAnswerAct.this;
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
        );

        result = (SuccessResGetInstruction.Result) getIntent().getSerializableExtra("instructionID");

        eventCode = getIntent().getExtras().getString("eventCode");

        binding.header.tvHeader.setText(getString(R.string.riddle));

        binding.btnAnswer.setOnClickListener(v ->
                {
                    showDialog();
                }
        );

        Glide.with(context)
                .load(result.getImage())
                .centerInside()
                .into(binding.ivPuzzel);

        binding.tvContent.setText(result.getInstructions());
        binding.btnHint.setOnClickListener(v ->
                {
                    showHints();
                }
        );
    }

    private void showDialog()
    {

        dialog = new Dialog(QuestionAnswerAct.this);
        selectedAnswer = "";
        RadioGroup radioGroup;
        RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
        AppCompatButton submit;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_select_answer);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        radioGroup = dialog.findViewById(R.id.radioGroup);
        submit = dialog.findViewById(R.id.btnSubmit);
        radioButton1 = dialog.findViewById(R.id.radio_button_0);
        radioButton2 = dialog.findViewById(R.id.radio_button_1);
        radioButton3 = dialog.findViewById(R.id.radio_button_2);
        radioButton4 = dialog.findViewById(R.id.radio_button_3);
        radioButton1.setText(result.getOptionA());
        radioButton2.setText(result.getOptionB());
        radioButton3.setText(result.getOptionC());
        radioButton4.setText(result.getOptionD());
        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        submit.setOnClickListener(v ->
                {

                    selectedAnswer = "";
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    selectedRadioButton = (RadioButton) dialog.findViewById(selectedId);
//                    String selectedText = selectedRadioButton.getText().toString();

                    if(radioButton1.isChecked())
                    {
                        selectedAnswer = "A";
                        submitAnswer(selectedAnswer);
                    }else if(radioButton2.isChecked())
                    {
                        selectedAnswer = "B";
                        submitAnswer(selectedAnswer);
                    }else if(radioButton3.isChecked())
                    {
                        selectedAnswer = "C";
                        submitAnswer(selectedAnswer);
                    }else if(radioButton4.isChecked())
                    {
                        selectedAnswer = "D";
                        submitAnswer(selectedAnswer);
                    }

                }
        );
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    TextView tvSHowHint1,tvSHowHint2,tvSHowHint3,tvHint1,tvHint2,tvHint3;

    private void showHints()
    {
        AppCompatButton appCompatButton;
        dialog = new Dialog(QuestionAnswerAct.this);
        selectedAnswer = "";
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

        appCompatButton = dialog.findViewById(R.id.btnSubmit);

        tvHint1 = dialog.findViewById(R.id.tvHint1);
        tvHint2 = dialog.findViewById(R.id.tvHint2);
        tvHint3 = dialog.findViewById(R.id.tvHint3);

        tvHint1.setText(result.getInstructionsHint1());
        tvHint2.setText(result.getInstructionsHint2());
        tvHint3.setText(result.getInstructionsHint3());

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        if(hint1)
        {
            tvHint1.setVisibility(View.VISIBLE);
        } if(hint2)
         {
        tvHint2.setVisibility(View.VISIBLE);
         }
        if(hint3)
        {
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

        tvSHowHint3.setOnClickListener(v ->
                {
                    addHintPanalties(3);
                }
        );

        appCompatButton.setOnClickListener(v ->
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

    public void addHintPanalties(int penalty)
    {
        String penaltyTime = "";
        if(penalty == 1)
        {
            penaltyTime = "2";
        }else if(penalty == 2)
        {
            penaltyTime = "5";
        }else if(penalty == 3)
        {
            penaltyTime = "10";
        }
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(QuestionAnswerAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getEventId());
        map.put("event_instructions_id", result.getId());
        map.put("time", penaltyTime);
        map.put("hint_type", penalty+"");
        map.put("user_id", userId);
        Call<ResponseBody> call = apiInterface.addPanaltiesGame4(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                        if(penalty == 1)
                        {
                            hint1 = true;
                            tvHint1.setVisibility(View.VISIBLE);
                        } else if(penalty == 2)
                        {
                            hint2 = true;
                            tvHint2.setVisibility(View.VISIBLE);
                        }else if(penalty == 3)
                        {
                            hint3 = true;
                            tvHint3.setVisibility(View.VISIBLE);
                        }
                    } else if (data.equals("0")) {
                        showToast(QuestionAnswerAct.this, message);
                    }else if (data.equals("2")) {
                        showToast(QuestionAnswerAct.this, jsonObject.getString("result"));
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

    public void submitAnswer(String answer)
    {
        String userId = SharedPreferenceUtility.getInstance(QuestionAnswerAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(QuestionAnswerAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_id", result.getEventId());
        map.put("event_game_id", result.getId());
        map.put("ans", answer);
//        map.put("event_code", eventCode);
        Call<ResponseBody> call = apiInterface.submitCrimeAnswer(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.e("data", data);
                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        dialog.dismiss();

                        answerSuccess();
                    } else if (data.equals("0")) {
                        String result = jsonObject.getString("result");
                        showToast(QuestionAnswerAct.this, result);
                    }else if (data.equals("2")) {
                        dialog.dismiss();
                        answerSuccess();
//                        showToast(QuestionAnswerAct.this, message);
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

    private void answerSuccess()
    {

        dialog = new Dialog(QuestionAnswerAct.this);
        selectedAnswer = "";
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_answer_success);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        ImageView ivPuzzel = dialog.findViewById(R.id.ivWitness);
        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        Glide.with(QuestionAnswerAct.this)
                .load(result.getFinalPuzzleImage())
                .into(ivPuzzel);

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    
    
}