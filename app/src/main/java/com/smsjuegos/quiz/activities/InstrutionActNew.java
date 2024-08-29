package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.GAME_LAVEL;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;
import static com.smsjuegos.quiz.utility.DataManager.showSimpleCancelBtnDialog;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.cardigo.CardigoPuzzleFinalActivity;
import com.smsjuegos.quiz.activities.game4.QuestionAnswerAct;
import com.smsjuegos.quiz.activities.puzzle.SamplePuzzleActivity;
import com.smsjuegos.quiz.databinding.ActivityInstrutionNewBinding;
import com.smsjuegos.quiz.model.SuccessResGetInstruction;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.ApiClient2;
import com.smsjuegos.quiz.retrofit.ApiClient3;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.DataParser;
import com.smsjuegos.quiz.utility.DataParser2;
import com.smsjuegos.quiz.utility.DrawPollyLine;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.LocationUtil;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstrutionActNew extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationUtil.LocationListener {
    private final ArrayList<SuccessResGetInstruction.Result> instructionList = new ArrayList<>();
    String TAG = "InstrutionActNew";
    ActivityInstrutionNewBinding binding;
    Marker[] marker = new Marker[2]; //change length of array according to you
    Marker myMarker;
    GPSTracker gpsTracker;
    private Dialog mDialog;
    private QuizInterface apiInterface;
    private String eventId, eventCode, strtlat = "", strtlang = "", endlat = "", endlang = "", getDis = "";
    private GoogleMap mMap;
    Circle circle;
    private Handler handler;
    private Runnable runnable;
    private boolean isRunning;
    private Long result;
    private Snackbar snackbar;
    private double MyLatitude = 0, MyLongitude = 0, MyAltitude = 0;
    LocationUtil mLocationUtil;
    LatLng location;
    String updateTime ="";
    String pauseId="",gamePlayPauseStatus="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instrution_new);
        gpsTracker = new GPSTracker(this);
        handler = new Handler();
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.imgHeader.setOnClickListener(v -> finish());
        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");
        //  eventId = "5";
        //    eventCode = "934121";

        Log.e("TAG", "eventIdeventIdeventIdeventId: " + eventId);
        Log.e("TAG", "eventCodeeventCodeeventCode: " + eventCode);
        //  getEventDetails();
        binding.tvInstruction.setOnClickListener(v -> {
            startActivity(new Intent(InstrutionActNew.this, InstrutionAct.class)
                    .putExtra("eventId", eventId).putExtra("eventCode", eventCode));
            onStop();
        });

        binding.tvMap.setOnClickListener(v -> {
            SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId", "");
            startActivity(new Intent(InstrutionActNew.this, MapAct.class).putExtra
                    ("eventId", eventId).putExtra("eventCode", eventCode));
            onStop();

        });


        binding.tvINventory.setOnClickListener(v -> {
            startActivity(new Intent(InstrutionActNew.this,
                    InventoryAct.class).putExtra("eventId", eventId)
                    .putExtra("eventCode", eventCode));
            onStop();

        });

        binding.tvFinalPuzzel.setOnClickListener(v -> {

            Log.e(TAG, "showMainMenu: " + eventId);
            if (eventId.equals("8") | eventId.equals("15") | eventId.equals("18")
                    | eventId.equals("19") | eventId.equals("34") | eventId.equals("28")) {
                startActivity(new Intent(InstrutionActNew
                        .this, CardigoPuzzleFinalActivity.class).putExtra("eventId", eventId)
                        .putExtra("eventCode", eventCode));

            } else {
                startActivity(new Intent(InstrutionActNew.this,
                        FinalPuzzelAct.class).putExtra("eventId", eventId)
                        .putExtra("eventCode", eventCode));
            }
            onStop();

        });


        binding.imgPlayPause.setOnClickListener(v -> {
           if(location!=null){
             if(gamePlayPauseStatus.equalsIgnoreCase("START")){
                 alertDialogPlayPause("Whenever you restart the game, you will have to be at around this location.","STOP",MyLatitude,MyLongitude,pauseId);
             }
             else {
                 alertDialogPlayPause("Are you sure you're where you left it?","START",MyLatitude,MyLongitude,pauseId);

             }
           }
        });

        getTimer();

    }

    private void alertDialogPlayPause(String msg,String status,double lat,double lon,String pauseId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                .setMessage(msg)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Handle positive button click
                    // You can add your code here
                    dialog.dismiss();
                    PlayPauseTimer(status,lat,lon,pauseId);

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle negative button click
                    // You can add your code here
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        //SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId", "");
        super.onResume();

        if (mMap != null) {
            mMap.clear();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(InstrutionActNew.this);
        gpsTracker = new GPSTracker(InstrutionActNew.this);

        mLocationUtil = new LocationUtil(InstrutionActNew.this);

        if (gpsTracker.canGetLocation()) {
            //   MyLatitude = gpsTracker.getLatitude();
            //   MyLongitude = gpsTracker.getLongitude();
            //    MyAltitude = gpsTracker.getAltitude();

        } else {
            Toast.makeText(getApplicationContext(), "Gps Off", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mLocationUtil.fetchApproximateLocation(this);
        mLocationUtil.fetchPreciseLocation(this);

        // mMap.setMyLocationEnabled(true);
        // googleMap.setLocationSource();


    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (marker.getTitle().equalsIgnoreCase("My Location")) {

        } else {
            int position = (int) (marker.getTag());
            SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId", "");
            if (snackbar != null) {
                snackbar.dismiss();
            }
            Log.e(TAG, "onMarkerClick: " + instructionList.get(position).getGeolocation());
            Log.e(TAG, "onMarkerClick: " + instructionList.get(position).getEventId());
            Log.e(TAG, "onMarkerClick: " + eventId);
            if (eventId.equalsIgnoreCase("19")  // Rescate Gaudalajara
                    || eventId.equalsIgnoreCase("18")
                    || eventId.equalsIgnoreCase("28")

                    || eventId.equalsIgnoreCase("1")


                    || eventId.equalsIgnoreCase("5") // crime
                    || eventId.equalsIgnoreCase("20") // crime

                    || eventId.equalsIgnoreCase("15") // zoombi (Mexico,Gudaljar)


                    || eventId.equalsIgnoreCase("8")   //Codigo (Mexico)

                    || eventId.equalsIgnoreCase("4")  // virus
                    || eventId.equalsIgnoreCase("7")  // Amenaza Nuclear
                    || eventId.equalsIgnoreCase("17") // lajoya
                    || eventId.equalsIgnoreCase("39") //

                    || eventId.equalsIgnoreCase("24") // mission magica
                    || eventId.equalsIgnoreCase("22") // mission magica Gaudalajara
                    || eventId.equalsIgnoreCase("31") // mission magica Monterrey
                    || eventId.equalsIgnoreCase("32") // mission magica Monterrey


                    || eventId.equalsIgnoreCase("40") // riddle (Mexico)

            ) {
                handleEventWithLocation(position);
            } else {
                startQuestionAnswerActivity(position);

            }

        }
        return false;
    }

    private void handleEventWithLocation(int position) {
        if (instructionList.get(position).getGeolocation().equalsIgnoreCase("on")) {
            //  if (gpsTracker != null && gpsTracker.canGetLocation()) {
            //      MyLatitude = gpsTracker.getLatitude();
            //       MyLongitude = gpsTracker.getLongitude();
            //    }

            if (location != null) {
                MyLatitude = location.latitude;
                MyLongitude = location.longitude;
            } else {
                Toast.makeText(getApplicationContext(), "Gps Off", Toast.LENGTH_SHORT).show();
                return;
            }
            double distance = GPSTracker.getDistanceFromPointWithoutAlt(
                    Double.parseDouble(instructionList.get(position).getLat()),
                    Double.parseDouble(instructionList.get(position).getLon()),
                    MyLatitude, MyLongitude);


/*
            double distance =  Double.parseDouble(getDistanceBtTwoPoints(InstrutionActNew.this,
                    new LatLng(Double.parseDouble(instructionList.get(position).getLat().trim()),
                    Double.parseDouble(instructionList.get(position).getLon().trim())),
                    new LatLng(MyLatitude,MyLongitude)));
*/

            //       LatLng latLngs1 = new LatLng(Double.parseDouble(strtlat), Double.parseDouble(strtlang));

            Log.e("location=====", MyLatitude + "," + MyLongitude + "");

            Log.e("TAG", "onMarkerClick: distancedistancedistancedistance" + distance);
            Toast.makeText(getApplicationContext(), "" + distance, Toast.LENGTH_SHORT).show();


       /*     if (instructionList.get(position).JigsawPuzzleStatus.equalsIgnoreCase("enable")) {
                startActivity(new Intent(InstrutionActNew.this, SamplePuzzleActivity.class)
                        .putExtra("myData",instructionList.get(position))
                        .putExtra("eventCode",eventCode));
                String urlImg[] = instructionList.get(position).getJigsawPuzzleImage().split(".png");
                for(int i =0;i<urlImg.length;i++){
                    Log.e("split value====",i+"======="+urlImg[i]);
                }

            } else startQuestionAnswerActivity(position);*/


            if (distance > 100) {  //  if (distance >= 150)         {
                showSimpleCancelBtnDialog(InstrutionActNew.this, R.layout.dialog_distance, distance + "");
            } else {
                Log.e("TAG", "onMarkerClick: " + instructionList.get(position));
                if (instructionList.get(position).JigsawPuzzleStatus.equalsIgnoreCase("enable")) {
                    startActivity(new Intent(InstrutionActNew.this, SamplePuzzleActivity.class)
                            .putExtra("myData",instructionList.get(position))
                            .putExtra("eventCode",eventCode));
                    String urlImg[] = instructionList.get(position).getJigsawPuzzleImage().split(".png");
                      for(int i =0;i<urlImg.length;i++){
                          Log.e("split value====",i+"======="+urlImg[i]);
                      }

                } else startQuestionAnswerActivity(position);
            }

        } else {
            startQuestionAnswerActivity(position);


        }
    }


    private void startQuestionAnswerActivity(int position) {
        Intent intent = new Intent(InstrutionActNew.this, QuestionAnswerAct.class)
                .putExtra("instructionID", instructionList.get(position))
                .putExtra("eventCode", eventCode)
                .putExtra("position", position);
        startActivity(intent);
    }

    /**/
    private void getTimer() {
        handler = new Handler();
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("user_id", userId);
        String level = SharedPreferenceUtility.getInstance(this).getString(GAME_LAVEL);
        map.put("level", level);
        Call<ResponseBody> call = apiInterface.get_event_time(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    result = jsonObject.getLong("result");
                    pauseId = jsonObject.getString("pause_id");
                    gamePlayPauseStatus = jsonObject.getString("pause_status");
                    if (!jsonObject.getString("lat").equalsIgnoreCase("")) {
                        MyLatitude = Double.parseDouble(jsonObject.getString("lat"));
                        MyLongitude = Double.parseDouble(jsonObject.getString("lon"));
                    }
                    result = result;
                    if (result == null & result <= 0) {
                    } else {

                       if(gamePlayPauseStatus.equalsIgnoreCase("START")) {
                           binding.imgPlayPause.setImageResource(R.drawable.ic_play);
                           isRunning = false;
                           startTimer();
                       }
                       else {
                           binding.imgPlayPause.setImageResource(R.drawable.ic_pause);
                           isRunning = true;
                           ShowStopTimer(result);

                       }
                    }
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





    private void PlayPauseTimer(String status,double lat,double lon,String pauseId) {
        DataManager.getInstance().showProgressMessage(InstrutionActNew.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("event_status", status);
        map.put("user_id", userId);
        map.put("lat", lat+"");
        map.put("lon", lon+"");
        map.put("pause_id", pauseId);
        map.put("total_time", result+"");


        Call<ResponseBody> call = apiInterface.eventTimePlayPause(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if(status.equalsIgnoreCase("1")){
//                       /* JSONArray resultArray = jsonObject.getJSONArray("result");
//                        JSONObject resultObj = resultArray.getJSONObject(0);
//                        result = Long.parseLong(resultObj.getString("total_time"));
//                        gamePlayPauseStatus = resultObj.getString("event_status");
//                        MyLatitude = Double.parseDouble(resultObj.getString("lat"));
//                        MyLongitude = Double.parseDouble(resultObj.getString("lon"));
//
//                        if (result == null & result <= 0) {
//                        } else {
//
//                            if(gamePlayPauseStatus.equalsIgnoreCase("START")) {
//                                binding.imgPlayPause.setImageResource(R.drawable.ic_play);
//                                startTimer();
//                            }
//                            else {
//                                binding.imgPlayPause.setImageResource(R.drawable.ic_pause);
//                                ShowStopTimer(result);
//
//                            }
//
//                            mMap.clear();
//                            getInstruction();*/


  //                      }
                        getTimer();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DataManager.getInstance().hideProgressMessage();

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
        if (instructionList != null) {
            instructionList.clear();
        }
        ArrayList<SuccessResGetInstruction.Result> data = SharedPreferenceUtility.getInstance(getApplicationContext()).getSuccessResGetInstruction("SuccessResGetInstruction");
        instructionList.addAll(data);


        // For check my current location

        Log.e(TAG, "getInstruction======size: " + instructionList.size());




/*
        for(int i =0;i<instructionList.size();i++){
           // Long cordinate
         //   instructionList.get(i).setLat("25.8802392");
        //    instructionList.get(i).setLon("78.3143733");

            // Shore Co ordinate under 100 m
            instructionList.get(i).setLat("25.879474");
            instructionList.get(i).setLon("78.313540");


        }
*/



        marker = new Marker[instructionList.size()];
        int i = 0;
        try {
            LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));


        } catch (Exception e) {
            Log.e(TAG, "moveCameramoveCamera: " + e.getLocalizedMessage());
            Log.e(TAG, "moveCameramoveCamera: " + e.getMessage());
        }

        for (SuccessResGetInstruction.Result result : instructionList) {

            if (result.getAnswer_status().equalsIgnoreCase("1")) {
                if (result.getLat().equalsIgnoreCase("")) {

                } else {
                    try {
                     //   marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),
                      //          "#" + i, "", R.drawable.flag_green,result.getEventId(),result.getId());
                           int m = i + 1;
                        marker[i] = addMarkerWithNumber(i,1,"",Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),m+"",result.getEventId());

                        Log.e("Lat Lon Position === ", +i + "  " + result.getLat() + " , " + result.getLon());
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "onMarkerClick: NumberFormatExceptionNumberFormatException" + result.getId());
                       // marker[i] = createMarker(i, convertDMSToDecimal(result.getLat()), convertDMSToDecimal(result.getLon()),
                       //         "#" + i, "", R.drawable.flag_green,result.getEventId(),result.getId());
                        int m = i + 1;
                        marker[i] = addMarkerWithNumber(i,1,"",Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),m+"",result.getEventId());



                        continue;
                    }
                }
            } else {

                if (result.getLat().equalsIgnoreCase("")) {

                } else {
                    try {
                    //    marker[i] = createMarker(i, Double.parseDouble(result.getLat()),
                     //           Double.parseDouble(result.getLon()),
                             //   "#" + i, "", R.drawable.flag_red,result.getEventId(),result.getId());
                        int m = i + 1;
                        marker[i] = addMarkerWithNumber(i,2,"",Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),m+"",result.getEventId());

                        Log.e("Lat Lon Position === ", +i + "  " + result.getLat() + " , " + result.getLon());


                    } catch (NumberFormatException e) {
                        Log.e(TAG, "onMarkerClick: NumberFormatExceptionNumberFormatException" + result.getId());
                      //  marker[i] = createMarker(i, convertDMSToDecimal(result.getLat()),
                       //         convertDMSToDecimal(result.getLon()),
                          //    "#" + i, "", R.drawable.flag_red,result.getEventId(),result.getId());
                        int m = i + 1;
                        marker[i] = addMarkerWithNumber(i,2,"",Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),m+"",result.getEventId());


                        continue;
                    }
                }

            }
            i++;

            Log.e(TAG, "getInstruction: ------------------------------" + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId"));
            Log.e(TAG, "getInstruction: ------------------------------" + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime"));
            if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId").equalsIgnoreCase("") | SharedPreferenceUtility.getInstance(getApplicationContext())
                    .getString("ArrTime").equalsIgnoreCase("")) {
                strtlat = "";
                strtlang = "";
                endlat = "";
                endlang = "";
            } else {
                Log.e("TAG", "SharedPreferenceUtility: " + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId"));
                Log.e("TAG", "SharedPreferenceUtility: " + result.id);
                if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId").equalsIgnoreCase(result.id)) {
                    strtlat = result.getLat();
                    strtlang = result.getLon();
                    //   endlat    = data.getResult().get(i + 1).getLat();
                    //  endlang  = data.getResult().get(i + 1).getLon();
                    Log.e("TAG", "SharedPreferenceUtility: " + strtlat + strtlang + endlat + endlang);

                }
                if ((Integer.parseInt(SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId"))) + 1
                        == Integer.parseInt(result.id)) {
                    endlat = result.getLat();
                    endlang = result.getLon();


                }
            }

        }









        try {
            if (strtlang.equalsIgnoreCase("")) {

            } else {
                //     List<LatLng>latLngs = new ArrayList<>();
                LatLng latLngs1 = new LatLng(Double.parseDouble(strtlat), Double.parseDouble(strtlang));
                LatLng latLngs2 = new LatLng(Double.parseDouble(endlat), Double.parseDouble(endlang));
                // latLngs.add(new LatLng(Double.parseDouble(endlat),Double.parseDouble(endlang)));
                //     drawPolyLineOnMap(latLngs);

                DrawPollyLine.get(getApplicationContext())
                        .setOrigin(latLngs1)
                        .setDestination(latLngs2)
                        .execute(latLngs -> {
                            PolylineOptions options = new PolylineOptions();
                            options.addAll(latLngs);
                            options.color(Color.GREEN);
                            options.width(10);
                            options.startCap(new SquareCap());
                            options.endCap(new SquareCap());
                            Polyline line = mMap.addPolyline(options);
                            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                            valueAnimator.setDuration(2000); // 2 seconds
                            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                            valueAnimator.addUpdateListener(animator -> {
                                int alpha = (int) animator.getAnimatedValue();
                                line.setColor(Color.BLACK);
                            });
                            valueAnimator.start();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(latLngs1);
                            final LatLngBounds bounds = builder.build();
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                            mMap.animateCamera(cu);
                            snackbar = Snackbar.make(getWindow().getDecorView().getRootView()
                                    , "", Snackbar.LENGTH_INDEFINITE);
                            View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
                            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                            // now change the layout of the snackbar
                            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                            snackbarLayout.setPadding(0, 0, 0, 0);
                            TextView textView2 = customSnackView.findViewById(R.id.textView2);
                            textView2.setText(getString(R.string.you_have_only) + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime") + getString(R.string.minutes_to_reach_next_checkpoint_hurry_up));
                            Button bGotoWebsite = customSnackView.findViewById(R.id.gotoWebsiteButton);
                            bGotoWebsite.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "Reaching Check....", Toast.LENGTH_SHORT).show();
                                    SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId", "");
                                    SharedPreferenceUtility.getInstance(getApplicationContext()).putString("ArrTime", "");
                                    Log.e(TAG, "getInstruction: ------------------------------" + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId"));
                                    Log.e(TAG, "getInstruction: ------------------------------" + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime"));


                                    snackbar.dismiss();
                                }
                            });
                            snackbarLayout.addView(customSnackView, 0);
                            snackbar.show();
                        });
            }
        } catch (Exception e) {
            Log.e("TAG", "onResponse: " + e.getLocalizedMessage());
            Log.e("TAG", "onResponse: " + e.getMessage());
            Log.e("TAG", "onResponse: " + e.getCause());
        }
        //getInstruction2();
    }






    public static double convertDMSToDecimal(String dmsCoordinate) {
        // Split degrees, minutes, and seconds
        String[] parts = dmsCoordinate.split("[°'\"NWE]");

        double degrees = Double.parseDouble(parts[0]);
        double minutes = Double.parseDouble(parts[1]);
        double seconds = Double.parseDouble(parts[2]);

        // Calculate decimal degrees
        double decimalDegrees = degrees + (minutes / 60.0) + (seconds / 3600.0);

        // Adjust for South or West coordinates (negative values)
        if (dmsCoordinate.contains("S") || dmsCoordinate.contains("W")) {
            decimalDegrees = -decimalDegrees;
        }

        return decimalDegrees;
    }

    private void getInstruction2() {
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
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        map.put("event_code", eventCode);
        String level = SharedPreferenceUtility.getInstance(this).getString(GAME_LAVEL);
        map.put("level", level);
        Call<SuccessResGetInstruction> call = apiInterface.getInstruction(map);
        call.enqueue(new Callback<SuccessResGetInstruction>() {
            @Override
            public void onResponse(Call<SuccessResGetInstruction> call, Response<SuccessResGetInstruction> response) {
                try {
                    SuccessResGetInstruction data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplicationContext()).putSuccessResGetInstruction("", data.getResult());
                    } else if (data.status.equals("0")) {
                        showToast(InstrutionActNew.this, data.message);
                    } else if (data.status.equals("2")) {
                        showToast(InstrutionActNew.this, data.message);
                        Thread.sleep(5000);
                        startActivity(new Intent(InstrutionActNew.this, HomeAct.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetInstruction> call, Throwable t) {
                call.cancel();
            }
        });
    }


    protected Marker createMarker(int position, double latitude, double longitude, String title, String snippet, int iconResID,String eventId,String id) {
        BitmapDescriptor icon = createCustomMarkerIconWithNumber(iconResID,id,this);     //BitmapDescriptorFactory.fromResource(iconResID);
        myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(id).icon(icon).snippet(snippet));
        myMarker.setTag(position);
      //  getNumberedMarkerIcon(Integer.parseInt(eventId));
        Log.e("marker event id====",eventId);

       /* if(eventId.equalsIgnoreCase("15")) {
            // Set custom InfoWindowAdapter
           // mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(id));
            // Show info window immediately
            if (myMarker != null) {
                myMarker.showInfoWindow();
                Log.e("marker event id222====",eventId);

            }
        }*/


        return myMarker;
    }




    private Marker addMarkerWithNumber(int position,int type, String title, double latitude, double longitude,String number,String eventId) {
        // Inflate the custom marker view
        View markerView=null;
        if(type==1) markerView =    LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
         else markerView = LayoutInflater.from(this).inflate(R.layout.marker_layout_red, null);

        TextView numberTextView = markerView.findViewById(R.id.marker_number);
        Log.e("check marker number===",number);
        if (eventId.equalsIgnoreCase("15")){
            numberTextView.setVisibility(View.VISIBLE);
            numberTextView.setText("#"+number);
        }
        else {
            numberTextView.setVisibility(View.GONE);
        }


        // Create the marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(createBitmapFromView(markerView)));

        // Add the marker to the map
        myMarker =  mMap.addMarker(markerOptions);
        myMarker.setTag(position);
        return myMarker;
    }

    private Bitmap createBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



    private BitmapDescriptor createCustomMarkerIconWithNumber(int drawableResId, String number, Context context) {
        // Load the base icon from resources
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, drawableResId);
        if (bitmapDrawable == null) {
            Log.e("CustomMarker", "Drawable resource not found: " + drawableResId);
            return BitmapDescriptorFactory.defaultMarker(); // Return a default marker if the drawable is null
        }

        Bitmap baseBitmap = bitmapDrawable.getBitmap();
        if (baseBitmap == null) {
            Log.e("CustomMarker", "Bitmap from drawable is null.");
            return BitmapDescriptorFactory.defaultMarker(); // Return a default marker if the bitmap is null
        }

        // Scale the base bitmap to a suitable size if needed
        int scaledWidth = 80; // Width of the final icon
        int scaledHeight = 80; // Height of the final icon
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(baseBitmap, scaledWidth, scaledHeight, true);
        if (scaledBitmap.getWidth() <= 0 || scaledBitmap.getHeight() <= 0) {
            Log.e("CustomMarker", "Scaled bitmap dimensions are invalid: " + scaledBitmap.getWidth() + "x" + scaledBitmap.getHeight());
            return BitmapDescriptorFactory.defaultMarker(); // Return a default marker if the scaled bitmap dimensions are invalid
        }

        // Create a mutable bitmap to draw on
        Bitmap mutableBitmap = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        // Define paint for the text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK); // Text color
        textPaint.setTextSize(20); // Text size (adjust as needed)
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);

        // Define paint for the background
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE); // Background color
        backgroundPaint.setAntiAlias(true);

        // Calculate text size and background
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textWidth = textPaint.measureText(number);
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        // Define padding for the background rectangle
        int padding = 10;
        float rectWidth = textWidth + padding * 2;
        float rectHeight = textHeight + padding * 2;

        // Calculate position for the text and background to be placed above the marker
        float xPos = mutableBitmap.getWidth() / 2;
        float yPos = -rectHeight / 2; // Position above the marker, adjust if needed

        // Draw the background rectangle centered above the marker
        canvas.drawRect(xPos - rectWidth / 2, yPos - rectHeight / 2, xPos + rectWidth / 2, yPos + rectHeight / 2, backgroundPaint);

        // Draw the text on the bitmap, ensuring it is centered within the rectangle
        canvas.drawText(number, xPos, yPos + rectHeight / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, textPaint);

        // Convert the bitmap to a BitmapDescriptor
        return BitmapDescriptorFactory.fromBitmap(mutableBitmap);
    }


    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if(isRunning) {
                    Log.d(TAG, "millismillismillis: " + result);
                    updateTimer(result);
                    handler.postDelayed(this, 1000);
                }// Update every second
            }
        };
        startRunnable(); // Start the timer
    }

    private void updateTimer(long milliseconds) {
        Log.e(TAG, "updateTimer: milliseconds ---  " + milliseconds);

        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        Log.e(TAG, "updateTimer: minutes ---  " + minutes);
        Log.e(TAG, "updateTimer: seconds ---  " + seconds);
        Log.e(TAG, "updateTimer: hours   ---  " + hours);

        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        updateTime = time;
        binding.tvHeader.setText(time);
        result = result + 1000;
    }

    private void ShowStopTimer(long milliseconds) {
        Log.e(TAG, "ShowStopTimer: milliseconds ---  " + milliseconds);

        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        Log.e(TAG, "updateTimer: minutes ---  " + minutes);
        Log.e(TAG, "updateTimer: seconds ---  " + seconds);
        Log.e(TAG, "updateTimer: hours   ---  " + hours);

        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        updateTime = time;
        binding.tvHeader.setText(time);
        if(handler!=null){
            result = result + 1000;
            stopRunnable();
        }
    }


    private void startRunnable() {
        if (!isRunning) {
            isRunning = true;
            handler.post(runnable);
        }
    }

    private void stopRunnable() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }


    @Override
    protected void onStop() {
       // handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the timer and remove the handler callbacks
       // handler.removeCallbacks(runnable);
       // PlayPauseTimer("STOP",MyLatitude,MyLongitude,pauseId);

    }

    public String getDistanceBtTwoPoints(Context context, LatLng origin, LatLng destination) {

        String URL = DrawPollyLine.getPolyLineUrl(context, origin, destination);
        QuizInterface quizInterface = ApiClient2.getClient().create(QuizInterface.class);
        quizInterface.getURL(URL).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    Log.e("Check response=====", object.toString());
                    DataParser2 parser = new DataParser2();
                    getDis = parser.parse2(object);
                    Log.e("Distance===", getDis);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return getDis;
    }

    @Override
    public void onLocationReceived(@NonNull LatLng location) {
        if (location != null) {
            this.location = location;
            mMap.setOnMarkerClickListener(InstrutionActNew.this);
            mMap.clear();
            getInstruction();
            MyLatitude = location.latitude;
            MyLongitude = location.longitude;
            MyAltitude = gpsTracker.getAltitude();

            Log.e("Try new location====", MyLatitude + "," + MyLongitude + "");

            if (ActivityCompat.checkSelfPermission(InstrutionActNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InstrutionActNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (MyLatitude != 0) {
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                bounds.contains(new LatLng(MyLatitude, MyLongitude));
            }

            mMap.addMarker(new MarkerOptions()
                    .title("My Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loca_green))
                    .position(new LatLng(MyLatitude, MyLongitude))
                    .flat(true));


            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(MyLatitude, MyLongitude))
                    .radius(100)
                    .strokeWidth(0)
                    .strokeColor(Color.parseColor("#2271cce7"))
                    .fillColor(Color.parseColor("#2271cce7")));


        }
    }



    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mWindow;
        private String id;

        CustomInfoWindowAdapter(String flagId) {
            mWindow = getLayoutInflater().inflate(R.layout.layout_custom_info_window, null);
            id = flagId;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        private void render(Marker marker, View view) {
            TextView title = view.findViewById(R.id.title);
           // TextView snippet = view.findViewById(R.id.snippet);

         //   title.setText(marker.getTitle());
         //   snippet.setText(marker.getSnippet());


            title.setText(id);
           // snippet.setText("1");
        }
    }
}




