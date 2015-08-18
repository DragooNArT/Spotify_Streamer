package com.example.dragoonart.spotifystreamer.workers;

import android.media.MediaPlayer;
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
        MediaPlayer player = activity.getPlayer();
        while (player.isPlaying()) {
            final Chronometer elapsed = (Chronometer) activity.findViewById(R.id.player_timeElapsed);
            final Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
            final DateFormat formatter = new SimpleDateFormat("mm:ss");
            final Date elapsedDate = new Date(player.getCurrentPosition());

            final Date remainingDate = new Date(player.getDuration() - player.getCurrentPosition());
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
