package com.smsjuegos.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityInstrutionBinding;
import com.smsjuegos.quiz.model.SuccessResGetInstruction;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.Constant;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smsjuegos.quiz.retrofit.Constant.showToast;

public class InstrutionAct extends AppCompatActivity {

    ActivityInstrutionBinding binding;
    private QuizInterface apiInterface;
    private String eventId;
    private SuccessResGetInstruction.Result instruction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_instrution);
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );
        binding.header.tvHeader.setText(getString(R.string.instruction));
        eventId = getIntent().getExtras().getString("eventId");
        getEventDetails();
    }

    private void getEventDetails()
    {

        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
       /* Map<String, String> map = new HashMap<>();

*/
        boolean val =  SharedPreferenceUtility.getInstance(getApplicationContext()).getBoolean(Constant.SELECTED_LANGUAGE);
        String lang = "";

        if(!val)
        {
            lang = "en";
        } else
        {
            lang = "sp";
        }
        HashMap<String,String> map = new HashMap<>();
        map.put("lang",lang);
        map.put("event_id", eventId);
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
                        binding.tvInstruction.setText(data.getResult().get(0).getInstructions());
                    } else if (data.status.equals("0")) {
                        showToast(InstrutionAct.this, data.message);
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

}