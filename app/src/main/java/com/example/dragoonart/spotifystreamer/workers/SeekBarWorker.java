package com.example.dragoonart.spotifystreamer.workers;

import android.media.MediaPlayer;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;

public class SeekBarWorker implements Runnable {
    private AudioPlayerActivityFragment activity;
    private boolean run = true;

    public SeekBarWorker(AudioPlayerActivityFragment activity) {

        this.activity = activity;
    }

    public void run() {

        while (run) {
            final MediaPlayer player = activity.getPlayer();
            if (activity.getSeekBar().getProgress() == player.getCurrentPosition() && !player.isPlaying()) {
                activity.getSeekBar().setProgress(activity.getSeekBar().getMax());
            }
            if (activity != null && activity.getActivity() != null) {
                activity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.getSeekBar().setProgress(player.getCurrentPosition());
                    }
                });

                try {
                    Thread.sleep(player.getDuration() / 200);
                } catch (InterruptedException e) {
                    //recalculate immediately
                }
            }
        }
    }

    public void stop() {
        this.run = false;
    }
}