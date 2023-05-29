package com.smsjuegos.quiz.activities.cardigo;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.smsjuegos.quiz.GameAztecStartVideoAct;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.DeclimarActivity;
import com.smsjuegos.quiz.activities.FinishTeamInfo;
import com.smsjuegos.quiz.databinding.ActivityCardigoPuzzleFinalBinding;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardigoPuzzleFinalActivity extends AppCompatActivity {
    ActivityCardigoPuzzleFinalBinding binding;
    private String eventId = "", eventCode = "", FinalAnswer = "",FinalHTML = "",FinalImage="";
    private QuizInterface apiInterface;
    private ArrayList<PuzzleList.Result> peopleList = new ArrayList<>();
    private CardigoPuzzleAdapter peopleAdapter;
    private final int peopleSelected = -1;
    final String encoding = "UTF-8";
    final String mimeType = "text/html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cardigo_puzzle_final);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.final_puzzel));
        binding.tvContent.getSettings().setBuiltInZoomControls(true);
      eventId = getIntent().getExtras().getString("eventId");
      eventCode = getIntent().getExtras().getString("eventCode");
       //   eventId = "8";
       //   eventCode = "855297";
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        getPeople();
        binding.btnSubmit.setOnClickListener(v ->
                {
                    if (!binding.etAnswer.getText().toString().equalsIgnoreCase("")) {
                        if (binding.etAnswer.getText().toString().trim().equalsIgnoreCase(FinalAnswer)) {
                            puzzelComplete();
                        } else {
                            addPanalties(5);

                        }
                    }
                }
        );
        binding.btnFinsh.setOnClickListener(v ->
                {
                    if (peopleSelected == -1) {
                        showToast(CardigoPuzzleFinalActivity.this, "" + getString(R.string.select_people_image));
                    } else {

                    }


                }
        );
    }

    public void addPanalties(int penalty) {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_instructions_id", "1");
        map.put("time", "" + penalty);
        map.put("event_code", eventCode);
        map.put("hint_type", penalty + "");
        map.put("user_id", userId);
        Call<ResponseBody> call = apiInterface.addPanalties(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    showToast(CardigoPuzzleFinalActivity.this,
                            "" + getString(R.string.wrong_answere_five));
                 /*   JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");*/
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

    private void getPeople() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(getApplicationContext()).
                getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";
        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("lang", lang);
        Call<PuzzleList> call = apiInterface.get_event_instructions_game_images(map);
        call.enqueue(new Callback<PuzzleList>() {
            @Override
            public void onResponse(@NonNull Call<PuzzleList> call, @NonNull Response<PuzzleList> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    PuzzleList data = response.body();
                    if (!data.getStatus().equals("0")) {
                        FinalAnswer = data.getFinal_answer().trim();
                        FinalHTML = data.getAfter_finish_text();
                        FinalImage = data.getAfter_finish_image();
                        peopleList = data.getResult();
                        binding.rvPeople.setLayoutManager(new GridLayoutManager(
                                CardigoPuzzleFinalActivity.this, 4));
                        peopleAdapter = new CardigoPuzzleAdapter(
                                CardigoPuzzleFinalActivity.this, peopleList);
                        binding.rvPeople.setHasFixedSize(true);
                        binding.rvPeople.setAdapter(peopleAdapter);
                    } else if (data.getStatus().equals("0")) {
                        showToast(CardigoPuzzleFinalActivity.this, data.getMessage());
                        peopleList.clear();
                        peopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PuzzleList> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void puzzelComplete() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        Call<ResponseBody> call = apiInterface.puzzelCompleted(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        showCongrats();

                       /* startActivity(new Intent(CardigoPuzzleFinalActivity.this,
                                FinishTeamInfo.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("from", "1")
                                .putExtra("eventId", eventId)
                                .putExtra("eventCode", eventCode));*/

                    } else if (data.equals("0")) {
                        showToast(CardigoPuzzleFinalActivity.this, message);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void showCongrats()
        {
            final Dialog dialogq = new Dialog(CardigoPuzzleFinalActivity.this);
            dialogq.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogq.getWindow().getAttributes().windowAnimations
                    = android.R.style.Widget_Material_ListPopupWindow;
            dialogq.setContentView(R.layout.dialog_after);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialogq.getWindow();
            lp.copyFrom(window.getAttributes());
            WebView tv_intro = dialogq.findViewById(R.id.tv_intro);
            ImageView intro_image = dialogq.findViewById(R.id.image_intro);
            ImageView imgHeader = dialogq.findViewById(R.id.imgHeader);
            tv_intro.loadDataWithBaseURL("", FinalHTML, mimeType, encoding, "");
            tv_intro.getSettings().setBuiltInZoomControls(true);
            tv_intro.getSettings().setDisplayZoomControls(false);
            Glide.with(getApplicationContext()).load(FinalImage).into(intro_image);
            Button ivSubmit = dialogq.findViewById(R.id.btnDownload);
            imgHeader.setOnClickListener(D ->
                    {
                    //    dialogq.dismiss();
                    }
            );
            ivSubmit.setOnClickListener(D ->
                    {
                        dialogq.dismiss();
                       /* startActivity(new Intent(CardigoPuzzleFinalActivity.this,
                                FinishTeamInfo.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("from", "1")
                                .putExtra("eventId", eventId)
                                .putExtra("eventCode", eventCode));*/
                    }
            );
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            dialogq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogq.show();
dialogq.setOnDismissListener(dialog -> startActivity(new Intent(CardigoPuzzleFinalActivity.this,
        FinishTeamInfo.class)
        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra("from", "1")
        .putExtra("eventId", eventId)
        .putExtra("eventCode", eventCode)));

        }

}