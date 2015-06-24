package com.example.dragoonart.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.dragoonart.spotifystreamer.adapters.ArtistTrackView;
import com.example.dragoonart.spotifystreamer.tasks.FetchArtistTracks;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class ArtistTracksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_tracks);
        String artistId = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        FetchArtistTracks fetchTask = new FetchArtistTracks(artistId, this);
        fetchTask.execute();
    }

    public void renderList(List<Track> tracks) {
        if (tracks == null) {

        } else {
            ListView artistList = (ListView) findViewById(R.id.artist_tracks_list);
            ArtistTrackView trackView = new ArtistTrackView(getBaseContext(), tracks.toArray(new Track[tracks.size()]));
            artistList.setAdapter(trackView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
