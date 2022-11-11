package com.my.quiz.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.adapter.InventoryAdapter;
import com.my.quiz.databinding.ActivityInventoryBinding;
import com.my.quiz.model.SuccessResGetInventory;
import com.my.quiz.model.SuccessResGetInventory;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

public class InventoryAct extends AppCompatActivity {

    ActivityInventoryBinding binding;
    private ArrayList<SuccessResGetInventory.Result> stringArrayList = new ArrayList<>();
    private InventoryAdapter inventoryAdapter;
    private QuizInterface apiInterface;
    private String eventId,eventCode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_inventory);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.inventory));
        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");

        inventoryAdapter = new InventoryAdapter(InventoryAct.this,stringArrayList);
        binding.rvINventory.setHasFixedSize(true);
        binding.rvINventory.setLayoutManager(new LinearLayoutManager(InventoryAct.this));
        binding.rvINventory.setAdapter(inventoryAdapter);

        if(eventCode.equalsIgnoreCase("game4") )
        {
            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getGame4Inventory();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getInventory();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getInventory()
    {

        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);

        Call<SuccessResGetInventory> call = apiInterface.getAllInventory(map);

        call.enqueue(new Callback<SuccessResGetInventory>() {
            @Override
            public void onResponse(Call<SuccessResGetInventory> call, Response<SuccessResGetInventory> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInventory data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        stringArrayList.clear();
                        stringArrayList.addAll(data.getResult());

                        inventoryAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(InventoryAct.this, data.message);
                        stringArrayList.clear();
                        inventoryAdapter.notifyDataSetChanged();
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

    private void getGame4Inventory()
    {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("user_id", userId);

        Call<SuccessResGetInventory> call = apiInterface.getGame4AllInventory(map);

        call.enqueue(new Callback<SuccessResGetInventory>() {
            @Override
            public void onResponse(Call<SuccessResGetInventory> call, Response<SuccessResGetInventory> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInventory data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        stringArrayList.clear();
                        stringArrayList.addAll(data.getResult());

                        inventoryAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(InventoryAct.this, data.message);
                        stringArrayList.clear();
                        inventoryAdapter.notifyDataSetChanged();
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

}