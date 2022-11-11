package com.my.quiz.activities.game2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.activities.HomeAct;
import com.my.quiz.databinding.ActivityQuestionBinding;
import com.my.quiz.model.SuccessResAddAnswer;
import com.my.quiz.model.SuccessResGetEvents;
import com.my.quiz.model.SuccessResGetVirusEvent;
import com.my.quiz.model.SuccessResGetVirusEvent;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

public class QuestionAct extends AppCompatActivity {

    private QuizInterface apiInterface;
    private ArrayList<SuccessResGetVirusEvent.Result> instructionList = new ArrayList<>();
    private SuccessResGetEvents.Result result;
    private ActivityQuestionBinding binding;
    private int position = 0;
    private Dialog dialog;
    private boolean hint1 = false, hint2=false, hint3 =false;
    TextView tvSHowHint1,tvSHowHint2,tvSHowHint3,tvHint1,tvHint2,tvHint3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_question);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        position = 0;
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        getInstruction();

        binding.tvHint.setOnClickListener(v ->
                {
                    showHints();
                }
                );

        binding.tvGiveup.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(QuestionAct.this)
                            .setTitle(getString(R.string.give_up))
                            .setMessage(getString(R.string.give_up_message))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addHintPanalties(3);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        );

        binding.etAnswer.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        binding.btnSubmit.setOnClickListener(v ->
                {
                    if(!binding.etAnswer.getText().toString().equalsIgnoreCase(""))
                    {
                        submitAnswer(binding.etAnswer.getText().toString());
                    }
                }
                );
    }

    private void submitAnswer(String answer) {
        DataManager.getInstance().showProgressMessage(QuestionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        map.put("event_game_id", instructionList.get(position).getId());
        map.put("ans", answer);
        map.put("event_id", instructionList.get(position).getEventId());
        map.put("user_id", userId);

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
                        if(position == instructionList.size()-1)
                        {
                            startActivity(new Intent(QuestionAct.this, MissionCompletedAct.class).putExtra("instructionID",result));
                            finishAffinity();
                        }
                        else
                        {
                            position = position+1;
                            setEventQuestions(position);
                        }
                    } else if (data.equals("0")) {
                        showToast(QuestionAct.this, message);
                    }else if (data.equals("2")) {
                        binding.etAnswer.setText("");
                        if(position == instructionList.size()-1)
                        {
                            startActivity(new Intent(QuestionAct.this, MissionCompletedAct.class).putExtra("instructionID",result));
                            finishAffinity();
                        }
                        else
                        {
                            position = position+1;
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

    private void getInstruction()
    {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("user_id", userId);
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
                        setEventQuestions(0);
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

    private void setEventQuestions(int count)
    {
        hint1 = false;
        hint2 =  false;
        hint3 =  false;
        Glide.with(QuestionAct.this)
                .load(instructionList.get(position).getImage())
                .centerCrop()
                .into(binding.ivPuzzel);
        binding.tvPuzzel.setText(instructionList.get(position).getInstructions());
    }

    private void showHints()
    {
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

        tvSHowHint3.setVisibility(View.GONE);

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
        DataManager.getInstance().showProgressMessage(QuestionAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", instructionList.get(position).getEventId());
        map.put("event_instructions_id", instructionList.get(position).getId());
        map.put("time", penaltyTime);
        map.put("hint_type", penalty+"");
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
                            binding.etAnswer.setText(instructionList.get(position).getOptionAns());
                        }
                    } else if (data.equals("0")) {
                        showToast(QuestionAct.this, message);
                    }else if (data.equals("2")) {
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