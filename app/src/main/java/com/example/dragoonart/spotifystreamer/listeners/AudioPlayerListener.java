package com.example.dragoonart.spotifystreamer.listeners;

import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.R;

/**
 * Created by xnml on 2015-07-17.
 */
public class AudioPlayerListener implements CompoundButton.OnCheckedChangeListener,SeekBar.OnSeekBarChangeListener{
    private AudioPlayerActivity activity;
    public AudioPlayerListener(AudioPlayerActivity activity) {
        this.activity=activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if(compoundButton.getId() == R.id.player_playButton) {
            if(checked && !activity.getPlayer().isPlaying()) {
                activity.getPlayer().start();
            } else {
                activity.getPlayer().pause();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        activity.getPlayer().seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
