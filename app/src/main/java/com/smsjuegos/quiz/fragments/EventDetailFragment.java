package com.smsjuegos.quiz.fragments;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.FragmentEventDetailBinding;
import com.smsjuegos.quiz.model.SuccessResGetEventDetail;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentEventDetailBinding binding;
    private QuizInterface apiInterface;
    private SuccessResGetEventDetail.Result eventDetails;
    private String eventId, strAmount = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDetailFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static EventDetailFragment newInstance(String param1, String param2) {
        EventDetailFragment fragment = new EventDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);

        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            eventId = bundle.getString("id");
        }

        getEventDetails();

        binding.btnBookEvent.setOnClickListener(v ->
                {
                    addToCart();
                }
        );

        binding.btnViewCart.setOnClickListener(v ->
                {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", eventId);
                    Navigation.findNavController(v).navigate(R.id.action_eventDetailFragment_to_cartFragment, bundle1);
                }
        );

        return binding.getRoot();
    }

    public void addToCart() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("user_id", userId);
        map.put("ticket", "1");
        map.put("amount", strAmount);

        Call<ResponseBody> call = apiInterface.addToCart(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        binding.btnViewCart.setVisibility(View.VISIBLE);
                        binding.btnBookEvent.setVisibility(View.GONE);

                    } else if (data.equals("0")) {
                        showToast(getActivity(), message);
                    } else if (data.equals("2")) {
                        showToast(getActivity(), jsonObject.getString("result"));
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

    private void getEventDetails() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("user_id", userId);

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

        strAmount = eventDetails.getAmount();
        Glide
                .with(getContext())
                .load(eventDetails.getImage())
                .centerCrop()
                .into(binding.imgEvent);

        binding.tvEventName.setText(eventDetails.getEventName());
        String dtStart = eventDetails.getEventDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        String strOutputFormat = null;

        try {
            Date date = format.parse(dtStart);
            strOutputFormat = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.tvEventDate.setText(strOutputFormat);
        binding.tvPrice.setText(eventDetails.getAmount());
        binding.tvEventLocation.setText(eventDetails.getAddress());
        float totalTickets = Float.parseFloat(eventDetails.getTotalTicket());
        float buyedTickets = Float.parseFloat(eventDetails.getBookingEventCount());
        float progressCount = buyedTickets * 100 / totalTickets;
        binding.progressBar.setProgress((int) progressCount);
        binding.tvTicket.setText(getString(R.string.only) + eventDetails.getRemainingEventCount() + getString(R.string.tickets_remaining));
        if (eventDetails.getEventStatus().equalsIgnoreCase("false")) {
            binding.btnBookEvent.setVisibility(View.VISIBLE);
            binding.btnViewCart.setVisibility(View.GONE);
        } else {
            binding.btnBookEvent.setVisibility(View.GONE);
            binding.btnViewCart.setVisibility(View.VISIBLE);
        }
    }
}