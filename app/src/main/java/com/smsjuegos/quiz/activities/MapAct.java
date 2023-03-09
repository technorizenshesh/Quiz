package com.smsjuegos.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.game4.QuestionAnswerAct;
import com.smsjuegos.quiz.model.SuccessResGetInstruction;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

public class MapAct extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    Marker[] marker = new Marker[2]; //change length of array according to you
    Marker myMarker;
    private String eventId,eventCode;
    private ArrayList<SuccessResGetInstruction.Result> instructionList = new ArrayList<>();
    private QuizInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        eventId = getIntent().getExtras().getString("eventId");
        eventCode = getIntent().getExtras().getString("eventCode");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getInstruction();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int position = (int) (marker.getTag());
        if(instructionList.get(position).getEventType().equalsIgnoreCase("crime"))
        {
            startActivity(new Intent(MapAct.this, QuestionAnswerAct.class).putExtra("instructionID",instructionList.get(position)).putExtra("eventCode",eventCode));
        }
        else
        {
            startActivity(new Intent(MapAct.this,PuzzleAct.class).putExtra("instructionID",instructionList.get(position)).putExtra("eventCode",eventCode));
        }
        return false;
    }

    private void getInstruction()
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
        map.put("lang", lang);

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
                        mMap.setOnMarkerClickListener(MapAct.this::onMarkerClick);
                        marker = new Marker[instructionList.size()];
                        int i=0;
                        for (SuccessResGetInstruction.Result result:instructionList)
                        {
                            marker[i] = createMarker(i,Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()), "Quiz "+i,"", R.drawable.ic_loca);
                            i++;
                        }
                        LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()),Double.parseDouble(instructionList.get(0).getLon()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
                    } else if (data.status.equals("0")) {
                        showToast(MapAct.this, data.message);
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
    private BitmapDescriptor bitmapDescriptorFromVector( int vectorResId) {
        Drawable vectorDrawable = getApplicationContext().getDrawable(vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    protected Marker createMarker(int position,double latitude, double longitude, String title, String snippet, int iconResID) {
        BitmapDescriptor icon = bitmapDescriptorFromVector(iconResID);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 9f));
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