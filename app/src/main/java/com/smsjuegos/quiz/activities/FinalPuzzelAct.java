package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.GAME_LAVEL;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.FinalPuzzelAdapter;
import com.smsjuegos.quiz.databinding.ActivityFinalPuzzelBinding;
import com.smsjuegos.quiz.model.SuccessResGetInventory;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.FinalPuzzelInterface;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalPuzzelAct extends AppCompatActivity {
    ActivityFinalPuzzelBinding binding;
    final String encoding = "UTF-8";
    final String mimeType = "image/html";
    private String eventId, eventCode,FinalHTML = "",FinalImage="";
    private QuizInterface apiInterface;
    private final ArrayList<SuccessResGetInventory.Result> peopleList = new ArrayList<>();
    private final ArrayList<SuccessResGetInventory.Result> placesList = new ArrayList<>();
    private final ArrayList<SuccessResGetInventory.Result> objectList = new ArrayList<>();

    private FinalPuzzelAdapter peopleAdapter;
    private FinalPuzzelAdapter placesAdapter;
    private FinalPuzzelAdapter objectAdapter;

    private int peopleSelected = -1;
    private int objectSelected = -1;
    private int placeSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_final_puzzel);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.final_puzzel));
        binding.tvContent.getSettings().setBuiltInZoomControls(true);


        peopleAdapter = new FinalPuzzelAdapter(FinalPuzzelAct.this, peopleList, new FinalPuzzelInterface() {
            @Override
            public void selectedFinalPuzzel(int position) {
                peopleSelected = position;
            }
        });

        placesAdapter = new FinalPuzzelAdapter(FinalPuzzelAct.this, placesList, new FinalPuzzelInterface() {
            @Override
            public void selectedFinalPuzzel(int position) {
                placeSelected = position;
            }
        });

        objectAdapter = new FinalPuzzelAdapter(FinalPuzzelAct.this, objectList, new FinalPuzzelInterface() {
            @Override
            public void selectedFinalPuzzel(int position) {
                objectSelected = position;
            }
        });

        apiInterface = ApiClient.getClient().create(QuizInterface.class);
       eventId = getIntent().getExtras().getString("eventId");
       eventCode = getIntent().getExtras().getString("eventCode");
       //  eventId=   "18";
      //   eventCode=   "604423";
        binding.rvObjects.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this, 3));
        binding.rvObjects.setAdapter(objectAdapter);
        binding.rvPeople.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this, 3));
        binding.rvPeople.setAdapter(peopleAdapter);

        binding.rvPlaces.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this, 3));
        binding.rvPlaces.setAdapter(placesAdapter);

        // getObject();
        getPeople();
        //  getPlaces();

        binding.btnFinsh.setOnClickListener(v ->
                {
                    //showCongrats();
                   // showCongrats();
                    if (placeSelected == -1) {
                        showToast(FinalPuzzelAct.this, "" + getString(R.string.select_places_image));
                    } else if (peopleSelected == -1) {
                        showToast(FinalPuzzelAct.this, "" + getString(R.string.select_people_image));
                    } else if (objectSelected == -1) {
                        showToast(FinalPuzzelAct.this, "" + getString(R.string.select_object_image));
                    } else {
                        if (placesList.get(placeSelected).getFinalPuzzleStatus().equalsIgnoreCase("Yes")) {
                            if (peopleList.get(peopleSelected).getFinalPuzzleStatus().equalsIgnoreCase("Yes")) {
                                if (objectList.get(objectSelected).getFinalPuzzleStatus().equalsIgnoreCase("Yes")) {
                                    puzzelComplete();
                                } else {
                                    showToast(FinalPuzzelAct.this, getString(R.string.wrong_image));
                                    addPanalties(5);
                                }
                            } else {
                                showToast(FinalPuzzelAct.this, getString(R.string.wrong_image));
                                addPanalties(5);

                            }

                        } else {
                            showToast(FinalPuzzelAct.this, getString(R.string.wrong_image));
                            addPanalties(5);

                        }
                    }
                }
        );
    }

    public void addPanalties(int penalty) {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_instructions_id", "1");
        map.put("time", "" + penalty);
        map.put("event_code", eventCode);
        map.put("hint_type", penalty + "");
        map.put("user_id", userId);
        String level = SharedPreferenceUtility.getInstance(this).getString(GAME_LAVEL);
        map.put("level", level);
        Call<ResponseBody> call = apiInterface.addPanalties(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void getPeople() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(getApplicationContext()).getBoolean(Constant.SELECTED_LANGUAGE);
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
        String level = SharedPreferenceUtility.getInstance(this).getString(GAME_LAVEL);
        map.put("level", level);
        // map.put("type", "People");
        Call<SuccessResGetInventory> call = apiInterface.getInventory(map);
        call.enqueue(new Callback<SuccessResGetInventory>() {
            @Override
            public void onResponse(Call<SuccessResGetInventory> call, Response<SuccessResGetInventory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInventory data = response.body();
                    Log.e("data", data.status);
                    final String mimeType = "text/html";
                    final String encoding = "UTF-8";
                     binding.tvContent.setWebViewClient(new WebViewClient());
                    binding.tvContent.getSettings().setLoadWithOverviewMode(true);
                    binding.tvContent.getSettings().setUseWideViewPort(true);
                    binding.tvContent.getSettings().setJavaScriptEnabled(true);
                     binding.tvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                     binding.tvContent.getSettings().setPluginState(WebSettings.PluginState.ON);
                     binding.tvContent.getSettings().setMediaPlaybackRequiresUserGesture(false);
                     binding.tvContent.setWebChromeClient(new WebChromeClient());
                     binding.tvContent.loadDataWithBaseURL(null, data.getNotice(), "text/html", "UTF-8", null);
                     binding.tvContent.getSettings().setBuiltInZoomControls(true);
                     binding.tvContent.getSettings().setDisplayZoomControls(false);
                    if (data.status.equals("1")) {
                        FinalHTML = data.getAfter_finish_text();
                        FinalImage = data.getAfter_finish_image();
                        peopleList.clear();
                        placesList.clear();
                        objectList.clear();
                        //  peopleList.addAll(data.getResult());
                        //   peopleAdapter.notifyDataSetChanged();
                        ArrayList<SuccessResGetInventory.Result> ArrayListss = new ArrayList<>();
                        ArrayListss.clear();
                        ArrayListss.addAll(data.getResult());
                        if (ArrayListss.size() >= 1) {
                            for (int i = 0; i < ArrayListss.size(); i++) {
                                SuccessResGetInventory.Result res = ArrayListss.get(i);
                                if (res.getType().equalsIgnoreCase("Places")) {

                                    if (!res.getFinalPuzzleImage().equalsIgnoreCase("http://appsmsjuegos.com/Quiz/uploads/images/")) placesList.add(res);
                                } else if (res.getType().equalsIgnoreCase("People")) {
                                    if (!res.getFinalPuzzleImage().equalsIgnoreCase("http://appsmsjuegos.com/Quiz/uploads/images/")) peopleList.add(res);
                                } else if (res.getType().equalsIgnoreCase("Objects")) {
                                    if (!res.getFinalPuzzleImage().equalsIgnoreCase("http://appsmsjuegos.com/Quiz/uploads/images/"))  objectList.add(res);
                                }
                            }

                            peopleAdapter.notifyDataSetChanged();
                            placesAdapter.notifyDataSetChanged();
                            objectAdapter.notifyDataSetChanged();
                        }


                    } else if (data.status.equals("0")) {
                        showToast(FinalPuzzelAct.this, data.message);
                        peopleList.clear();
                        peopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetInventory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void puzzelComplete() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        String level = SharedPreferenceUtility.getInstance(this).getString(GAME_LAVEL);
        map.put("level", level);
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

                   } else if (data.equals("0")) {
                        showToast(FinalPuzzelAct.this, message);
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

    private void showCongrats() {
        final Dialog dialogq = new Dialog(FinalPuzzelAct.this);
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
      //  tv_intro.loadDataWithBaseURL("", FinalHTML, mimeType, encoding, "");
        //tv_intro.getSettings().setLoadWithOverviewMode(true);
       // tv_intro.getSettings().setUseWideViewPort(true);
       tv_intro.setWebViewClient(new WebViewClient());
       tv_intro.getSettings().setJavaScriptEnabled(true);
       tv_intro.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
       tv_intro.getSettings().setPluginState(WebSettings.PluginState.ON);
       tv_intro.getSettings().setMediaPlaybackRequiresUserGesture(false);
       tv_intro.setWebChromeClient(new WebChromeClient());
       tv_intro.loadDataWithBaseURL(null, FinalHTML, "text/html", "UTF-8", null);
        tv_intro.getSettings().setBuiltInZoomControls(true);
        tv_intro.getSettings().setDisplayZoomControls(false);
        Glide.with(getApplicationContext()).load(FinalImage)
                .into(intro_image);
        Button ivSubmit = dialogq.findViewById(R.id.btnDownload);
        imgHeader.setOnClickListener(D ->
                {
                   // dialogq.dismiss();
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
     /*   dialogq.setOnDismissListener(dialog ->{
                startActivity(new Intent(FinalPuzzelAct.this,
                        FinishTeamInfo.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("from", "1")
                        .putExtra("eventId", eventId)
                        .putExtra("eventCode", eventCode));
        });

*/



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}