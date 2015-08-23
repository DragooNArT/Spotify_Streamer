package com.example.dragoonart.spotifystreamer.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;

import java.util.ArrayList;

/**
 * Created by xnml on 2015-06-30.
 */
public class TrackListClickListener implements AdapterView.OnItemClickListener {

    private Activity activity;
    private ArrayList<ArtistTrack> artistTracks;

    public TrackListClickListener(Activity activity, ArrayList<ArtistTrack> artistTracks) {
        this.activity = activity;
        this.artistTracks = artistTracks;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent playerActivity = new Intent(activity, AudioPlayerActivity.class);
        playerActivity.putExtra(AudioPlayerActivity.SAVED_ARTIST_TRACK_OBJECT_KEY, position);
        playerActivity.putParcelableArrayListExtra(AudioPlayerActivity.SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY, artistTracks);
        activity.startActivity(playerActivity);
    }
}
