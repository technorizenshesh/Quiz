package com.my.quiz.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.adapter.ListAdapter;
import com.my.quiz.databinding.FragmentListBinding;
import com.my.quiz.model.SuccessResGetEventDetail;
import com.my.quiz.model.SuccessResGetMyEvents;
import com.my.quiz.model.SuccessResGetMyEvents;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.quiz.activities.HomeAct.navView;
import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    FragmentListBinding binding;

    private QuizInterface apiInterface;

    private ArrayList<SuccessResGetMyEvents.Result> eventList = new ArrayList<>();

    private String strCode = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_list, container, false);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.tvHeader.setText(R.string.play_a_game);
        binding.imgHeader.setOnClickListener(view1 ->
                {
                    getActivity().onBackPressed();
                }
        );
        binding.rlParent.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            binding.rlParent.setVisibility(View.VISIBLE);
        }
        binding.rvListItems.setHasFixedSize(true);
        binding.rvListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvListItems.setAdapter(new ListAdapter(getActivity()));

        binding.btnUseCode.setOnClickListener(v ->
                {
                    showImageSelection();
                }
                );

        return  binding.getRoot();
    }

    public void showImageSelection() {
        strCode = "";

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_use_code);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        EditText editText =  dialog.findViewById(R.id.etName);

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        btnSubmit.setOnClickListener(v ->
                {
                    strCode = editText.getText().toString();
                    if(!strCode.equalsIgnoreCase(""))
                    {
                        addCode();
                        dialog.dismiss();
                    }
                    else
                    {
                        showToast(getActivity(),"Please enter code");
                    }
                }
                );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getMyEvents()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetMyEvents> call = apiInterface.getMyEvent(map);
        call.enqueue(new Callback<SuccessResGetMyEvents>() {
            @Override
            public void onResponse(Call<SuccessResGetMyEvents> call, Response<SuccessResGetMyEvents> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetMyEvents data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);


                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetMyEvents> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private void addCode()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("event_code", strCode);

        Call<ResponseBody> call = apiInterface.applyCode(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    Log.d(TAG, "onResponse: "+jsonObject);

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        showToast(getActivity(), message);

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

}