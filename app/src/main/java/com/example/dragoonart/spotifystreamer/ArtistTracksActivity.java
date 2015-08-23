package com.example.dragoonart.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DragooNART on 8/23/2015.
 */
public class ArtistTracksActivity extends AppCompatActivity {
    private ArtistTracksActivityFragment fragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_tracks);
        fragment = new ArtistTracksActivityFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.artist_activity_container, fragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (fragment != null && fragment.getArtistTracks() != null) {
            outState.putParcelableArrayList(ArtistTracksActivityFragment.SAVED_DATA_KEY, fragment.getArtistTracks());
        }
        super.onSaveInstanceState(outState);
    }
}
