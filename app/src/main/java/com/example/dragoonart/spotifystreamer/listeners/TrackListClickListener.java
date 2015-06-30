package com.example.dragoonart.spotifystreamer.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.dragoonart.spotifystreamer.ArtistTracksActivity;
import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;

import java.util.List;

/**
 * Created by xnml on 2015-06-30.
 */
public class TrackListClickListener implements AdapterView.OnItemClickListener {

    private ArtistTracksActivity activity;
    private List<ArtistTrack> artists;

    public TrackListClickListener(ArtistTracksActivity activity, List<ArtistTrack> artists) {
        this.activity = activity;
        this.artists = artists;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ArtistTrack artist = artists.get(position);
        Intent playerActivity = new Intent(activity, AudioPlayerActivity.class);
        //TODO add payload!
        //artistActivity.putExtra(Intent.PAR, artist.getName());
        activity.startActivity(playerActivity);
    }
}
