package com.example.dragoonart.spotifystreamer.listeners;

import android.media.MediaPlayer;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.R;

/**
 * Created by xnml on 2015-07-17.
 */
public class AudioPlayerListener implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private AudioPlayerActivity activity;
    private SeekBarTick barTick;

    public AudioPlayerListener(AudioPlayerActivity activity) {

        this.activity = activity;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (compoundButton.getId() == R.id.player_playButton) {
            if (checked && !activity.getPlayer().isPlaying()) {
                barTick = new SeekBarTick(activity.getSeekBar(), activity.getPlayer());
                new Thread(barTick).start();
                activity.getPlayer().start();
            } else {
                barTick.stop();
                activity.getPlayer().pause();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser) {
        if(seekBar.getProgress()>=seekBar.getMax() || !activity.getPlayer().isPlaying()) {
            activity.togglePlayButton(false);
        }
        if (activity.getPlayer() != null && isUser) {
            activity.getPlayer().seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private class SeekBarTick implements Runnable {
        private MediaPlayer player;
        private SeekBar bar;
        private boolean run = true;

        public SeekBarTick(SeekBar bar, MediaPlayer player) {
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
}
