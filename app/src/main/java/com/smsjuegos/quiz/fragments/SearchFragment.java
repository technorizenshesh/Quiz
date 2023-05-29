package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.smsjuegos.quiz.databinding.FragmentSearchBinding;
import com.smsjuegos.quiz.model.SuccessResNearbyEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.GPSTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    FragmentSearchBinding binding;
    GPSTracker gpsTracker;
    Marker myMarker;
    Marker[] marker;
    private GoogleMap mMap;
    private String strLat = "", strLng = "";
    private final ArrayList<SuccessResNearbyEvents.Result> nearbyEventList = new ArrayList<>();
    private QuizInterface apiInterface;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        gpsTracker = new GPSTracker(getActivity());
        getLocation();
        return binding.getRoot();
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====", gpsTracker.getLatitude() + "");
            strLat = Double.toString(gpsTracker.getLatitude());
            strLng = Double.toString(gpsTracker.getLongitude());
            getLocations();
        }
    }

    private void getLocations() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
    /*    map.put("lat", strLat);
        map.put("lon", strLng);
   */
        map.put("lat", "19.432651");
        map.put("lon", "-99.133587");
        Call<SuccessResNearbyEvents> call = apiInterface.getNearbyEvents(map);
        call.enqueue(new Callback<SuccessResNearbyEvents>() {
            @Override
            public void onResponse(Call<SuccessResNearbyEvents> call, Response<SuccessResNearbyEvents> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResNearbyEvents data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        nearbyEventList.clear();
                        nearbyEventList.addAll(data.getResult());
//                        LatLng sydney = new LatLng(-33, 150);
                        mMap.setOnMarkerClickListener(SearchFragment.this::onMarkerClick);
                        marker = new Marker[nearbyEventList.size()];
                        int i = 0;
                        for (SuccessResNearbyEvents.Result result : nearbyEventList) {
                            marker[i] = createMarker(Double.parseDouble(result.getLat()),
                                    Double.parseDouble(result.getLon()), result.getEventName(), "", R.drawable.ic_loca, i);
                            i++;
                        }
                     /*   LatLng sydney = new LatLng(Double.parseDouble(nearbyEventList.get(0).getLat()),Double.parseDouble(nearbyEventList.get(0).getLon()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );
                 */
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResNearbyEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID, int position) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 9f));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(iconResID);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(icon)
                .snippet(snippet));
        myMarker.setTag(position);
        return myMarker;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        LatLng sydney = new LatLng(19.432651, -99.133587);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        View v = getView();

        int position = (Integer) marker.getTag();

        Bundle bundle = new Bundle();
        bundle.putString("id", nearbyEventList.get(position).getId());
        // Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_eventDetailFragment,bundle);
        Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_navigation_list, bundle);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Latittude====", gpsTracker.getLatitude() + "");
                strLat = Double.toString(gpsTracker.getLatitude());
                strLng = Double.toString(gpsTracker.getLongitude());
                getLocations();
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


}