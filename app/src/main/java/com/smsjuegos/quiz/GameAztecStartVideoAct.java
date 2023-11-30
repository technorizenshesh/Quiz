package com.smsjuegos.quiz;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

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
import com.smsjuegos.quiz.activities.InstrutionActNew;
import com.smsjuegos.quiz.model.SuccessResGetEvents;

public class GameAztecStartVideoAct extends AppCompatActivity {
    ImageView ivHeader;
    private CardView cvVideo;
    private SuccessResGetEvents.Result result;
    private AppCompatButton btnPlay;
    private String videoUrl;
    private String eventId, eventCode;
    private StyledPlayerView playerView;
    private ExoPlayer player;
    private ImaAdsLoader adsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2_start_video);
        ivHeader = findViewById(R.id.ivBack);
        cvVideo = findViewById(R.id.cvBack);
        btnPlay = findViewById(R.id.btnPlay);
        playerView = findViewById(R.id.player_view_tt);
        adsLoader = new ImaAdsLoader.Builder(this).build();
        Log.e("TAG", "onCreate: " + videoUrl);
        if (getIntent().getExtras() != null) {
            eventId = getIntent().getExtras().getString("eventId");
            eventCode = getIntent().getExtras().getString("eventCode");
            videoUrl = getIntent().getExtras().getString("videoUrl");
            Log.e("TAG", "eventIdeventIdeventIdeventId: " + eventId);
            Log.e("TAG", "eventCodeeventCodeeventCode: " + eventCode);
        } else {
            //   eventId="4";
            //     eventCode="441812";
        }
        btnPlay.setAlpha(.5f);
        cvVideo.setOnClickListener(v -> {
         //   releasePlayer();
            finish();
        });

        btnPlay.setOnClickListener(v -> {
          //  releasePlayer();


            startActivity(new Intent(getApplicationContext(), InstrutionActNew.class)
                    .putExtra("eventId", eventId).putExtra("eventCode", eventCode));
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
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
        player.release();
        player = null;
    }

    private void initializePlayer() {
        if (videoUrl.equalsIgnoreCase("")) {
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
            Uri contentUri = Uri.parse(videoUrl);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(contentUri)
                    .setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(contentUri).build())
                    .build();
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
            playerView.setControllerShowTimeoutMs(1000);
            player.addAnalyticsListener(new AnalyticsListener() {
                @Override
                public void onPlaybackStateChanged(EventTime eventTime, int state) {
                    AnalyticsListener.super.onPlaybackStateChanged(eventTime, state);
                }
            });
        }
    }

}