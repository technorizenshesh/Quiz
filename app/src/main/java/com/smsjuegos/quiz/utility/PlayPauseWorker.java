package com.smsjuegos.quiz.utility;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayPauseWorker extends Worker {

    public static final String STATUS_KEY = "status";
    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";
    public static final String PAUSE_ID_KEY = "pause_id";
    public static final String EVENT_ID_KEY = "event_id";
    public static final String EVENT_CODE_KEY = "event_code";
    public static final String USER_ID_KEY = "user_id";
    public static final String RESULT_KEY = "result";

    private static final String TAG = "PlayPauseWorker";
    private QuizInterface apiInterface;
    public PlayPauseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get input data
        String status = getInputData().getString(STATUS_KEY);
        double lat = getInputData().getDouble(LAT_KEY, 0);
        double lon = getInputData().getDouble(LON_KEY, 0);
        String pauseId = getInputData().getString(PAUSE_ID_KEY);
        String eventId = getInputData().getString(EVENT_ID_KEY);
        String eventCode = getInputData().getString(EVENT_CODE_KEY);
        String userId = getInputData().getString(USER_ID_KEY);
        long result = getInputData().getLong(RESULT_KEY, 0);

        // Perform the network operation here (similar to your PlayPauseTimer logic)
        Map<String, String> map = new HashMap<>();
        map.put("event_id", eventId);
        map.put("event_code", eventCode);
        map.put("event_status", status);
        map.put("user_id", userId);
        map.put("lat", String.valueOf(lat));
        map.put("lon", String.valueOf(lon));
        map.put("pause_id", pauseId);
        map.put("total_time", String.valueOf(result));
        Log.e("worker timer request===",map.toString());

        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        Call<ResponseBody> call = apiInterface.eventTimePlayPause(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Log.e("worker timer response===",jsonObject.toString());

                        if (status.equalsIgnoreCase("1")) {
                            // Handle the response data
                            // For example, update UI or save state
                            Log.e("worker===","timer updated===");
                        }
                    } else {
                        // Handle error
                        Log.e("issue in worker==="," not timer updated===");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
            }
        });

        return Result.success();
    }
}

