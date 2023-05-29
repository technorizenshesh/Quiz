package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.LATITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.LONGITUDE;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.DeclimarActivity;
import com.smsjuegos.quiz.databinding.FragmentEventLocationsBinding;
import com.smsjuegos.quiz.model.SuccessResGetEventDetail;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.GPSTracker;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventLocationsFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentEventLocationsBinding binding;
    String eventStartTime = "";
    String event_code = "";
    GPSTracker gpsTracker;
    private QuizInterface apiInterface;
    private SuccessResGetEventDetail.Result eventDetails;
    private GoogleMap mMap;
    private String eventId = "";
    private String strLat = "";
    private String strLng = "";
    private final String strEventCode = "";
    private Dialog dialog, mDialog;
    private String strCode = "";
    private String strCodeTeam = "";
    private final String event_status = "";
    private Double Distanc;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventLocationsFragment() {
        // Required empty public constructor
    }

    public static EventLocationsFragment newInstance(String param1, String param2) {
        EventLocationsFragment fragment = new EventLocationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_locations, container, false);
        try {


            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.locat_map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gpsTracker = new GPSTracker(getActivity());
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString("eventId");
            event_code = bundle.getString("event_code");
        }

        getEventDetails();

        binding.tvStatus.setOnClickListener(v -> {
                   /* if (Distanc>5){
                        Toast.makeText(requireActivity(), Distanc.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }*/


                    if (eventDetails.team_name.equalsIgnoreCase("")) {
                        showImageSelection();
                    } else {
                        strCode = event_code;
                        strCodeTeam = eventDetails.team_name;

                        addeventStartTime();

                    }
                }
                /*    try {


                    Date currentDateTime = new Date();
                    Log.e(TAG, "onCreateView: "+currentDateTime );
                    Log.e(TAG, "eventDateTime: "+eventDateTime );
                    Log.e(TAG, "eventDateTime: "+eventDateTime.compareTo(currentDateTime)  );
                    if (eventDateTime.compareTo(currentDateTime) >= 0) {
                    } else {
                    //    Toast.makeText(requireContext(), getString(R.string.event_date_exp), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
}                   //     Toast.makeText(requireContext(), getString(R.string.event_date_exp), Toast.LENGTH_SHORT).show();

                }*/);

        return binding.getRoot();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====", gpsTracker.getLatitude() + "");
            strLat = Double.toString(gpsTracker.getLatitude());
            strLng = Double.toString(gpsTracker.getLongitude());
            // setLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Latittude====", gpsTracker.getLatitude() + "");
                strLat = Double.toString(gpsTracker.getLatitude());
                strLng = Double.toString(gpsTracker.getLongitude());
                //   setLocation();
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//
//                    }
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setLocation() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_loca);
        LatLng sydney = new LatLng(Double.parseDouble(eventDetails.lat), Double.parseDouble(eventDetails.lon));
        mMap.addMarker(new MarkerOptions().position(sydney).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        try {


        /*  Distanc=  DataManager.distanceInKm( sydney.latitude,sydney.longitude,19.429612948473434,
                  -99.19726243783843);*/
            //Distanc=  DataManager.distanceInKm(sydney.latitude,sydney.longitude,gpsTracker.getLatitude(),gpsTracker.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "setLocation: " + e.toString());
        }
    }

    private void getEventDetails() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        boolean val = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", event_code);
        map.put("user_id", userId);
        if (!val) {
            lang = "en";
        } else {
            lang = "sp";
        }
        map.put("lang", lang);
        Call<SuccessResGetEventDetail> call = apiInterface.getEventDetails(map);
        call.enqueue(new Callback<SuccessResGetEventDetail>() {
            @Override
            public void onResponse(Call<SuccessResGetEventDetail> call, Response<SuccessResGetEventDetail> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetEventDetail data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        eventDetails = data.getResult();
                        setEventDetails();
                        setLocation();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetEventDetail> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setEventDetails() {
        binding.label.setText(eventDetails.getEventName());
        binding.tvRate.setText(eventDetails.getAmount());
        binding.tvDate.setText(eventDetails.getEventDate());
        binding.tvLocation.setText(eventDetails.getAddress());

        Glide.with(requireActivity()).load(eventDetails.getImage()).centerCrop().into(binding.imgEvent);
        eventStartTime = eventDetails.getEventDate() + " " + eventDetails.getEventTime();

//        String dtStart = eventStartTime;
        //eventDateTime = eventStartTime;
        /*@SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        try {
      //      eventDateTime = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(requireActivity()
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        getLocation();


    }

    private void fullScreenDialog() {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.downloading_layout);

        ImageView ivBack, ivOption;

        ivBack = dialog.findViewById(R.id.imgHeader);
        ivOption = dialog.findViewById(R.id.imgOptions);

        ivBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        ivOption.setOnClickListener(v -> {

            showMainMenu();

        });

        dialog.show();

    }

    private void showMainMenu() {

        TextView tvInstruction;
        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        mDialog.setContentView(R.layout.main_menu_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        ImageView ivCancel = mDialog.findViewById(R.id.ivCancel);

        tvInstruction = mDialog.findViewById(R.id.tvInstruction);

        ivCancel.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        tvInstruction.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_calander_to_instructionFragment);
        });

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    public void showImageSelection() {
        try {
            strCode = "";
            strCodeTeam = "";
            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
            dialog.setContentView(R.layout.dialog_use_code_add_team);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
            EditText editText = dialog.findViewById(R.id.etName);
            EditText et_team_Name = dialog.findViewById(R.id.et_team_Name);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            if (eventDetails.eventStatus.equalsIgnoreCase("false")) {
                et_team_Name.setVisibility(View.VISIBLE);

            } else {
                if (eventDetails.team_name.equalsIgnoreCase("")) {

                } else {
                    et_team_Name.setVisibility(View.VISIBLE);
                    et_team_Name.setClickable(false);
                    et_team_Name.setFocusable(false);
                    et_team_Name.setInputType(0);
                    et_team_Name.setText(eventDetails.getTeam_name());
                }

            }
            btnSubmit.setOnClickListener(v -> {
                Log.e("TAG", "showImageSelection: Distanc" + Distanc);
                strCode = editText.getText().toString();
                strCodeTeam = et_team_Name.getText().toString();

                if (strCodeTeam.equalsIgnoreCase("")) {
                    showToast(getActivity(), "Please enter Team Name");

                } else if (!strCode.equalsIgnoreCase("")) {
                    addeventStartTime();
                    dialog.dismiss();

                } else {
                    showToast(getActivity(), "Please enter code");
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Unsupported Device", Toast.LENGTH_SHORT).show();

        }

    }
/*
    public void showImageSelection() {
        try {
            strCode = "";
            strCodeTeam = "";
            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
            dialog.setContentView(R.layout.dialog_use_code_add_team);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
            EditText editText = dialog.findViewById(R.id.etName);
            EditText et_team_Name = dialog.findViewById(R.id.et_team_Name);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            if (eventDetails.eventStatus.equalsIgnoreCase("false")) {
                et_team_Name.setVisibility(View.VISIBLE);

            } else {
                if (eventDetails.team_name.equalsIgnoreCase("")) {

                } else {
                    et_team_Name.setVisibility(View.VISIBLE);
                    et_team_Name.setClickable(false);
                    et_team_Name.setFocusable(false);
                    et_team_Name.setInputType(0);
                    et_team_Name.setText(eventDetails.getTeam_name());
                }

            }
            btnSubmit.setOnClickListener(v -> {
                Log.e("TAG", "showImageSelection: Distanc"+ Distanc);
                strCode = editText.getText().toString();
                strCodeTeam = et_team_Name.getText().toString();

                if (strCodeTeam.equalsIgnoreCase("")) {
                    showToast(getActivity(), "Please enter Team Name");

                } else if (!strCode.equalsIgnoreCase("")) {
                    addeventStartTime();
                    dialog.dismiss();

                } else {
                    showToast(getActivity(), "Please enter code");
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Unsupported Device", Toast.LENGTH_SHORT).show();

        }

    }
*/


    public void addeventStartTime() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        String lon = SharedPreferenceUtility.getInstance(getContext()).getString(LONGITUDE);
        String lat = SharedPreferenceUtility.getInstance(getContext()).getString(LATITUDE);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("user_id", userId);
        map.put("event_code", strCode);
        map.put("team_name", strCodeTeam);
        map.put("lat", lat);
        map.put("lon", lon);
        //  map.put("lat" ,"19.429612948473434");
        //  map.put("lon" ,"-99.19726243783850");
        Call<ResponseBody> call = apiInterface.addStartTime(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");
                    String result = jsonObject.getString("result");

                    if (data.equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("instructionID", eventDetails);
                        startActivity(new Intent(getActivity(), DeclimarActivity.class)
                                        .putExtras(bundle)
                                        .putExtra("eventId", eventId)
                                        .putExtra("eventCode", strCode)
                                /*   .putExtra("disclaimer", eventDetails.disclaimer)*/);

                    } else if (data.equals("0")) {
                        showToast(getActivity(), result);
                    } else if (data.equals("2")) {
                        showToast(getActivity(), jsonObject.getString("result"));
                    }
                    if (data.equals("3")) {
                        showDialog();
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

    private void showDialog() {
        final Dialog dialogq = new Dialog(requireActivity());
        dialogq.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogq.getWindow().getAttributes().windowAnimations
                = android.R.style.Widget_Material_ListPopupWindow;
        dialogq.setContentView(R.layout.dialog_already_comp);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogq.getWindow();
        lp.copyFrom(window.getAttributes());

        Button ivCancel = dialogq.findViewById(R.id.btncncel);
        ivCancel.setOnClickListener(D ->
                {
                    dialogq.dismiss();
                }
        );
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialogq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogq.show();
    }

}