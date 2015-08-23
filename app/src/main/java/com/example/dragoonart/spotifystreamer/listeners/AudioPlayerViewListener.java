package com.example.dragoonart.spotifystreamer.listeners;

import android.view.View;
import android.widget.ImageButton;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;
import com.example.dragoonart.spotifystreamer.R;

/**
 * Created by DragooNART on 8/21/2015.
 */
public class AudioPlayerViewListener implements ImageButton.OnClickListener {

    private AudioPlayerActivityFragment activity;

    public AudioPlayerViewListener(AudioPlayerActivityFragment activity) {
        this.activity = activity;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_previousTrack:
                activity.previousTrack();
                break;
            case R.id.player_nextTrack:
                activity.nextTrack();
                break;


        }

    }
}
