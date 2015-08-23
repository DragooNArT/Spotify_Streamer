package com.example.dragoonart.spotifystreamer.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.dragoonart.spotifystreamer.ArtistTracksActivity;
import com.example.dragoonart.spotifystreamer.ArtistTracksActivityFragment;
import com.example.dragoonart.spotifystreamer.MainActivityFragment;
import com.example.dragoonart.spotifystreamer.R;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;

import java.util.List;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class ArtistListClickListener implements AdapterView.OnItemClickListener {

    private MainActivityFragment activity;
    private List<DiscoveredArtist> artists;

    public ArtistListClickListener(MainActivityFragment activity, List<DiscoveredArtist> artists) {
        this.activity = activity;
        this.artists = artists;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DiscoveredArtist artist = artists.get(position);


        if (activity.getActivity().findViewById(R.id.tablet_masterpane) == null) {
            Intent artistActivity = new Intent(activity.getActivity(), ArtistTracksActivity.class);
            artistActivity.putExtra(ArtistTracksActivityFragment.SAVED_ARTIST_OBJECT_KEY, artist);
            activity.startActivity(artistActivity);
        } else {
            ArtistTracksActivityFragment fragment = new ArtistTracksActivityFragment();
            fragment.setArtist(artist);
            activity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tablet_trackListContainer, fragment).commit();
        }
    }
}
