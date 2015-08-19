package com.example.dragoonart.spotifystreamer.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.R;
import com.example.dragoonart.spotifystreamer.workers.SeekBarWorker;
import com.example.dragoonart.spotifystreamer.workers.TrackPositionWorker;
import com.example.dragoonart.spotifystreamer.workers.TrackTimesWorker;

/**
 * Created by xnml on 2015-07-17.
 */
public class AudioPlayerListener implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, ImageButton.OnTouchListener, ImageButton.OnClickListener {
    private AudioPlayerActivity activity;
    private SeekBarWorker barWorker;
    private Thread trackTimes;
    private TrackPositionWorker posWorker = null;

    public AudioPlayerListener(AudioPlayerActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (compoundButton.getId() == R.id.player_playButton) {
            if (checked && activity.getPlayer() != null && !activity.getPlayer().isPlaying()) {
                barWorker = new SeekBarWorker(activity.getSeekBar(), activity);
                new Thread(barWorker).start();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.getPlayer().start();
                    }
                });

                trackTimes = new Thread(new TrackTimesWorker(activity));
                trackTimes.start();
            } else {
                if (barWorker != null)
                    barWorker.stop();
                if (activity.getPlayer() != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.getPlayer().pause();
                        }
                    });
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean isUser) {
        if (seekBar.getProgress() >= seekBar.getMax() || activity.getPlayer() == null || !activity.getPlayer().isPlaying()) {
            activity.togglePlayButton(false);
        }
        if (isUser && activity.getPlayer() != null) {
            trackTimes.interrupt();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.getPlayer().seekTo(progress);
                }
            });

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private boolean performPositionModification(int buttonId, MotionEvent event) {

        if (buttonId == R.id.player_fastForward || buttonId == R.id.player_rewind) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (posWorker != null && posWorker.isAlive()) {
                    posWorker.killMe();
                    posWorker = null;
                }
                posWorker = new TrackPositionWorker(activity, buttonId);
                new Thread(posWorker).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (posWorker != null) {
                    posWorker.killMe();
                    posWorker = null;
                }
            }
            trackTimes.interrupt();
            return true;
        }


        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean result = performPositionModification(v.getId(), event);

        return result;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.player_previousTrack ) {
            activity.previousTrack();
        } else if (view.getId() == R.id.player_nextTrack ) {
            activity.nextTrack();
        }
    }
}
