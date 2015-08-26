package com.example.dragoonart.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.PlayerTrack;

import java.util.ArrayList;

/**
 * Created by DragooNART on 8/23/2015.
 */
public class AudioPlayerActivity extends AppCompatActivity {

    public static final String SAVED_PLAYER_TRACK_OBJECT_KEY = "SAVED_PLAYER_TRACK";
    public static final String SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY = "SAVED_ALL_TRACKS";
    public static final String SAVED_ARTIST_TRACK_OBJECT_KEY = "com.example.dragoonart.spotifystreamer.beans.ArtistTrack";
    private Intent mShareIntent;
    private AudioPlayerActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragment != null) {
            return;
        }
        setContentView(R.layout.activity_audio_player);
        fragment = (AudioPlayerActivityFragment) getSupportFragmentManager().findFragmentById(R.id.player_activity_container);
        boolean shouldAdd = false;
        if (fragment == null) {
            shouldAdd = true;
            fragment = new AudioPlayerActivityFragment();
        }
        ArrayList<ArtistTrack> allTracks = getIntent().getParcelableArrayListExtra(SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY);
        if (allTracks != null) {
            fragment.setAllTracks(allTracks);
            fragment.setCurrentTrack(allTracks.get(getIntent().getIntExtra(SAVED_ARTIST_TRACK_OBJECT_KEY, 0)));
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_PLAYER_TRACK_OBJECT_KEY)) {
            fragment.setCurrentPlayerTrack((PlayerTrack) savedInstanceState.getParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY));
        }


        if (shouldAdd) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_activity_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragment != null && fragment.getPlayerService() != null && fragment.getPlayerService().isPlayerPlaying() && fragment.getPlayerListener() != null) {
            fragment.getPlayerListener().startPlayerWorkers();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (fragment != null) {
            if (fragment.getPlayerTrack() != null) {
                outState.putParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY, fragment.getPlayerTrack());
            }
            if (fragment.getTrack() != null) {
                outState.putParcelable(SAVED_ARTIST_TRACK_OBJECT_KEY, fragment.getTrack());
            }
            if (fragment.getAllTracks() != null) {
                outState.putParcelableArrayList(SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY, fragment.getAllTracks());
            }
            fragment.getPlayerListener().killPlayerWorkers();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_player, menu);

        // Find the MenuItem that we know has the ShareActionProvider
        MenuItem item = menu.findItem(R.id.player_trackid_share);

        // Get its ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Connect the dots: give the ShareActionProvider its Share Intent
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(mShareIntent);
        }

        // Return true so Android will know we want to display the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.player_trackid_share) {
            shareTrackUrl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTrackUrl() {
        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");
        mShareIntent.putExtra(Intent.EXTRA_TEXT, fragment.getPlayerTrack().getPreviewUrl());
        startActivity(mShareIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragment != null) {
            fragment.unbindPlayerService(this);
        }
    }
}
