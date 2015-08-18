package com.example.dragoonart.spotifystreamer.workers;

import android.media.MediaPlayer;
import android.widget.SeekBar;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.R;

public class SeekBarWorker implements Runnable {
    private AudioPlayerActivity activity;
    private SeekBar bar;
    private boolean run = true;

    public SeekBarWorker(SeekBar bar, AudioPlayerActivity activity) {
        this.bar = bar;
        this.activity = activity;
    }

    public void run() {

        while (run) {
            final MediaPlayer player = activity.getPlayer();
            if (bar.getProgress() == player.getCurrentPosition() && !player.isPlaying()) {
                run = false;
                bar.setProgress(bar.getMax());
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((SeekBar) activity.findViewById(R.id.player_seekBar)).setProgress(player.getCurrentPosition());
                }
            });

            try {
                Thread.sleep(player.getDuration() / 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.run = false;
    }
}