package com.smsjuegos.quiz.activities.game2;

import static com.smsjuegos.quiz.retrofit.Constant.EVENT_CODE;
import static com.smsjuegos.quiz.retrofit.Constant.USER_ID;
import static com.smsjuegos.quiz.retrofit.Constant.showToast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.activities.FinishTeamInfo;
import com.smsjuegos.quiz.databinding.ActivityMissionCompletedBinding;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.retrofit.ApiClient;
import com.smsjuegos.quiz.retrofit.QuizInterface;
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
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mission_completed);
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        apiInterface = ApiClient.getClient().create(QuizInterface.class);
        exoPlayerView = findViewById(R.id.idExoPlayerVIew);

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
                            .putExtra("from","4")
                            .putExtra("eventId",result.getId()));
                    finishAffinity();
                }
                );

    }

    public void eventCompleted()
    {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        String event_code = SharedPreferenceUtility.getInstance(this).getString(EVENT_CODE);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("event_id", result.getId());
        map.put("user_id", userId);
        map.put("event_code", event_code);
        Call<ResponseBody> call = apiInterface.addVirusEndTime(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                         if (jsonObject.has("end_video")&&!
                                 jsonObject.getString("end_video").equalsIgnoreCase("")){
                        String end_video = jsonObject.getString("end_video");
                        PlayVideo(end_video);
                        binding.firstFinal.setVisibility(View.GONE);
                        binding.secondVid.setVisibility(View.VISIBLE);
                         }else {
                             startActivity(new Intent(MissionCompletedAct.
                                     this, FinishTeamInfo.class)
                                     .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                     .putExtra("from","4")
                                     .putExtra("eventId",result.getId()));
                             finishAffinity();
                         }

                    } else if (data.equals("0")) {
                        showToast(MissionCompletedAct.this, message);
                    }else if (data.equals("2")) {
                        showToast(MissionCompletedAct.this, jsonObject.getString("result"));
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

    private void PlayVideo(String url) {
        try {
            // bandwisthmeter is used for
            // getting default bandwidth
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // track selector is used to navigate between
            // video using a default seekbar.
            TrackSelector trackSelector = new DefaultTrackSelector(
                    new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            // we are parsing a video url
            // and parsing its video uri.
            Uri videouri = Uri.parse(url);
            // we are creating a variable for datasource factory
            // and setting its user agent as 'exoplayer_view'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(
                    "exoplayer_video");
            // we are creating a variable for extractor factory
            // and setting it to default extractor factory.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // we are creating a media source with above variables
            // and passing our event handler as null,

            MediaSource mediaSource = new ExtractorMediaSource(videouri,
                    dataSourceFactory, extractorsFactory, null, null);
            // inside our exoplayer view
            // we are setting our player
            exoPlayerView.setPlayer(exoPlayer);
            // we are preparing our exoplayer
            // with media source.
            exoPlayer.prepare(mediaSource);

            // we are setting our exoplayer
            // when it is ready.
            exoPlayer.setPlayWhenReady(true);

            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray
                        trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if (playbackState == ExoPlayer.STATE_ENDED){
                        //player back ended
                        exoPlayer.stop();
                        exoPlayer.release();
                        binding.btnfinish.setAlpha(1);
                        binding.btnfinish.setOnClickListener(v ->
                                {
                                    startActivity(new Intent(MissionCompletedAct.
                                            this, FinishTeamInfo.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra("from","4")
                                            .putExtra("eventId",result.getId()));
                                    finishAffinity();

                                }
                        );
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

        } catch (Exception e) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : " + e.toString());
        }
    }

    @Override
    protected void onPause() {
        if (exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();
        }
        super.onStop();
    }
}