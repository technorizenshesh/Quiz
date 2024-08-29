package com.smsjuegos.quiz.utility;

import android.content.Context;
import android.media.MediaPlayer;

import com.smsjuegos.quiz.R;

public class SoundUtils {
    private static MediaPlayer mediaPlayer;

    public static void playBlastSound(Context context) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.blast_sound);
        mediaPlayer.start();
    }
}