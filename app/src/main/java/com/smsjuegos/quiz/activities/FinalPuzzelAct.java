package com.smsjuegos.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

public class FinalPuzzelAct extends AppCompatActivity {

    ActivityFinalPuzzelBinding binding;
    private String eventId,eventCode;
    private QuizInterface apiInterface;

    private ArrayList<SuccessResGetInventory.Result> peopleList = new ArrayList<>();
    private ArrayList<SuccessResGetInventory.Result> placesList = new ArrayList<>();
    private ArrayList<SuccessResGetInventory.Result> objectList = new ArrayList<>();

    private FinalPuzzelAdapter peopleAdapter;
    private FinalPuzzelAdapter placesAdapter;
    private FinalPuzzelAdapter objectAdapter;

    private int peopleSelected=-1;
    private int objectSelected=-1;
    private int placeSelected=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_final_puzzel);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.final_puzzel));

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

        binding.rvObjects.setHasFixedSize(true);
        binding.rvObjects.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this,3));
        binding.rvObjects.setAdapter(objectAdapter);
        binding.rvPeople.setHasFixedSize(true);
        binding.rvPeople.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this,3));
        binding.rvPeople.setAdapter(peopleAdapter);
        binding.rvPlaces.setHasFixedSize(true);

        binding.rvPlaces.setLayoutManager(new GridLayoutManager(FinalPuzzelAct.this,3));
        binding.rvPlaces.setAdapter(placesAdapter);

        getObject();
        getPeople();
        getPlaces();

        binding.btnFinsh.setOnClickListener(v ->
                {
                    if(placeSelected == -1)
                    {
                        showToast(FinalPuzzelAct.this,""+getString(R.string.select_places_image));
                    }else  if(peopleSelected == -1)
                    {
                        showToast(FinalPuzzelAct.this,""+getString(R.string.select_people_image));
                    }else  if(objectSelected == -1)
                    {
                        showToast(FinalPuzzelAct.this,""+getString(R.string.select_object_image));
                    }else
                    {
                        if(placesList.get(placeSelected).getFinalPuzzleStatus().equalsIgnoreCase("Yes"))
                        {
                            if(peopleList.get(peopleSelected).getFinalPuzzleStatus().equalsIgnoreCase("Yes"))
                            {
                                if(objectList.get(objectSelected).getFinalPuzzleStatus().equalsIgnoreCase("Yes"))
                                {
                                     puzzelComplete();
                                }else
                                {
                                    showToast(FinalPuzzelAct.this,getString(R.string.wrong_image));
                                }
                            }else
                            {
                                showToast(FinalPuzzelAct.this,getString(R.string.wrong_image));
                            }

                        }else
                        {
                            showToast(FinalPuzzelAct.this,getString(R.string.wrong_image));
                        }
                    }
                }
                );
    }

    private void getPeople()
    {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("type", "People");
        Call<SuccessResGetInventory> call = apiInterface.getInventory(map);

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
                        peopleList.clear();
                        peopleList.addAll(data.getResult());
                        peopleAdapter.notifyDataSetChanged();
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

    private void getObject()
    {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        boolean val =  SharedPreferenceUtility.getInstance(getApplicationContext()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";

        if(!val)
        {
            lang = "en";
        } else
        {
            lang = "sp";
        }
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("type", "Objects");
        map.put("lang", lang);
        Call<SuccessResGetInventory> call = apiInterface.getInventory(map);

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

                        objectList.clear();
                        objectList.addAll(data.getResult());
                        objectAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(FinalPuzzelAct.this, data.message);
                        objectList.clear();
                        objectAdapter.notifyDataSetChanged();
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

    private void getPlaces()
    {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        boolean val =  SharedPreferenceUtility.getInstance(getApplicationContext()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";

        if(!val)
        {
            lang = "en";
        } else
        {
            lang = "sp";
        }
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("type", "Places");
        map.put("lang", lang);

        Call<SuccessResGetInventory> call = apiInterface.getInventory(map);

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
                        placesList.clear();
                        placesList.addAll(data.getResult());
                        placesAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        showToast(FinalPuzzelAct.this, data.message);
                        placesList.clear();
                        placesAdapter.notifyDataSetChanged();
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

    public void puzzelComplete()
    {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        Call<ResponseBody> call = apiInterface.puzzelCompleted(map);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                        startActivity(new Intent(FinalPuzzelAct.this, FinishTeamInfo.class)
                                .putExtra("from", "1")
                                .putExtra("eventId", eventId)
                                .putExtra("eventCode", eventCode));

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


}