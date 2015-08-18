package com.example.dragoonart.spotifystreamer.workers;

import android.media.MediaPlayer;
import android.widget.SeekBar;

public class SeekBarWorker implements Runnable {
    private MediaPlayer player;
    private SeekBar bar;
    private boolean run = true;

    public SeekBarWorker(SeekBar bar, MediaPlayer player) {
        this.bar = bar;
        this.player = player;
    }

    public void run() {
        while (run) {
            if (bar.getProgress() == player.getCurrentPosition() && !player.isPlaying()) {
                run = false;
                bar.setProgress(bar.getMax());
            }
            bar.setProgress(player.getCurrentPosition());
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