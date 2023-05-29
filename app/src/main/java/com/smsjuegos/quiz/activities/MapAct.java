package com.smsjuegos.quiz.activities;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;
import static com.smsjuegos.quiz.utility.DataManager.downloadUrl;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.smsjuegos.quiz.utility.DirectionsJSONParser;
import com.smsjuegos.quiz.utility.DrawPollyLine;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapAct extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    Marker[] marker = new Marker[2]; //change length of array according to you
    Marker myMarker;
    private String eventId, eventCode, strtlat = "", strtlang = "", endlat = "", endlang = "";
    private ArrayList<SuccessResGetInstruction.Result> instructionList = new ArrayList<>();
    private QuizInterface apiInterface;
    private Snackbar snackbar ;
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
        //      eventId ="5";
        //      eventCode = "885429";
    }

    @Override
    protected void onResume() {
        getInstruction();
        super.onResume();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

      /*  try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyp));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }*/
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
        if (instructionList.get(position).getEventType().equalsIgnoreCase("crime")) {
       //     startActivity(new Intent(MapAct.this, QuestionAnswerAct.class).
        //            putExtra("instructionID", instructionList.get(position))
       //             .putExtra("eventCode", eventCode));
            Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();
        } else if (instructionList.get(position).getEventType().equalsIgnoreCase("codigo_frida")) {
            startActivity(new Intent(MapAct.this, QuestionAnswerAct.class).
                    putExtra("instructionID", instructionList.get(position))
                    .putExtra("eventCode", eventCode));
        } else if (instructionList.get(position).getEventType().equalsIgnoreCase("codigo_frida")) {
            startActivity(new Intent(MapAct.this, QuestionAnswerAct.class).
                    putExtra("instructionID", instructionList.get(position))
                    .putExtra("eventCode", eventCode));
        } else {

            Log.e("TAG", "onMarkerClick: " + instructionList.get(position));
            startActivity(new Intent(MapAct.this, PuzzleAct.class)
                    .putExtra("instructionID", instructionList.get(position))
                    .putExtra("eventCode", eventCode));
        }
        return false;
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
        map.put("event_code", eventCode);
        map.put("user_id", userId);
        Call<SuccessResGetInstruction> call = apiInterface.getInstruction(map);
        call.enqueue(new Callback<>() {
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
                                else marker[i] = createMarker(i, Double.parseDouble(result.getLat()), Double.parseDouble(result.getLon()),
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

                            } else {
                                Log.e("TAG", "SharedPreferenceUtility: "+SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId") );
                                Log.e("TAG", "SharedPreferenceUtility: "+result.id );
                                if (SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId").equalsIgnoreCase(result.id)) {
                                    strtlat   = result.getLat();
                                    strtlang = result.getLon();
                                 //   endlat    = data.getResult().get(i + 1).getLat();
                                  //  endlang  = data.getResult().get(i + 1).getLon();
                                    Log.e("TAG", "SharedPreferenceUtility: "+   strtlat+ strtlang+ endlat+ endlang );

                                }
                                 if ((Integer.parseInt(SharedPreferenceUtility.getInstance(getApplicationContext()).getString("NevId")))+1==Integer.parseInt(result.id)){
                                     endlat    = result.getLat();
                                     endlang  =  result.getLon();


                                 }
                            }

                        }
                        LatLng sydney = new LatLng(Double.parseDouble(instructionList.get(0).getLat()), Double.parseDouble(instructionList.get(0).getLon()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        try {
                            if (strtlang.equalsIgnoreCase("") ){

                            }else {
                            //     List<LatLng>latLngs = new ArrayList<>();
                                LatLng  latLngs1=  new LatLng(Double.parseDouble(strtlat),Double.parseDouble(strtlang));
                                LatLng  latLngs2=  new LatLng(Double.parseDouble(endlat),Double.parseDouble(endlang));
                               // latLngs.add(new LatLng(Double.parseDouble(endlat),Double.parseDouble(endlang)));
                           //     drawPolyLineOnMap(latLngs);

                                DrawPollyLine.get(getApplicationContext())
                                        .setOrigin(latLngs1)
                                        .setDestination(latLngs2)
                                        .execute(latLngs -> {
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
                                            snackbar = Snackbar.make(getWindow().getDecorView().getRootView()
                                                    , "", Snackbar.LENGTH_INDEFINITE);
                                            View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
                                            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                                            // now change the layout of the snackbar
                                            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                                            snackbarLayout.setPadding(0, 0, 0, 0);
                                            TextView textView2 = customSnackView.findViewById(R.id.textView2);
                                            textView2.setText("You have only "+SharedPreferenceUtility.getInstance(getApplicationContext()).getString("ArrTime")+" Minutes To Reach Next CheckPoint Hurry up!");
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
                            Log.e("TAG", "onResponse: "+e.getLocalizedMessage());
                            Log.e("TAG", "onResponse: "+e.getMessage());
                            Log.e("TAG", "onResponse: "+e.getCause());
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
    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.addAll(list);

       // mMap.clear();
        mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        final LatLngBounds bounds = builder.build();

        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cu);
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

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map
            if (points.size() != 0) {
                mMap.addPolyline(lineOptions);
            }
        }
    }

}
