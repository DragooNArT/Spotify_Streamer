package com.example.dragoonart.spotifystreamer.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.dragoonart.spotifystreamer.ArtistTracksActivity;
import com.example.dragoonart.spotifystreamer.MainActivity;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;

import java.util.List;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class ArtistListClickListener implements AdapterView.OnItemClickListener {

    private MainActivity activity;
    private List<DiscoveredArtist> artists;

    public ArtistListClickListener(MainActivity activity, List<DiscoveredArtist> artists) {
        this.activity = activity;
        this.artists = artists;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DiscoveredArtist artist = artists.get(position);
        Intent artistActivity = new Intent(activity, ArtistTracksActivity.class);
        artistActivity.putExtra(Intent.EXTRA_TEXT, artist.getId());
        artistActivity.putExtra(MainActivity.EXTRA_ARTIST_NAME_KEY, artist.getName());
        activity.startActivity(artistActivity);
    }
}
