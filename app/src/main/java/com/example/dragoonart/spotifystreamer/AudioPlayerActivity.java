package com.example.dragoonart.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.PlayerTrack;
import com.example.dragoonart.spotifystreamer.listeners.AudioPlayerListener;
import com.example.dragoonart.spotifystreamer.services.PlayerService;
import com.example.dragoonart.spotifystreamer.tasks.FetchTrack;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AudioPlayerActivity extends AppCompatActivity {

    public static final String SAVED_ARTIST_TRACK_OBJECT_KEY = "com.example.dragoonart.spotifystreamer.beans.ArtistTrack";
    private static final String SAVED_PLAYER_TRACK_OBJECT_KEY = "SAVED_PLAYER_TRACK";

    public static final String SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY = "SAVED_ALL_TRACKS";

    private ArtistTrack currentTrack;
    private ArrayList<ArtistTrack> allTracks;
    private PlayerTrack currentPlayerTrack;

    private AudioPlayerListener listener;
    private MediaPlayer player;
    private Intent playerIntent;
    //connect to the service
    private ServiceConnection playerConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            //set the player
            player = binder.getService().getPlayer();
            binder.setActivity(AudioPlayerActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            player = null;

        }
    };

    public AudioPlayerActivity() {
        listener = new AudioPlayerListener(this);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public AudioPlayerListener getPlayerListener() {
        return listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        this.allTracks = getIntent().getParcelableArrayListExtra(SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY);
        this.currentTrack = allTracks.get(getIntent().getIntExtra(SAVED_ARTIST_TRACK_OBJECT_KEY,0));

        setPlayerData();

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_PLAYER_TRACK_OBJECT_KEY)) {
            currentPlayerTrack = savedInstanceState.getParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY);
        } else {
            FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
            fetchTrack.execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playerIntent);
    }

    private void setPlayerData() {

        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        ImageButton rewindButton = (ImageButton) findViewById(R.id.player_rewind);
        ImageButton fFowrardButton = (ImageButton) findViewById(R.id.player_fastForward);
        ImageButton prevTrackButton = (ImageButton) findViewById(R.id.player_previousTrack);
        ImageButton nextTrackButton = (ImageButton) findViewById(R.id.player_nextTrack);

        prevTrackButton.setOnClickListener(listener);
        nextTrackButton.setOnClickListener(listener);
        rewindButton.setOnTouchListener(listener);
        fFowrardButton.setOnTouchListener(listener);
        playButton.setOnCheckedChangeListener(listener);
        TextView trackName = (TextView) findViewById(R.id.player_trackName);
        trackName.setText(currentTrack.getTrackName());

        TextView albumName = (TextView) findViewById(R.id.player_albumName);
        albumName.setText(currentTrack.getAlbumName());


        TextView artistName = (TextView) findViewById(R.id.player_artistName);
        artistName.setText(currentTrack.getArtistName());

        ImageView albumImage = (ImageView) findViewById(R.id.player_albumImage);
        Picasso.with(getBaseContext()).load(currentTrack.getAlbumCoverFull()).into(albumImage);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentPlayerTrack != null) {
            outState.putParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY, currentPlayerTrack);
        }
        if (currentTrack != null) {
            outState.putParcelable(SAVED_ARTIST_TRACK_OBJECT_KEY, currentTrack);
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

    private void disablePlayerControls() {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setEnabled(false);
    }

    public SeekBar getSeekBar() {
        return (SeekBar) findViewById(R.id.player_seekBar);
    }

    public void renderView(PlayerTrack track) {
        currentPlayerTrack = track;

        String previewUrl = track.getPreviewUrl();
        if (previewUrl != null && !previewUrl.equals("")) {

            playerIntent = new Intent(this, PlayerService.class);
            playerIntent.setAction(PlayerService.ACTION_PLAY);
            playerIntent.putExtra(PlayerService.DATA_SOURCE_URI, previewUrl);

            startService(playerIntent);
            bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);


        } else {
            disablePlayerControls();
        }
    }

    public void togglePlayButton(boolean b) {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setChecked(b);
    }

    public void nextTrack() {
        int pos = allTracks.indexOf(currentTrack)+1;
        if(pos>=allTracks.size()) {
            return;
        }
        currentTrack = allTracks.get(pos);

        reloadView();

    }

    private void reloadView() {

        if(playerIntent != null) {
            try {
                unbindService(playerConnection);
            } catch(Exception e) {
                //do nothing
            }
            stopService(playerIntent);

        }
        setPlayerData();
        FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
        fetchTrack.execute();
    }

    public void previousTrack() {
        int pos = allTracks.indexOf(currentTrack) - 1;
        if(pos<0) {
            return;
        }
        currentTrack = allTracks.get(pos);
        reloadView();
    }

}
