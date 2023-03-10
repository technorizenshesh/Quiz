package com.smsjuegos.quiz.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.HomeAct;
import com.smsjuegos.quiz.databinding.FragmentCardBinding;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    FragmentCardBinding binding;

    private QuizInterface apiInterface;

    private Dialog dialog;

    String cardNo ="",expirationDate="",cvv = "",cardType = "",holderName="",expirationMonth = "",expirationYear = "";

    private String strTotal = "",strCartId = "",strTotalTicket="",strEventId = "",strTeamName="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_card, container, false);

        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        Bundle bundle = this.getArguments();

        if(bundle!=null)
        {
            strTotal = bundle.getString("total");
            strCartId = bundle.getString("card_id");
            strEventId = bundle.getString("event_id");
            strTotalTicket = bundle.getString("total_ticket");
            strTeamName = bundle.getString("tName");
        }

        binding.cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity) getActivity());

        binding.cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                cardNo = binding.cardForm.getCardNumber();
                expirationDate = binding.cardForm.getExpirationMonth()+"/"+binding.cardForm.getExpirationYear();
                expirationMonth = binding.cardForm.getExpirationMonth();
                expirationYear = binding.cardForm.getExpirationYear();
                cvv = binding.cardForm.getCvv();
                cardType = "";
                holderName = binding.cardForm.getCardholderName();
                if(binding.cardForm.isValid())
                {
                 clickOnPayNow();
                }else
                {
                    binding.cardForm.validate();
                }
            }
        });

        binding.btnPay.setOnClickListener(v ->
                {
                    cardNo = binding.cardForm.getCardNumber();
                    expirationDate = binding.cardForm.getExpirationMonth()+"/"+binding.cardForm.getExpirationYear();
                    expirationMonth = binding.cardForm.getExpirationMonth();
                    expirationYear = binding.cardForm.getExpirationYear();
                    cvv = binding.cardForm.getCvv();
                    cardType = "";
                    holderName = binding.cardForm.getCardholderName();
                    if(binding.cardForm.isValid())
                    {
                        clickOnPayNow();
                    }else
                    {
                        binding.cardForm.validate();
                    }

//                    Navigation.findNavController(v).navigate(R.id.action_cardFragment_to_bookingSuccessFragment);
                }
                );

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );
        binding.header.tvHeader.setText(getString(R.string.card));
        return binding.getRoot();
    }

    private void clickOnPayNow() {

        Card.Builder card = new Card.Builder(cardNo,
                Integer.parseInt(expirationMonth),
                Integer.parseInt(expirationYear),
                cvv);

        if (!card.build().validateCard()) {
            cardNo = "";
            expirationDate = "";
            cvv = "";
            showToast(getActivity(),"Please Enter valid card details.");
            return;
        }

        Stripe stripe = new Stripe(getActivity(), "pk_test_51Jl1kpIzhVsEreKHYKdvN0fLZUv3xQaOjf4W73C3qvTAMexMXcbJP5SwioNPbeeh6o2cP2ygdUrlV8oBfH2VAH9f000YseP4ES");

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        stripe.createCardToken(
                card.build(), new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(@NotNull Token token) {
                        DataManager.getInstance().hideProgressMessage();
                        callPaymentApi(token.getId());
                    }

                    @Override
                    public void onError(@NotNull Exception e) {
                        showToast(getActivity(),e.getMessage());
                        DataManager.getInstance().hideProgressMessage();
                    }
                });
    }

    private void callPaymentApi(String token)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("payment_method", "Card");
        map.put("amount", strTotal);
        map.put("currency", "USD");
        map.put("token", token);

        Call<ResponseBody> call = apiInterface.stipePayment(map);

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

                        booking();
//                        getActivity().onBackPressed();
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

    private void booking()
    {

        Date currentTime = Calendar.getInstance().getTime();

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("book_date", currentTime+"");
        map.put("book_time", currentTime+"");
        map.put("amount", strTotal);
        map.put("event_id", strEventId);
        map.put("total_ticket", strTotalTicket);
        map.put("cart_id", strCartId);
        map.put("team_name", strTeamName);

        Call<ResponseBody> call = apiInterface.booking(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                      //  String dataResponse = new Gson().toJson(response.body());
                     //   Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        startActivity(new Intent(getActivity(), HomeAct.class));
                        getActivity().finish();
//                        getActivity().onBackPressed();

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

//    private void fullScreenDialog() {
//
//        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
//
//        dialog.setContentView(R.layout.dialog_add_card);
//
//        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
//
//        ImageView ivBack;
//
//        ivBack = dialog.findViewById(R.id.imgHeader);
//
//        CardForm cardForm = dialog.findViewById(R.id.card_form);
//
//        cardForm.cardRequired(true)
//                .maskCardNumber(true)
//                .maskCvv(true)
//                .expirationRequired(true)
//                .cvvRequired(true)
//                .postalCodeRequired(false)
//                .mobileNumberRequired(false)
//                .saveCardCheckBoxChecked(false)
//                .saveCardCheckBoxVisible(false)
//                .cardholderName(CardForm.FIELD_REQUIRED)
//                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
//                .actionLabel("Purchase")
//                .setup((AppCompatActivity) getActivity());
//
//        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
//            @Override
//            public void onCardFormSubmit() {
//
//            }
//        });
//
//        btnAdd.setOnClickListener(v ->
//                {
//
//
//                }
//        );
//
//        ivBack.setOnClickListener(v ->
//                {
//                    dialog.dismiss();
//                }
//        );
//        dialog.show();
//
//    }


}