package com.smsjuegos.quiz.fragments;

import static android.content.ContentValues.TAG;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.ListAdapter;
import com.smsjuegos.quiz.databinding.FragmentListBinding;
import com.smsjuegos.quiz.model.SuccessResGetMyEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {
    FragmentListBinding binding;
    private QuizInterface apiInterface;
    private ListAdapter listAdapter;
    private final ArrayList<SuccessResGetMyEvents.Result> eventList = new ArrayList<>();
    private final ArrayList<SuccessResGetMyEvents.Result> myEventList = new ArrayList<>();
    private String strCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
//        listAdapter = new ListAdapter(getActivity(),eventList);
        binding.tvHeader.setText(R.string.lists);
        binding.imgHeader.setOnClickListener(view1 ->
                {
                    getActivity().onBackPressed();
                }
        );
        binding.btngenerateCode.setOnClickListener(view1 ->
                {
                    booking();
                }
        );

        binding.rlParent.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.rlParent.setVisibility(View.VISIBLE);
        }
        binding.btnUseCode.setOnClickListener(v ->
                {
                    showImageSelection();
                }
        );
        getMyEvents();
        return binding.getRoot();
    }

    public void showImageSelection() {
        strCode = "";
        final Dialog dialog = new Dialog(getActivity());
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
                    strCode = editText.getText().toString();
                    if (!strCode.equalsIgnoreCase("")) {
                        addCode();
                        dialog.dismiss();
                    } else {
                        showToast(getActivity(), "Please enter code");
                    }
                }
        );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getMyEvents() {
        boolean val = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(Constant.SELECTED_LANGUAGE);

        String lang = "";

        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("lang", lang);
        Call<SuccessResGetMyEvents> call = apiInterface.getMyEvent(map);
        call.enqueue(new Callback<SuccessResGetMyEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetMyEvents> call, Response<SuccessResGetMyEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetMyEvents data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventList.clear();
                        myEventList.clear();

                        myEventList.addAll(data.getResult());
                        for (SuccessResGetMyEvents.Result result : myEventList) {
                            if (!result.getEventStatus().equalsIgnoreCase("END")) {
                                if (!result.getId().equalsIgnoreCase("4")
                                        && !result.getId().equalsIgnoreCase("7")) {
                                    eventList.add(result);
                                }
                            }
                        }

                        binding.rvListItems.setHasFixedSize(true);
                        binding.rvListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvListItems.setAdapter(new ListAdapter(getActivity(), eventList));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);

                        eventList.clear();
                        binding.rvListItems.setHasFixedSize(true);
                        binding.rvListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvListItems.setAdapter(new ListAdapter(getActivity(), eventList));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetMyEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void addCode() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_code", strCode);

        Call<ResponseBody> call = apiInterface.applyCode(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    Log.d(TAG, "onResponse: " + jsonObject);

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), message);
                        getMyEvents();

                    } else if (data.equals("0")) {
                        showToast(getActivity(), jsonObject.getString("result"));
                    } else if (data.equals("2")) {
                        showToast(getActivity(), jsonObject.getString("result"));
                    }
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

    private void booking() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Date currentTime = Calendar.getInstance().getTime();
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("book_date", currentTime + "");
        map.put("book_time", currentTime + "");
        map.put("amount", "25");
        map.put("event_id", "1");
        map.put("total_ticket", "1");
        map.put("cart_id", "1");
        map.put("team_name", "cc");
        Call<ResponseBody> call = apiInterface.booking(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        String event_code = result.getString("event_code");
                        binding.btngenerateCode.setClickable(false);
                        binding.btngenerateCode.setFocusable(false);
                        binding.btngenerateCode.setText(event_code);
                        //   Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                      /*  startActivity(new Intent(getActivity(), HomeAct.class));
                        getActivity().finish();*/

//                        getActivity().onBackPressed();

                    } else if (data.equals("0")) {
                        showToast(getActivity(), message);
                    } else if (data.equals("2")) {
                        showToast(getActivity(), jsonObject.getString("result"));
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