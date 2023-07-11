package com.smsjuegos.quiz.activities.game2;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT;
import static com.smsjuegos.quiz.retrofit.Constant.EVENT_CODE;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.FinishTeamInfo;
import com.smsjuegos.quiz.databinding.ActivityMissionCompletedBinding;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
import com.smsjuegos.quiz.retrofit.VirusEndRes;
import com.smsjuegos.quiz.utility.DataManager;
import com.smsjuegos.quiz.utility.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionCompletedAct extends AppCompatActivity {

    ActivityMissionCompletedBinding binding;
    private SuccessResGetEvents.Result result;
    private QuizInterface apiInterface;
    private StyledPlayerView playerView;
    private ExoPlayer player;
    private ImaAdsLoader adsLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mission_completed);
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        playerView = findViewById(R.id.player_view_tt);
        adsLoader = new ImaAdsLoader.Builder(this).build();
        Glide.with(this)
                .load(result.getImage())
                .fitCenter()
                .into(binding.ivGame);

        binding.btnEndOfGame.setOnClickListener(v ->
                {
                    eventCompleted();
                }
        );
        binding.btnfinish.setOnClickListener(v ->
                {
                    startActivity(new Intent(MissionCompletedAct.
                            this, FinishTeamInfo.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("from", "4")
                           // .putExtra("eventId", "4"));
                            .putExtra("eventId", result.getId()));
                    finishAffinity();
                }
        );

    }

    public void eventCompleted() {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        String event_code = SharedPreferenceUtility.getInstance(this).getString(EVENT_CODE);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
       // map.put("event_id", "4");
       map.put("event_id", result.getId());
        map.put("user_id", userId);
        map.put("event_code", event_code);
        Call<VirusEndRes> call = apiInterface.addVirusEndTime2(map);
        call.enqueue(new Callback<VirusEndRes>() {
            @Override
            public void onResponse(@NonNull Call<VirusEndRes> call, @NonNull Response<VirusEndRes> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        if (!response.body().getResult().getEndVideo().equalsIgnoreCase("")) {
                            PlayVideo(response.body().getResult().getEndVideo());
                            binding.firstFinal.setVisibility(View.GONE);
                            binding.secondVid.setVisibility(View.VISIBLE);
                        } else {
                           startActivity(new Intent(MissionCompletedAct.
                                    this, FinishTeamInfo.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("from", "4")
                                    //.putExtra("eventId", "4"));
                                    .putExtra("eventId", result.getId()));
                            finishAffinity();
                        }

                    } else if (response.body().getStatus().equals("0")) {
                        showToast(MissionCompletedAct.this, response.body().getMessage());
                    } else if (response.body().getStatus().equals("2")) {
                        showToast(MissionCompletedAct.this, response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onResponse: "+e.getLocalizedMessage() );
                    Log.e("TAG", "onResponse: "+e.getCause() );
                    Log.e("TAG", "onResponse: "+e.getMessage() );
                }
            }

            @Override
            public void onFailure(@NonNull Call<VirusEndRes> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void PlayVideo(String url) {
        try {

            Log.e("TAG", "PlayVideo: urlurlurl  "+url);
initializePlayer(url);
                        binding.btnfinish.setAlpha(1);
                        binding.btnfinish.setOnClickListener(v ->
                        {
                          //  releasePlayer();
                            startActivity(new Intent(MissionCompletedAct.
                                    this, FinishTeamInfo.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("from", "4")
                                    // .putExtra("eventId", "4"));
                                    .putExtra("eventId", result.getId()));
                            finishAffinity();
                        });



        } catch (Exception e) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : " + e);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        adsLoader.release();
        super.onDestroy();
    }

    private void releasePlayer() {
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
         if (player!=null) {
             player.release();
         }
        player = null;
    }

    private void initializePlayer(String urls) {
        if (urls.equalsIgnoreCase("")) {
        } else {
            DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
            MediaSource.Factory mediaSourceFactory =
                    new DefaultMediaSourceFactory(dataSourceFactory)
                            .setAdsLoaderProvider(unusedAdTagUri -> adsLoader)
                            .setAdViewProvider(playerView);
            player = new ExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();
            //  playerView.setControllerHideDuringAds(true);
            player.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT);
            playerView.setPlayer(player);
            adsLoader.setPlayer(player);
            Uri contentUri = Uri.parse(urls);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(contentUri)
                    .setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(contentUri).build())
                    .build();
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
            player.addAnalyticsListener(new AnalyticsListener() {
                @Override
                public void onPlaybackStateChanged(EventTime eventTime, int state) {
                    AnalyticsListener.super.onPlaybackStateChanged(eventTime, state);
                }
            });
        }
    }
}