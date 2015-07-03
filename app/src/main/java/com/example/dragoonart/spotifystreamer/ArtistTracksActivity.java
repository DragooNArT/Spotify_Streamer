package com.example.dragoonart.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.dragoonart.spotifystreamer.adapters.ArtistTrackView;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;
import com.example.dragoonart.spotifystreamer.helpers.ListViewHelper;
import com.example.dragoonart.spotifystreamer.listeners.TrackListClickListener;
import com.example.dragoonart.spotifystreamer.tasks.FetchArtistTracks;

import java.util.ArrayList;

public class ArtistTracksActivity extends AppCompatActivity {

    public static final String SAVED_ARTIST_OBJECT_KEY = "com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist";
    private static final String SAVED_DATA_KEY = "ARTISTS_DATA";
    private ArrayList<ArtistTrack> artistTracks;
    private DiscoveredArtist artist = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_tracks);
        this.artist = getIntent().getParcelableExtra(SAVED_ARTIST_OBJECT_KEY);
        setArtistTitle();
        if (savedInstanceState != null) {
            ArrayList<ArtistTrack> artistTracks = (ArrayList<ArtistTrack>) savedInstanceState.getSerializable(SAVED_DATA_KEY);
            renderList(artistTracks);
        }
        FetchArtistTracks fetchTask = new FetchArtistTracks(artist.getId(), this);
        fetchTask.execute();
    }

    /**
     * Set the activity title to artist's name
     */
    private void setArtistTitle() {
        if (artist != null && artist.getName() != null) {
            setTitle(artist.getName() + " Top Tracks");

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
        ListView trackList = (ListView) findViewById(R.id.artist_tracks_list);
        if (tracks == null) {
            ListViewHelper.displayEmptyList(this, trackList, "No Tracks found? Hmm....");
        } else {
            ArtistTrackView trackView = new ArtistTrackView(getBaseContext(), tracks.toArray(new ArtistTrack[tracks.size()]));
            trackList.setAdapter(trackView);
            TrackListClickListener listener = new TrackListClickListener(this,tracks);
            trackList.setOnItemClickListener(listener);

        }
    }


}
