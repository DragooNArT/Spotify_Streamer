package com.example.dragoonart.spotifystreamer.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.dragoonart.spotifystreamer.ArtistTracksActivity;
import com.example.dragoonart.spotifystreamer.MainActivity;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class ArtistListClickListener implements AdapterView.OnItemClickListener {

    private MainActivity activity;
    private List<Artist> artists;

    public ArtistListClickListener(MainActivity activity, List<Artist> artists) {
        this.activity = activity;
        this.artists = artists;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Artist artist = artists.get(position);
        Intent artistActivity = new Intent(activity, ArtistTracksActivity.class);
        artistActivity.putExtra(Intent.EXTRA_TEXT, artist.id);
        activity.startActivity(artistActivity);
    }
}
