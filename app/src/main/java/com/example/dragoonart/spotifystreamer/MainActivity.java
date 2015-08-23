package com.example.dragoonart.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;
import com.example.dragoonart.spotifystreamer.beans.PlayerTrack;

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


        AudioPlayerActivityFragment fragmentPlayer = (AudioPlayerActivityFragment) getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragmentPlayer != null) {
            fragmentPlayer.setCurrentPlayerTrack(savedInstanceState.<PlayerTrack>getParcelable(AudioPlayerActivity.SAVED_PLAYER_TRACK_OBJECT_KEY));
            fragmentPlayer.setCurrentTrack(savedInstanceState.<ArtistTrack>getParcelable(AudioPlayerActivity.SAVED_ARTIST_TRACK_OBJECT_KEY));

            fragmentPlayer.setAllTracks(savedInstanceState.<ArtistTrack>getParcelableArrayList(AudioPlayerActivity.SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY));

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

        AudioPlayerActivityFragment fragmentPlayer = (AudioPlayerActivityFragment) getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragmentPlayer != null) {
            if (fragmentPlayer.getPlayerTrack() != null) {
                outState.putParcelable(AudioPlayerActivity.SAVED_PLAYER_TRACK_OBJECT_KEY, fragmentPlayer.getPlayerTrack());
            }
            if (fragmentPlayer.getTrack() != null) {
                outState.putParcelable(AudioPlayerActivity.SAVED_ARTIST_TRACK_OBJECT_KEY, fragmentPlayer.getTrack());
            }
            if (fragmentPlayer.getAllTracks() != null) {
                outState.putParcelableArrayList(AudioPlayerActivity.SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY, fragmentPlayer.getAllTracks());
            }
            fragmentPlayer.getPlayerListener().killPlayerWorkers();
        }


        super.onSaveInstanceState(outState);
    }
}
