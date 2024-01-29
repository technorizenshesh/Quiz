package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.GAME_LAVEL;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;
import static com.smsjuegos.quiz.utility.DataManager.showSimpleCancelBtnDialog;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.smsjuegos.quiz.databinding.ActivityInstrutionNewBinding;
import com.smsjuegos.quiz.model.SuccessResGetInstruction;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DrawPollyLine;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstrutionActNew extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final ArrayList<SuccessResGetInstruction.Result> instructionList = new ArrayList<>();
    String TAG = "InstrutionActNew";
    ActivityInstrutionNewBinding binding;
    Marker[] marker = new Marker[2]; //change length of array according to you
    Marker myMarker;
    GPSTracker gpsTracker;
    private Dialog mDialog;
    private QuizInterface apiInterface;
    private String eventId, eventCode, strtlat = "", strtlang = "", endlat = "", endlang = "";
    private GoogleMap mMap;
    private Handler handler;
    private Runnable runnable;
    private Long result;
    private Snackbar snackbar;
    private double MyLatitude = 0, MyLongitude = 0, MyAltitude = 0;

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

        if (gpsTracker.canGetLocation()) {
            MyLatitude = gpsTracker.getLatitude();
            MyLongitude = gpsTracker.getLongitude();
            MyAltitude = gpsTracker.getAltitude();
        } else {
            Toast.makeText(getApplicationContext(), "Gps Off", Toast.LENGTH_SHORT).show();

        }
        getTimer();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(InstrutionActNew.this);
        mMap.clear();
        getInstruction();

        if (ActivityCompat.checkSelfPermission(InstrutionActNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InstrutionActNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int position = (int) (marker.getTag());
        SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId", "");
        if (snackbar != null) {
            snackbar.dismiss();
        }
        Log.e(TAG, "onMarkerClick: " + instructionList.get(position).getGeolocation());
        Log.e(TAG, "onMarkerClick: " + instructionList.get(position).getEventId());
        Log.e(TAG, "onMarkerClick: " + eventId);
        if (  eventId.equalsIgnoreCase("19")
                || eventId.equalsIgnoreCase("18")
                || eventId.equalsIgnoreCase("5")
                || eventId.equalsIgnoreCase("8")
                || eventId.equalsIgnoreCase("1")|| eventId.equalsIgnoreCase("28")) {
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
            Toast.makeText(getApplicationContext(),""+distance,Toast.LENGTH_SHORT).show();
          /*  if (distance >= 150) {
                showSimpleCancelBtnDialog(InstrutionActNew.this, R.layout.dialog_distance, distance + "");
            } else {
                Log.e("TAG", "onMarkerClick: " + instructionList.get(position));
                startQuestionAnswerActivity(position);
            }*/
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
                    result = result;
                    if (result == null & result <= 0) {
                    } else {
                        startTimer();
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

    private void getInstruction() {
        if (instructionList != null) {
            instructionList.clear();
        }
        ArrayList<SuccessResGetInstruction.Result> data = SharedPreferenceUtility.getInstance(getApplicationContext()).getSuccessResGetInstruction("SuccessResGetInstruction");
        instructionList.addAll(data);
        Log.e(TAG, "getInstruction: " + instructionList.toString());
        marker = new Marker[instructionList.size()];
        int i = 0;
        try {
            LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        } catch (Exception e) {
            Log.e(TAG, "moveCameramoveCamera: " + e.getLocalizedMessage());
            Log.e(TAG, "moveCameramoveCamera: " + e.getMessage());
        }
        for (SuccessResGetInstruction.Result result : instructionList) {

            if (result.getAnswer_status().equalsIgnoreCase("1")) {
                if (result.getLat().equalsIgnoreCase(""))
                    return;
                else {
                    try {
                        marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),
                                "#" + i, "", R.drawable.flag_green);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "onMarkerClick: NumberFormatExceptionNumberFormatException" + result.getId());
                        marker[i] = createMarker(i, convertDMSToDecimal(result.getLat()), convertDMSToDecimal(result.getLon()),
                                "#" + i, "", R.drawable.flag_green);

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
                        Log.e(TAG, "onMarkerClick: NumberFormatExceptionNumberFormatException" + result.getId());
                        marker[i] = createMarker(i, convertDMSToDecimal(result.getLat()),
                                convertDMSToDecimal(result.getLon()),
                                "#" + i, "", R.drawable.flag_red);

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


    protected Marker createMarker(int position, double latitude, double longitude, String title, String snippet, int iconResID) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(iconResID);
        myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).anchor(0.5f, 0.5f).title(title).icon(icon).snippet(snippet));
        myMarker.setTag(position);
        return myMarker;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(InstrutionActNew.this, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "millismillismillis: " + result);
                updateTimer(result);
                handler.postDelayed(this, 1000); // Update every second
            }
        };
        handler.post(runnable); // Start the timer
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
        binding.tvHeader.setText(time);
        result = result + 1000;
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the timer and remove the handler callbacks
        handler.removeCallbacks(runnable);
    }
}
