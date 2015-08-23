package com.example.dragoonart.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;

import java.util.ArrayList;

/**
 * Created by DragooNART on 8/23/2015.
 */
public class MainActivity extends AppCompatActivity {

    public static final String SAVED_DATA_KEY = "SEARCH_DATA";
    private MainActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.headlines_fragment);
        if (fragment == null) {
            fragment = new MainActivityFragment();
        }
        ArtistTracksActivityFragment fragmentArtist = (ArtistTracksActivityFragment) getSupportFragmentManager().findFragmentById(R.id.tablet_trackListContainer);
        if (fragmentArtist != null) {
            fragmentArtist.setTracks(savedInstanceState.<ArtistTrack>getParcelableArrayList(ArtistTracksActivityFragment.SAVED_DATA_KEY));
        }

        if (findViewById(R.id.tablet_masterpane) == null) {
            if (savedInstanceState != null) {
                ArrayList<DiscoveredArtist> artists = (ArrayList<DiscoveredArtist>) savedInstanceState.getSerializable(SAVED_DATA_KEY);
                fragment.setArtists(artists);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_container, fragment)
                    .commit();
        } else {
            if (savedInstanceState != null) {
                ArrayList<DiscoveredArtist> artists = (ArrayList<DiscoveredArtist>) savedInstanceState.getSerializable(SAVED_DATA_KEY);
                fragment.renderList(artists);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fragment != null && fragment.getArtists() != null) {
            outState.putSerializable(SAVED_DATA_KEY, fragment.getArtists());
        }
        ArtistTracksActivityFragment fragmentArtist = (ArtistTracksActivityFragment) getSupportFragmentManager().findFragmentById(R.id.tablet_trackListContainer);
        if (fragmentArtist != null) {
            outState.putParcelableArrayList(ArtistTracksActivityFragment.SAVED_DATA_KEY, fragmentArtist.getArtistTracks());
        }
        super.onSaveInstanceState(outState);
    }
}
