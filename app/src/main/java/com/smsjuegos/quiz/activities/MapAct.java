package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;
import static com.smsjuegos.quiz.utility.DataManager.showSimpleCancelBtnDialog;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.smsjuegos.quiz.activities.game4.QuestionAnswerAct;
import com.smsjuegos.quiz.model.SuccessResGetInstruction;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.DrawPollyLine;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapAct extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final ArrayList<SuccessResGetInstruction.Result> instructionList = new ArrayList<>();
    Marker[] marker = new Marker[2]; //change length of array according to you
    Marker myMarker;
    GPSTracker gpsTracker;
    private GoogleMap mMap;
    private String eventId, eventCode, strtlat = "", strtlang = "", endlat = "", endlang = "";
    private QuizInterface apiInterface;
    private Snackbar snackbar;
    private double MyLatitude = 0, MyLongitude = 0, MyAltitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        gpsTracker = new GPSTracker(this);

        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");
        //      eventId ="5";
        //      eventCode = "885429";
    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mMap != null) {
            mMap.clear();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        getInstruction();
        if (ActivityCompat.checkSelfPermission(MapAct.this
                , Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MapAct.this
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int position = (int) (marker.getTag());
        Log.e("TAG", "onMarkerClick: position" + position);
        Log.e("TAG",
                "onMarkerClick: instructionList.get(position).getEventType()---" + instructionList.get(position).getEventType());
        Log.e("TAG", "onMarkerClick: " + instructionList.get(position).getGeolocation());
        Log.e("TAG", "onMarkerClick: " + eventId);
        Log.e("TAG", "onMarkerClick: "+instructionList.get(position).getGeolocation() );
        Log.e("TAG", "onMarkerClick: "+instructionList.get(position).getEventId() );
        Log.e("TAG", "onMarkerClick: "+eventId);
        if (eventId.equalsIgnoreCase("5") || eventId.equalsIgnoreCase("8") || eventId.equalsIgnoreCase("1")) {
            handleEventWithLocation(position);
        } else {
            startQuestionAnswerActivity(position);
        }
        return false;
    }

    private void handleEventWithLocation(int position) {
        if (instructionList.get(position).getGeolocation().equalsIgnoreCase("on")) {
            if (gpsTracker != null && gpsTracker.canGetLocation()) {
                MyLatitude = gpsTracker.getLatitude();
                MyLongitude = gpsTracker.getLongitude();
            } else {
                Toast.makeText(getApplicationContext(), "Gps Off", Toast.LENGTH_SHORT).show();
                return;
            }
            double distance = GPSTracker.getDistanceFromPointWithoutAlt(
                    Double.parseDouble(instructionList.get(position).getLat()),
                    Double.parseDouble(instructionList.get(position).getLon()),
                    MyLatitude, MyLongitude);
            Log.e("TAG", "onMarkerClick: distancedistancedistancedistance" + distance);
            if (distance > 200) {
                showSimpleCancelBtnDialog(MapAct.this, R.layout.dialog_distance, distance + "");
            } else {
                Log.e("TAG", "onMarkerClick: " + instructionList.get(position));
                startQuestionAnswerActivity(position);
            }
        } else {
            startQuestionAnswerActivity(position);
        }
    }

    private void startQuestionAnswerActivity(int position) {
        Intent intent = new Intent(MapAct.this, QuestionAnswerAct.class)
                .putExtra("instructionID", instructionList.get(position))
                .putExtra("eventCode", eventCode)
                .putExtra("position", position);
        startActivity(intent);
    }

    private void getInstruction() {

        if (instructionList != null) {
            instructionList.clear();
        }
        ArrayList<SuccessResGetInstruction.Result> data = SharedPreferenceUtility.getInstance(getApplicationContext()).getSuccessResGetInstruction("SuccessResGetInstruction");
        instructionList.addAll(data);
        mMap.setOnMarkerClickListener(MapAct.this);
        marker = new Marker[instructionList.size()];
        int i = 0;
        try {
            LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        }catch (Exception e ){
            Log.e("TAG", "moveCameramoveCamera: "+e.getLocalizedMessage());
            Log.e("TAG", "moveCameramoveCamera: "+e.getMessage());

        }
        for (SuccessResGetInstruction.Result result : instructionList) {

            if (result.getAnswer_status().equalsIgnoreCase("1")) {
                if (result.getLat().equalsIgnoreCase("")) return;
                else {
                    try {
                        marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),
                                "#" + i, "", R.drawable.flag_green);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            } else {

                if (result.getLat().equalsIgnoreCase("")) return;
                else {
                    try {
                        marker[i] = createMarker(i, Double.parseDouble(result.getLat()),
                                Double.parseDouble(result.getLon()),
                                "#" + i, "", R.drawable.flag_red);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            i++;
            if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId").equalsIgnoreCase("")) {

            } else if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime").equalsIgnoreCase("")) {

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
                if ((Integer.parseInt(SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId"))) + 1 == Integer.parseInt(result.id)) {
                    endlat = result.getLat();
                    endlang = result.getLon();


                }
            }

        }
       // LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
     //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
     //   mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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


    }

    private void getInstruction2() {
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
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        map.put("event_code", eventCode);
        map.put("user_id", userId);
        Call<SuccessResGetInstruction> call = apiInterface.getInstruction(map);
        call.enqueue(new Callback<SuccessResGetInstruction>() {
            @Override
            public void onResponse(Call<SuccessResGetInstruction> call, Response<SuccessResGetInstruction> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInstruction data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        instructionList.clear();
                        instructionList.addAll(data.getResult());
                        mMap.setOnMarkerClickListener(MapAct.this);
                        marker = new Marker[instructionList.size()];
                        int i = 0;

                        for (SuccessResGetInstruction.Result result : instructionList) {

                            if (result.getAnswer_status().equalsIgnoreCase("1")) {
                                if (result.getLat().equalsIgnoreCase("")) return;
                                else
                                    marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),
                                            "#" + i, "", R.drawable.flag_green);
                            } else {

                                if (result.getLat().equalsIgnoreCase("")) return;
                                else
                                    marker[i] = createMarker(i, Double.parseDouble(result.getLat()),
                                            Double.parseDouble(result.getLon()),
                                            "#" + i, "", R.drawable.flag_red);
                            }
                            i++;
                            if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId").equalsIgnoreCase("")) {

                            } else if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime").equalsIgnoreCase("")) {

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
                                if ((Integer.parseInt(SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId"))) + 1 == Integer.parseInt(result.id)) {
                                    endlat = result.getLat();
                                    endlang = result.getLon();


                                }
                            }

                        }
                        LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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

                    } else if (data.status.equals("0")) {
                        showToast(MapAct.this, data.message);
                    } else if (data.status.equals("2")) {
                        showToast(MapAct.this, data.message);
                        Thread.sleep(5000);
                        startActivity(new Intent(MapAct.this, HomeAct.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetInstruction> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    protected Marker createMarker(int position, double latitude, double longitude, String title,
                                  String snippet, int iconResID) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(iconResID);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 9f));
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(icon)
                .snippet(snippet));
        myMarker.setTag(position);
        return myMarker;
    }


}
