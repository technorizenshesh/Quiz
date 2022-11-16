package com.my.quiz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.my.quiz.R;
import com.my.quiz.adapter.ListAdapter;
import com.my.quiz.databinding.FragmentInstructionBinding;
import com.my.quiz.model.SuccessResGetInstruct;
import com.my.quiz.model.SuccessResGetInstruct;
import com.my.quiz.retrofit.ApiClient;
import com.my.quiz.retrofit.Constant;
import com.my.quiz.retrofit.QuizInterface;
import com.my.quiz.utility.DataManager;
import com.my.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.quiz.retrofit.Constant.USER_ID;
import static com.my.quiz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstructionFragment extends Fragment {

    FragmentInstructionBinding binding;

    private QuizInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InstructionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstructionFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static InstructionFragment newInstance(String param1, String param2) {
        InstructionFragment fragment = new InstructionFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_instruction, container, false);

        apiInterface = ApiClient.getClient().create(QuizInterface.class);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.header.tvHeader.setText(getString(R.string.instruction));

        getMyEvents();

        return binding.getRoot();
    }

    private void getMyEvents()
    {

        boolean val =  SharedPreferenceUtility.getInstance(getActivity()).getBoolean(Constant.SELECTED_LANGUAGE);

        String lang = "";

        if(!val)
        {
            lang = "en";
        } else
        {
            lang = "sp";
        }

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("lang","sp");
        Call<SuccessResGetInstruct> call = apiInterface.geApplicationInstruction(map);
        call.enqueue(new Callback<SuccessResGetInstruct>() {
            @Override
            public void onResponse(Call<SuccessResGetInstruct> call, Response<SuccessResGetInstruct> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInstruct data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "SuccessResGetInstruct" + dataResponse);

                        if(!val)
                        {
                            binding.txtInstruct.setText(data.getResult().getContent());
                        } else
                        {
                            binding.txtInstruct.setText(data.getResult().getContentSp());
                        }

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetInstruct> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


}