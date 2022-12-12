package com.my.quiz.activities.game2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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
import com.my.quiz.R;
import com.my.quiz.model.SuccessResGetEvents;

public class Game2StartVideoAct extends AppCompatActivity {

    ImageView ivHeader;
    private CardView cvVideo;
    private SuccessResGetEvents.Result result;
    private AppCompatButton btnPlay;

    // creating a variable for exoplayerview.
    SimpleExoPlayerView exoPlayerView;
    // creating a variable for exoplayer
    SimpleExoPlayer exoPlayer;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2_start_video);

        ivHeader = findViewById(R.id.ivBack);
        cvVideo = findViewById(R.id.cvBack);
        btnPlay = findViewById(R.id.btnPlay);
        result = (SuccessResGetEvents.Result) getIntent().
                getSerializableExtra("instructionID");
        videoUrl =     result.getVideo(); //"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4";    //
        btnPlay.setAlpha(.5f);

        cvVideo.setOnClickListener(v ->
                {
                    exoPlayer.stop();
                    exoPlayer.release();
                    finish();
                }
        );

        btnPlay.setOnClickListener(v ->
                {
                    exoPlayer.stop();
                    exoPlayer.release();
                    startActivity(new Intent(
                            Game2StartVideoAct.this,Game2InstructionAct.class)
                            .putExtra("instructionID",result));
                }
        );

        exoPlayerView = findViewById(R.id.idExoPlayerVIew);
        try {
            // bandwisthmeter is used for
            // getting default bandwidth
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // track selector is used to navigate between
            // video using a default seekbar.
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            // we are parsing a video url
            // and parsing its video uri.
            Uri videouri = Uri.parse(videoUrl);
            // we are creating a variable for datasource factory
            // and setting its user agent as 'exoplayer_view'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

            // we are creating a variable for extractor factory
            // and setting it to default extractor factory.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // we are creating a media source with above variables
            // and passing our event handler as null,

            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
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
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

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
                        btnPlay.setAlpha(1);
                        btnPlay.setOnClickListener(v ->
                                {
                                    startActivity(new Intent(Game2StartVideoAct.this,Game2InstructionAct.class).putExtra("instructionID",result));
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