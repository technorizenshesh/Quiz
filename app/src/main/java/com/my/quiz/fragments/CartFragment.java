package com.my.quiz.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.databinding.FragmentCardBinding;
import com.my.quiz.databinding.FragmentCartBinding;
import com.my.quiz.model.SuccessResGetCart;
import com.my.quiz.model.SuccessResGetEventDetail;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.NetworkAvailablity;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import org.json.JSONArray;
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

import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    private SuccessResGetEventDetail.Result eventDetails;
    private QuizInterface apiInterface;
    private String cartID;
    private SuccessResGetCart.Result cartItems = null;
    private String eventId = "",strAmount = "",strCount="",strTotalAmount="",strTotalTicket="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false);
        binding.header.tvHeader.setText(R.string.cart);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.btnBookEvent.setOnClickListener(v ->
                {

                    if(!binding.etName.getText().toString().equalsIgnoreCase(""))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("total",strTotalAmount);
                        bundle.putString("card_id",cartID);
                        bundle.putString("total_ticket",strTotalTicket);
                        bundle.putString("event_id",eventId);
                        bundle.putString("tName",binding.etName.getText().toString());
                        Navigation.findNavController(v).navigate(R.id.action_cartFragment_to_cardFragment,bundle);
                    }

                    else
                    {
                        showToast(getActivity(),"Please enter team name.");
                    }

                }
        );

        Bundle bundle = this.getArguments();

        if(bundle!=null)
        {
            eventId = bundle.getString("id");
        }

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getCart();
            getEventDetails();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.btnAdd.setOnClickListener(v ->
                {
                    int count = Integer.parseInt(strCount);
                    count++;

                    if (count>6)
                    {

                        showToast(getActivity(),""+getString(R.string.only_6_tickets_per_team));

                        return;
                    }

                    addToCart(count+"");
                }
                );

        binding.btnRemove.setOnClickListener(v ->
                {

                    int count = Integer.parseInt(strCount);
                    count--;

                    if(count==0)
                    {
                        deleteCart();
                    }
                    else
                    {
                        addToCart(count+"");
                    }
                }
        );

        binding.ivDelete.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Cart")
                            .setMessage("Are you sure you want to delete this cart?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    deleteCart();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                );

        return binding.getRoot();
    }

    private void deleteCart()
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("cart_id", cartID);
        map.put("user_id", userId);

        Call<ResponseBody> call = apiInterface.deleteCart(map);

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

                        getActivity().onBackPressed();

                    } else if (data.equals("0")) {
                        showToast(getActivity(), message);
                    }else if (data.equals("2")) {
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

    public void addToCart(String strQuantity)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("user_id", userId);
        map.put("ticket", strQuantity);
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

                        SuccessResGetCart myCartItem = new Gson().fromJson(response.body().string(),SuccessResGetCart.class);

//                        binding.tvCount.setText(myCartItem.getResult().get(0).getTicket());

                        getEventDetails();
                        getCart();

                    } else if (data.equals("0")) {
                        showToast(getActivity(), message);
                    }else if (data.equals("2")) {
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

    private void getCart()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<SuccessResGetCart> call = apiInterface.getCart(map);

        call.enqueue(new Callback<SuccessResGetCart>() {
            @Override
            public void onResponse(Call<SuccessResGetCart> call, Response<SuccessResGetCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCart data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        cartItems = data.getResult().get(0);
                        strCount = cartItems.getTicket();

                        cartID = data.getResult().get(0).getCartId();

                        binding.tvCount.setText(cartItems.getTicket());

                        strTotalTicket = cartItems.getTicket();

                        binding.tvTotal.setText(data.getTotalAmount()+"");

                        strTotalAmount = data.getTotalAmount()+"";

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCart> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void getEventDetails()
    {

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

    public void setEventDetails()
    {


        binding.tvTicket.setText(getString(R.string.only)+" "+eventDetails.getRemainingEventCount()+" "+getString(R.string.ticket_remaining));

        binding.tvPrice.setText(eventDetails.getAmount());

        strAmount = eventDetails.getAmount();

        float totalTickets =  Float.parseFloat(eventDetails.getTotalTicket());
        float buyedTickets = Float.parseFloat(eventDetails.getBookingEventCount());
        float progressCount = buyedTickets * 100 /  totalTickets;
        binding.progressBar.setProgress((int) progressCount);

    }


}