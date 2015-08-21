package com.example.dragoonart.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.PlayerTrack;
import com.example.dragoonart.spotifystreamer.listeners.AudioPlayerListener;
import com.example.dragoonart.spotifystreamer.listeners.AudioPlayerViewListener;
import com.example.dragoonart.spotifystreamer.services.PlayerService;
import com.example.dragoonart.spotifystreamer.tasks.FetchTrack;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AudioPlayerActivity extends AppCompatActivity {

    public static final String SAVED_ARTIST_TRACK_OBJECT_KEY = "com.example.dragoonart.spotifystreamer.beans.ArtistTrack";
    public static final String SAVED_ARTIST_ALL_TRACKS_OBJECT_KEY = "SAVED_ALL_TRACKS";
    private static final String SAVED_PLAYER_TRACK_OBJECT_KEY = "SAVED_PLAYER_TRACK";
    private static Intent playerIntent;
    private ArtistTrack currentTrack;
    private ArrayList<ArtistTrack> allTracks;
    private PlayerTrack currentPlayerTrack;
    private AudioPlayerListener listener;
    private AudioPlayerViewListener viewListener;
    private MediaPlayer player;
    private ProgressBar progressBar;
    //connect to the service
    private ServiceConnection playerConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            //set the player

            binder.setActivity(AudioPlayerActivity.this);
            player = binder.getService().preparePlayer(playerIntent);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            player = null;

        }
    };
    private Intent mShareIntent;

    public AudioPlayerActivity() {
        listener = new AudioPlayerListener(this);
        viewListener = new AudioPlayerViewListener(this);
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
        this.currentTrack = allTracks.get(getIntent().getIntExtra(SAVED_ARTIST_TRACK_OBJECT_KEY, 0));

        setPlayerData();
        disablePlayerControls();

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_PLAYER_TRACK_OBJECT_KEY)) {
            currentPlayerTrack = savedInstanceState.getParcelable(SAVED_PLAYER_TRACK_OBJECT_KEY);
            mShareIntent.putExtra(Intent.EXTRA_TEXT, currentPlayerTrack.getPreviewUrl());
        } else {
            FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
            fetchTrack.execute();
        }
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(playerConnection);
        stopService(playerIntent);
    }
*/


    private void setPlayerData() {

        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        ImageButton rewindButton = (ImageButton) findViewById(R.id.player_rewind);
        ImageButton fForwardButton = (ImageButton) findViewById(R.id.player_fastForward);
        ImageButton prevTrackButton = (ImageButton) findViewById(R.id.player_previousTrack);
        ImageButton nextTrackButton = (ImageButton) findViewById(R.id.player_nextTrack);

        progressBar = (ProgressBar) findViewById(R.id.player_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        prevTrackButton.setOnClickListener(viewListener);
        nextTrackButton.setOnClickListener(viewListener);
        rewindButton.setOnTouchListener(listener);
        fForwardButton.setOnTouchListener(listener);
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


    private void shareTrackUrl() {
        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");
        mShareIntent.putExtra(Intent.EXTRA_TEXT, getPlayerTrack().getPreviewUrl());
        startActivity(mShareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.player_trackid_share) {
            shareTrackUrl();
        }
        return super.onOptionsItemSelected(item);
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

    public void enablePlayerControls() {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setEnabled(true);
    }

    public void disablePlayerControls() {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setEnabled(false);
        SeekBar bar = (SeekBar) findViewById(R.id.player_seekBar);
        bar.setProgress(0);

        final Chronometer elapsed = (Chronometer) findViewById(R.id.player_timeElapsed);
        final Chronometer remaining = (Chronometer) findViewById(R.id.player_timeRemaining);
        elapsed.setText("0:00");
        remaining.setText("0:00");
    }

    public SeekBar getSeekBar() {
        return (SeekBar) findViewById(R.id.player_seekBar);
    }

    public void renderView(PlayerTrack track) {
        currentPlayerTrack = track;

        String previewUrl = track.getPreviewUrl();

        if (previewUrl != null && !previewUrl.equals("")) {
            if (playerIntent == null) {
                playerIntent = new Intent(this, PlayerService.class);
                playerIntent.setAction(PlayerService.ACTION_PLAY);
                startService(playerIntent);
            }
            playerIntent.putExtra(PlayerService.DATA_SOURCE_URI, previewUrl);
            bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(playerConnection);

    }

    public void togglePlayButton(boolean b) {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setChecked(b);
    }

    public void nextTrack() {
        int pos = allTracks.indexOf(currentTrack) + 1;
        if (pos >= allTracks.size()) {
            return;
        }
        currentTrack = allTracks.get(pos);

        reloadView();

    }

    private void reloadView() {
        listener.killPlayerWorkers();
        disablePlayerControls();
        player.reset();
        setPlayerData();
        unbindService(playerConnection);
        FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
        fetchTrack.execute();
    }


    public void previousTrack() {
        int pos = allTracks.indexOf(currentTrack) - 1;
        if (pos < 0) {
            return;
        }
        currentTrack = allTracks.get(pos);
        reloadView();
    }

    public PlayerTrack getPlayerTrack() {
        return currentPlayerTrack;
    }
}
