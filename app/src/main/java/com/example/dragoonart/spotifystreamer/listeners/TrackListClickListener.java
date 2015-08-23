package com.example.dragoonart.spotifystreamer.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;
import com.example.dragoonart.spotifystreamer.R;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;

import java.util.ArrayList;

/**
 * Created by xnml on 2015-06-30.
 */
public class TrackListClickListener implements AdapterView.OnItemClickListener {

    private FragmentActivity activity;
    private ArrayList<ArtistTrack> artistTracks;

    public TrackListClickListener(FragmentActivity activity, ArrayList<ArtistTrack> artistTracks) {
        this.activity = activity;
        this.artistTracks = artistTracks;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (activity.findViewById(R.id.tablet_masterpane) == null) {
            Intent playerActivity = new Intent(activity, AudioPlayerActivity.class);
            playerActivity.putExtra(AudioPlayerActivity.SAVED_ARTIST_TRACK_OBJECT_KEY, position);
            playerActivity.putParcelableArrayListExtra(AudioPlayerActivity.SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY, artistTracks);
            activity.startActivity(playerActivity);
        } else {
            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            AudioPlayerActivityFragment newFragment = new AudioPlayerActivityFragment();
            newFragment.setAllTracks(artistTracks);
            newFragment.setCurrentTrack(artistTracks.get(position));
            newFragment.show(ft, "dialog");

        }
    }
}
