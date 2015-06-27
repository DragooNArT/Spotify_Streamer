package com.example.dragoonart.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.dragoonart.spotifystreamer.adapters.ArtistTrackView;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.tasks.FetchArtistTracks;

import java.util.ArrayList;

public class ArtistTracksActivity extends AppCompatActivity {

    public static final String SAVED_DATA_KEY = "ARTISTS_DATA";

    private ArrayList<ArtistTrack> artistTracks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_tracks);
        String artistId = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        setArtistTitle();
        if (savedInstanceState != null) {
            ArrayList<ArtistTrack> artistTracks = (ArrayList<ArtistTrack>) savedInstanceState.getSerializable(SAVED_DATA_KEY);
            renderList(artistTracks);
        }
        FetchArtistTracks fetchTask = new FetchArtistTracks(artistId, this);
        fetchTask.execute();
    }

    /**
     * Set the activity title to artist's name
     */
    private void setArtistTitle() {
        String artistName = getIntent().getStringExtra(MainActivity.EXTRA_ARTIST_NAME_KEY);
        if (artistName != null) {
            setTitle(artistName + " Top Tracks");

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (artistTracks != null) {
            outState.putSerializable(SAVED_DATA_KEY, artistTracks);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * fill artist tracks list with items from spotify
     *
     * @param tracks
     */
    public void renderList(ArrayList<ArtistTrack> tracks) {
        this.artistTracks = tracks;
        if (tracks == null) {

        } else {
            ListView artistList = (ListView) findViewById(R.id.artist_tracks_list);
            ArtistTrackView trackView = new ArtistTrackView(getBaseContext(), tracks.toArray(new ArtistTrack[tracks.size()]));
            artistList.setAdapter(trackView);
        }
    }


}
