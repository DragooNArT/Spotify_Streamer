package com.example.dragoonart.spotifystreamer.workers;

import android.widget.Chronometer;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;
import com.example.dragoonart.spotifystreamer.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DragooNART on 8/18/2015.
 */
public class TrackTimesWorker implements Runnable {

    private AudioPlayerActivityFragment activity;
    private boolean alive = true;

    public TrackTimesWorker(AudioPlayerActivityFragment activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        final DateFormat formatter = new SimpleDateFormat("mm:ss");

        while (alive) {
            if (activity.getPlayerService() != null) {
                final Chronometer elapsed = (Chronometer) activity.findViewById(R.id.player_timeElapsed);
                final Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
                final Date elapsedDate = new Date(activity.getPlayerService().getCurrentPosition());
                final Date remainingDate = new Date(activity.getPlayerService().getDuration() - activity.getPlayerService().getCurrentPosition());
                if (activity != null && activity.getActivity() != null) {
                    activity.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            elapsed.setText(formatter.format(elapsedDate));
                            remaining.setText(formatter.format(remainingDate));
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    //recalculate immediately
                }
            }
        }

    }

    public void killMe() {
        alive = false;
        Thread.currentThread().interrupt();
    }
}
