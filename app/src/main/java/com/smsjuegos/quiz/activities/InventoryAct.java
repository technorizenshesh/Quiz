package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.adapter.InventoryAdapter;
import com.smsjuegos.quiz.databinding.ActivityInventoryBinding;
import com.smsjuegos.quiz.model.SuccessResGetInventory;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.NetworkAvailablity;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryAct extends AppCompatActivity {

    ActivityInventoryBinding binding;
    private final ArrayList<SuccessResGetInventory.Result> stringArrayList = new ArrayList<>();
    private final ArrayList<SuccessResGetInventory.Result> placeList = new ArrayList<>();
    private final ArrayList<SuccessResGetInventory.Result> peopleList = new ArrayList<>();
    private final ArrayList<SuccessResGetInventory.Result> objectList = new ArrayList<>();
    private InventoryAdapter inventoryAdapter;
    private InventoryAdapter inventoryAdapterplace;
    private InventoryAdapter inventoryAdapterperple;
    private InventoryAdapter inventoryAdapterobject;
    private QuizInterface apiInterface;
    private String eventId, eventCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.inventory));
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");


        // eventId = "1";
        //  eventCode = "969107";
        //    inventoryAdapter = new InventoryAdapter(InventoryAct.this,stringArrayList);
        //    binding.rvINventory.setHasFixedSize(true);
        //   binding.rvINventory.setLayoutManager(new LinearLayoutManager(InventoryAct.this));
        //  binding.rvINventory.setAdapter(inventoryAdapter);
        if (eventId.equalsIgnoreCase("8")&&eventId.equalsIgnoreCase("15")) {
            binding.txt1.setVisibility(View.GONE);
            binding.txt2.setVisibility(View.GONE);
            binding.txt3.setVisibility(View.GONE);
        }
        inventoryAdapterplace = new InventoryAdapter(InventoryAct.this, placeList);
        binding.rvPlaces.setHasFixedSize(true);
        binding.rvPlaces.setLayoutManager(new LinearLayoutManager(InventoryAct.this));
        binding.rvPlaces.setAdapter(inventoryAdapterplace);


        inventoryAdapterperple = new InventoryAdapter(InventoryAct.this, peopleList);
        binding.rvPeople.setHasFixedSize(true);
        binding.rvPeople.setLayoutManager(new LinearLayoutManager(InventoryAct.this));
        binding.rvPeople.setAdapter(inventoryAdapterperple);


        inventoryAdapterobject = new InventoryAdapter(InventoryAct.this, objectList);
        binding.rvObjects.setHasFixedSize(true);
        binding.rvObjects.setLayoutManager(new LinearLayoutManager(InventoryAct.this));
        binding.rvObjects.setAdapter(inventoryAdapterobject);




/*
        binding.rvINventory.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(rv.getChildCount() > 0) {
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                        int action = e.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                rv.requestDisallowInterceptTouchEvent(true);

                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
*/
        if (eventCode.equalsIgnoreCase("game4")) {
            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getGame4Inventory();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                getInventory();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getInventory() {

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
        map.put("lang", lang);
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
                        ArrayList<SuccessResGetInventory.Result> ArrayList = new ArrayList<>();
                        ArrayList.clear();
                        ArrayList.addAll(data.getResult());
                        if (ArrayList.size() >= 1) {
                            for (int i = 0; i < ArrayList.size(); i++) {
                                SuccessResGetInventory.Result res = ArrayList.get(i);
                                if (res.getType().equalsIgnoreCase("Places")) {
                                    placeList.add(res);
                                } else if (res.getType().equalsIgnoreCase("People")) {
                                    peopleList.add(res);
                                } else if (res.getType().equalsIgnoreCase("Objects")) {
                                    objectList.add(res);
                                }
                            }

                            inventoryAdapterplace.notifyDataSetChanged();
                            inventoryAdapterperple.notifyDataSetChanged();
                            inventoryAdapterobject.notifyDataSetChanged();
                        }
                        //    inventoryAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(InventoryAct.this, data.message);
                        stringArrayList.clear();
                        //  inventoryAdapter.notifyDataSetChanged();
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

    private void getGame4Inventory() {
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
        map.put("event_id", eventId);
        map.put("user_id", userId);
        map.put("lang", lang);

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
                        ArrayList<SuccessResGetInventory.Result> ArrayList = new ArrayList<>();
                        ArrayList.clear();
                        ArrayList.addAll(data.getResult());
                        if (ArrayList.size() >= 1) {
                            for (int i = 0; i < ArrayList.size(); i++) {
                                SuccessResGetInventory.Result res = ArrayList.get(i);
                                if (res.getType().equalsIgnoreCase("Places")) {
                                    placeList.add(res);
                                } else if (res.getType().equalsIgnoreCase("People")) {
                                    peopleList.add(res);
                                } else if (res.getType().equalsIgnoreCase("Objects")) {
                                    objectList.add(res);
                                }
                            }

                            inventoryAdapterplace.notifyDataSetChanged();
                            inventoryAdapterperple.notifyDataSetChanged();
                            inventoryAdapterobject.notifyDataSetChanged();
                        }


                        // inventoryAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(InventoryAct.this, data.message);
                        stringArrayList.clear();
                        // inventoryAdapter.notifyDataSetChanged();
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