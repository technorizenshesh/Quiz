package com.smsjuegos.quiz.activities.game2;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.model.SuccessResGetEvents;

public class Game2StartVideoAct extends AppCompatActivity {
    ImageView ivHeader;
    private CardView cvVideo;
    private SuccessResGetEvents.Result result;
    private AppCompatButton btnPlay;
    private String videoUrl;
    private StyledPlayerView playerView;
    private ExoPlayer player;
    private ImaAdsLoader adsLoader;
    private String SAMPLE_VIDEO_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2_start_video);
        playerView = findViewById(R.id.player_view_tt);
        adsLoader = new ImaAdsLoader.Builder(this).build();
        result = (SuccessResGetEvents.Result) getIntent().getSerializableExtra("instructionID");
        SAMPLE_VIDEO_URL = result.getVideo();
        ivHeader = findViewById(R.id.ivBack);
        cvVideo = findViewById(R.id.cvBack);
        btnPlay = findViewById(R.id.btnPlay);
        cvVideo.setOnClickListener(v ->
                {
                 //   releasePlayer();
                    finish();
                }
        );

        btnPlay.setOnClickListener(v ->
                {
                   // releasePlayer();
                    startActivity(new Intent(Game2StartVideoAct.this,
                            Game2InstructionAct.class).putExtra("instructionID", result));
                }
        );

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
        player.release();
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
        player = null;
    }

    private void initializePlayer() {
        if (SAMPLE_VIDEO_URL.equalsIgnoreCase("")) {
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
            Uri contentUri = Uri.parse(SAMPLE_VIDEO_URL);
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
