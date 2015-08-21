package com.example.dragoonart.spotifystreamer.workers;

import android.widget.Chronometer;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DragooNART on 8/18/2015.
 */
public class TrackTimesWorker implements Runnable {

    private AudioPlayerActivity activity;

    public TrackTimesWorker(AudioPlayerActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        final Chronometer elapsed = (Chronometer) activity.findViewById(R.id.player_timeElapsed);
        final Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
        final DateFormat formatter = new SimpleDateFormat("mm:ss");

        while (activity.getPlayer() != null && activity.getPlayer().isPlaying()) {

            final Date elapsedDate = new Date(activity.getPlayer().getCurrentPosition());
            final Date remainingDate = new Date(activity.getPlayer().getDuration() - activity.getPlayer().getCurrentPosition());
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    elapsed.setText(formatter.format(elapsedDate));
                    remaining.setText(formatter.format(remainingDate));
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                //recalculate immediately
            }
        }

    }
}
