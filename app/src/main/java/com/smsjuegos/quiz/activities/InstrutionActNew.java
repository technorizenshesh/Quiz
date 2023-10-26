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
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.DrawPollyLine;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(InstrutionActNew.this);
        binding.imgHeader.setOnClickListener(v -> finish());
        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");

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
                    InventoryAct.class).putExtra("eventId", eventId).putExtra("eventCode", eventCode));
            onStop();

        });

        binding.tvFinalPuzzel.setOnClickListener(v -> {

            Log.e(TAG, "showMainMenu: " + eventId);
            if (eventId.equals("8") | eventId.equals("15")|eventId.equals("18")) {
                startActivity(new Intent(InstrutionActNew
                        .this, CardigoPuzzleFinalActivity.class).putExtra("eventId", eventId)
                        .putExtra("eventCode", eventCode));

            } else {
                startActivity(new Intent(InstrutionActNew.this, FinalPuzzelAct.class).putExtra("eventId", eventId).putExtra("eventCode", eventCode));
            }
            onStop();

        });


    }

    @Override
    protected void onResume() {
        SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId", "");

        if (mMap != null) {
            mMap.clear();
        }

        if (gpsTracker != null && gpsTracker.canGetLocation()) {
            MyLatitude = gpsTracker.getLatitude();
            MyLongitude = gpsTracker.getLongitude();
            MyAltitude = gpsTracker.getAltitude();
        } else {
            Toast.makeText(getApplicationContext(), "Gps Off", Toast.LENGTH_SHORT).show();

        }

        getInstruction();
        getTimer();
        super.onResume();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(InstrutionActNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InstrutionActNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int position = (int) (marker.getTag());
        Log.e("TAG", "onMarkerClick: position" + position);
        Log.e("TAG", "onMarkerClick: getIdgetIdgetIdgetId" + instructionList.get(position).getId());
        Log.e("TAG", "onMarkerClick: instructionList.get(position).getEventType()---" + instructionList.get(position).getEventType());
        if (gpsTracker != null && gpsTracker.canGetLocation()) {
            MyLatitude = gpsTracker.getLatitude();
            MyLongitude = gpsTracker.getLongitude();
            MyAltitude = gpsTracker.getAltitude();
        } else {
            Toast.makeText(getApplicationContext(), "Gps Off", Toast.LENGTH_SHORT).show();

        }
        double distance = GPSTracker.getDistanceFromPointWithoutAlt(Double.parseDouble(instructionList.get(position).getLat()),
                Double.parseDouble(instructionList.get(position).getLon()),
                MyLatitude, MyLongitude);
        Log.e(TAG, "onMarkerClick: distancedistancedistancedistance"+distance );
        if (distance >50) {
            showSimpleCancelBtnDialog(InstrutionActNew.this, R.layout.dialog_distance  , distance+"");
        } else {
            if (instructionList.get(position).getEventType().equalsIgnoreCase("crime")) {
                startActivity(new Intent(InstrutionActNew.this, QuestionAnswerAct.class).
                        putExtra("instructionID", instructionList.get(position))
                        .putExtra("eventCode", eventCode));
                //Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
            } else if (instructionList.get(position).getEventType().equalsIgnoreCase("codigo_frida")) {
                startActivity(new Intent(InstrutionActNew.this, QuestionAnswerAct.class).
                        putExtra("instructionID", instructionList.get(position))
                        .putExtra("eventCode", eventCode));
            } else if (instructionList.get(position).getEventType().equalsIgnoreCase("zombie")) {
                startActivity(new Intent(InstrutionActNew.this, QuestionAnswerAct.class).
                        putExtra("instructionID", instructionList.get(position))
                        .putExtra("eventCode", eventCode));
            } else {

                Log.e("TAG", "onMarkerClick: " + instructionList.get(position));
                startActivity(new Intent(InstrutionActNew.this, PuzzleAct.class)
                        .putExtra("instructionID", instructionList.get(position))
                        .putExtra("eventCode", eventCode));
            }
        }
        return false;
    }

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
        String level = SharedPreferenceUtility.getInstance(this).getString(GAME_LAVEL);
        map.put("level", level);
        map.put("event_code", eventCode);
        map.put("user_id", userId);
        Log.e(TAG, "getInstruction: ---------------" + map.toString());
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
                        mMap.setOnMarkerClickListener(InstrutionActNew.this);
                        marker = new Marker[instructionList.size()];
                        int i = 0;

                        for (SuccessResGetInstruction.Result result : instructionList) {

                            if (result.getAnswer_status().equalsIgnoreCase("1")) {
                                if (result.getLat().equalsIgnoreCase("")) return;
                                else marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()), "#" + i, "", R.drawable.flag_green);
                            } else {

                                if (result.getLat().equalsIgnoreCase("")) return;
                                else marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()), "#" + i, "", R.drawable.flag_red);
                            }
                            i++;
                            if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId").equalsIgnoreCase("")) {

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
                        LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()),
                                Double.parseDouble(instructionList.get(0).getLon()));
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

                                DrawPollyLine.get(getApplicationContext()).setOrigin(latLngs1).setDestination(latLngs2).execute(latLngs -> {
                                    PolylineOptions options = new PolylineOptions();
                                    options.addAll(latLngs);
                                    options.color(Color.BLACK);
                                    options.width(8);
                                    options.startCap(new SquareCap());
                                    options.endCap(new SquareCap());
                                    Polyline line = mMap.addPolyline(options);

                                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                                    valueAnimator.setDuration(2000); // 2 seconds
                                    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                                    valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                                    valueAnimator.addUpdateListener(animator -> {
                                        int alpha = (int) animator.getAnimatedValue();
                                        line.setColor(Color.argb(alpha, 0, 0, 0));
                                    });
                                    valueAnimator.start();
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(latLngs1);
                                    final LatLngBounds bounds = builder.build();
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                                    mMap.animateCamera(cu);
                                          /*  Snackbar.make(getWindow().getDecorView().getRootView()
                                                    ,"",Snackbar.LENGTH_INDEFINITE).show();
                                            SharedPreferenceUtility.getInstance(getApplicationContext()).putString("NevId","");*/

                                    // create an instance of the snackbar
                                    snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_INDEFINITE);
                                    View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
                                    snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                                    // now change the layout of the snackbar
                                    Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                                    snackbarLayout.setPadding(0, 0, 0, 0);
                                    TextView textView2 = customSnackView.findViewById(R.id.textView2);
                                    textView2.setText("You have only " + SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime") + " Minutes To Reach Next CheckPoint Hurry up!");
                                    Button bGotoWebsite = customSnackView.findViewById(R.id.gotoWebsiteButton);
                                    bGotoWebsite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "Reaching Check....", Toast.LENGTH_SHORT).show();
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
                        showToast(InstrutionActNew.this, data.message);
                    } else if (data.status.equals("2")) {
                        showToast(InstrutionActNew.this, data.message);
                        Thread.sleep(5000);
                        startActivity(new Intent(InstrutionActNew.this, HomeAct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

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

/*    private void getInstruction() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(getApplicationContext()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";
        if (!val) {lang = "en";} else {lang = "sp";}
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("lang", lang);
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        map.put("event_code", eventCode);
        map.put("user_id", userId);
        Call<SuccessResGetInstructionTwo> call = apiInterface.get_event_instructions_game_two(map);
        call.enqueue(new Callback<SuccessResGetInstructionTwo>() {
            @Override
            public void onResponse(Call<SuccessResGetInstructionTwo> call, Response<SuccessResGetInstructionTwo> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInstructionTwo data = response.body();
                    Log.e("data", data.getStatus());
                    if (data.getStatus().equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        instructionList.clear();
                        instructionList.addAll(data.getResult());
                        mMap.setOnMarkerClickListener(InstrutionActNew.this);
                        marker = new Marker[instructionList.size()];
                        int i = 0;
                        for (SuccessResGetInstructionTwo.Result result : instructionList) {
                            if (result.getAnswerStatus().toString().equalsIgnoreCase("1")) {
                                if (result.getLat().equalsIgnoreCase("")) return;
                                else
                                    marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()), "#" + i, "", R.drawable.flag_green);
                            } else {

                                if (result.getLat().equalsIgnoreCase("")) return;
                                else
                                    marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()), "#" + i, "", R.drawable.flag_red);
                            }
                            i++;
                        }
                        LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    } else if (data.getStatus().equals("0")) {
                        showToast(InstrutionActNew.this, data.getMessage());
                    } else if (data.getStatus().equals("2")) {
                        showToast(InstrutionActNew.this, data.getMessage());
                        Thread.sleep(5000);
                        startActivity(new Intent(InstrutionActNew.this, HomeAct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        onStop();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetInstructionTwo> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }*/

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
