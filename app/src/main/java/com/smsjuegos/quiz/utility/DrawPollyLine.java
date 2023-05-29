package com.smsjuegos.quiz.utility;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smsjuegos.quiz.retrofit.ApiClient2;
import com.smsjuegos.quiz.retrofit.QuizInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawPollyLine {

    List<List<HashMap<String, String>>> routes = new ArrayList<>();
    ArrayList<LatLng> points = new ArrayList<>();
    private final Context context;
    private LatLng origin;
    private LatLng destination;
    private PolylineOptions lineOptions;

    public DrawPollyLine(Context context) {
        this.context = context;
    }

    public static DrawPollyLine get(Context context) {
        return new DrawPollyLine(context);
    }

    public DrawPollyLine setOrigin(LatLng origin) {
        this.origin = origin;
        return this;
    }

    public DrawPollyLine setDestination(LatLng destination) {
        this.destination = destination;
        return this;
    }

    public String getPolyLineUrl(Context context, LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        //String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + "AIzaSyAAjp4wuz5FyteoKH3Yp7Cv8pS1K86fJw0";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + "AIzaSyCjm3GS5HaNLYXnxqqF_IhsufMpQgcx8T0";
        String output = "json";
        String url = "" + output + "?" + parameters;
        Log.e("PathURL", "====>" + url);
        //https://maps.googleapis.com/maps/api/directions
        return url;
    }

    public void execute(onPolyLineResponse listener) {
        String URL = getPolyLineUrl(context, origin, destination);
        QuizInterface apiInterface = ApiClient2.getClient().create(QuizInterface.class);
        apiInterface.getURL(URL).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    // JSONObject object = new JSONObject(String.valueOf(response));
                    DataParser parser = new DataParser();
                    routes = parser.parse(object);
                    for (int i = 0; i < routes.size(); i++) {
                        points = new ArrayList<>();
//                        lineOptions = new PolylineOptions();
                        List<HashMap<String, String>> path = routes.get(i);
                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }
                        Log.e("SIZE POINT", " True >> " + points.size());
                       /* lineOptions.addAll(points);
                        lineOptions.width(10);
                        lineOptions.color(Color.RED);*/
                        listener.Success(points);
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public interface onPolyLineResponse {
        void Success(ArrayList<LatLng> latLngs);
    }

}
