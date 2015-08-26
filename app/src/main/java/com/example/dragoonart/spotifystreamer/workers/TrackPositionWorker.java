package com.example.dragoonart.spotifystreamer.workers;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;
import com.example.dragoonart.spotifystreamer.R;

/**
 * Created by DragooNART on 8/18/2015.
 */
public class TrackPositionWorker implements Runnable {

    private boolean alive = true;
    private AudioPlayerActivityFragment activity;
    private boolean forward;
    private int iterations = 1;

    public TrackPositionWorker(AudioPlayerActivityFragment activity, int id) {
        forward = id == R.id.player_fastForward;
        this.activity=activity;
    }

    private boolean playerIsAlive() {
        return alive && activity.getPlayerService() != null;
    }

    private int getIncrement() {
        int num = (activity.getPlayerService().getDuration() / 200) * ((iterations / 20) + 1);


               return num;
    }

    @Override
    public void run() {

        while(playerIsAlive()) {
            if(forward) {
                activity.getPlayerService().playerSeekTo(activity.getPlayerService().getCurrentPosition() + getIncrement());
            } else {
                activity.getPlayerService().playerSeekTo(activity.getPlayerService().getCurrentPosition() - getIncrement());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                alive = false;
            }
            iterations++;

        }

    }
    public boolean isAlive() {
        return alive;
    }
    public void killMe() {
        alive = false;
    }
}
