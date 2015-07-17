package com.example.dragoonart.spotifystreamer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.PlayerTrack;
import com.example.dragoonart.spotifystreamer.listeners.AudioPlayerListener;
import com.example.dragoonart.spotifystreamer.tasks.FetchTrack;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class AudioPlayerActivity extends AppCompatActivity {

    public static final String SAVED_ARTIST_TRACK_OBJECT_KEY = "com.example.dragoonart.spotifystreamer.beans.ArtistTrack";
    private static final String SAVED_TRACK_OBJECT_KEY = "SAVED_TRACK";
    private static final String SAVED_PLAYER_TRACK_OBJECT_KEY = "SAVED_PLAYER_TRACK";
    private ArtistTrack track;
    private PlayerTrack playerTrack;

    private AudioPlayerListener listener = new AudioPlayerListener(this);

    public MediaPlayer getPlayer() {
        return player;
    }

    private MediaPlayer player = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        this.track = getIntent().getParcelableExtra(SAVED_ARTIST_TRACK_OBJECT_KEY);
        setPlayerData();

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_PLAYER_TRACK_OBJECT_KEY)) {
            playerTrack = savedInstanceState.getParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY);
        } else {
            FetchTrack fetchTrack = new FetchTrack(track.getTrackId(), this);
            fetchTrack.execute();
        }
    }

    private void setPlayerData() {

        ToggleButton playButton  = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setOnCheckedChangeListener(listener);
        TextView trackName = (TextView) findViewById(R.id.player_trackName);
        trackName.setText(track.getTrackName());

        TextView albumName = (TextView) findViewById(R.id.player_albumName);
        albumName.setText(track.getAlbumName());


        TextView artistName = (TextView) findViewById(R.id.player_artistName);
        artistName.setText(track.getArtistName());

        ImageView albumImage = (ImageView) findViewById(R.id.player_albumImage);
        Picasso.with(getBaseContext()).load(track.getAlbumCoverFull()).into(albumImage);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (playerTrack != null) {
            outState.putParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY, playerTrack);
        }
        if (track != null) {
            outState.putParcelable(SAVED_TRACK_OBJECT_KEY, track);
        }
        super.onSaveInstanceState(outState);
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
    private void disablePlayerControls () {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setEnabled(false);
    }

    public void renderView(PlayerTrack track) {
        playerTrack = track;
        SeekBar seekBar = (SeekBar) findViewById(R.id.player_seekBar);
        seekBar.setOnSeekBarChangeListener(listener);
        String previewUrl = track.getPreviewUrl();
        if (previewUrl != null && !previewUrl.equals("")) {
            try {
                player.setDataSource(previewUrl);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            disablePlayerControls();
        }
    }
}
