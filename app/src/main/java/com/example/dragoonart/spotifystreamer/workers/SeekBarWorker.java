package com.example.dragoonart.spotifystreamer.workers;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;

public class SeekBarWorker implements Runnable {
    private AudioPlayerActivityFragment activity;
    private boolean run = true;

    public SeekBarWorker(AudioPlayerActivityFragment activity) {

        this.activity = activity;
    }

    public void run() {

        while (run) {
            if (activity != null && activity.getActivity() != null) {
                activity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.getSeekBar().setProgress(activity.getPlayerService().getCurrentPosition());
                    }
                });

                try {
                    if (activity.getPlayerService() != null && activity.getPlayerService().isPlayerPlaying() && activity.getPlayerService().getDuration() > 0)
                        Thread.sleep(activity.getPlayerService().getDuration() / 200);
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