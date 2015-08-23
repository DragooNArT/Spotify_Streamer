package com.example.dragoonart.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class AudioPlayerActivityFragment extends Fragment {


    private static Intent playerIntent;
    private ArtistTrack currentTrack;
    private ArrayList<ArtistTrack> allTracks;
    private PlayerTrack currentPlayerTrack;
    private AudioPlayerListener listener;
    private AudioPlayerViewListener viewListener;
    private MediaPlayer player;
    private boolean isRegistered = false;
    private boolean doContinue = false;
    private View view;
    //connect to the service
    private ServiceConnection playerConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            //set the player
            binder.setActivity(AudioPlayerActivityFragment.this);
            player = binder.getService().preparePlayer(playerIntent, doContinue);
            doContinue = false;
            isRegistered = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            player = null;

        }
    };

    public AudioPlayerActivityFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_audio_player_fragment, container, false);
        disablePlayerControls();

        if (currentPlayerTrack != null) {
            doContinue = true;
            if (getActivity() != null) {
                getActivity().bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            }
        } else {
            FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
            fetchTrack.execute();
        }
        setPlayerData();
        return view;
    }


    private void setPlayerData() {

        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        ImageButton rewindButton = (ImageButton) findViewById(R.id.player_rewind);
        ImageButton fForwardButton = (ImageButton) findViewById(R.id.player_fastForward);
        ImageButton prevTrackButton = (ImageButton) findViewById(R.id.player_previousTrack);
        ImageButton nextTrackButton = (ImageButton) findViewById(R.id.player_nextTrack);

        prevTrackButton.setOnClickListener(viewListener);
        nextTrackButton.setOnClickListener(viewListener);
        rewindButton.setOnTouchListener(listener);
        fForwardButton.setOnTouchListener(listener);
        playButton.setChecked(doContinue);
        playButton.setOnCheckedChangeListener(listener);

        TextView trackName = (TextView) findViewById(R.id.player_trackName);
        trackName.setText(currentTrack.getTrackName());

        TextView albumName = (TextView) findViewById(R.id.player_albumName);
        albumName.setText(currentTrack.getAlbumName());


        TextView artistName = (TextView) findViewById(R.id.player_artistName);
        artistName.setText(currentTrack.getArtistName());

        ImageView albumImage = (ImageView) findViewById(R.id.player_albumImage);
        Picasso.with(getActivity()).load(currentTrack.getAlbumCoverFull()).into(albumImage);
    }

    public View findViewById(int id) {
        return view.findViewById(id);
    }


    public void enablePlayerControls() {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setEnabled(true);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.player_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void disablePlayerControls() {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setEnabled(false);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.player_progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void resetNumericControls() {
        SeekBar bar = (SeekBar) findViewById(R.id.player_seekBar);
        bar.setProgress(0);

        final Chronometer elapsed = (Chronometer) findViewById(R.id.player_timeElapsed);
        final Chronometer remaining = (Chronometer) findViewById(R.id.player_timeRemaining);
        elapsed.setText("00:00");
        remaining.setText("00:00");
    }

    public SeekBar getSeekBar() {
        return (SeekBar) findViewById(R.id.player_seekBar);
    }

    public void renderView(PlayerTrack track) {
        currentPlayerTrack = track;

        String previewUrl = track.getPreviewUrl();

        if (previewUrl != null && !previewUrl.equals("")) {
            if (playerIntent == null) {
                playerIntent = new Intent(getActivity(), PlayerService.class);
                playerIntent.setAction(PlayerService.ACTION_PLAY);
                getActivity().startService(playerIntent);
            }
            playerIntent.putExtra(PlayerService.DATA_SOURCE_URI, previewUrl);
            if (getActivity() != null) {
                getActivity().bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            }
        }
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

        reloadViewInternals();

    }

    private void reloadViewInternals() {
        //stop player animating threads
        listener.killPlayerWorkers();

        disablePlayerControls();
        resetNumericControls();
        setPlayerData();

        unbindPlayerService((AppCompatActivity) getActivity());
        FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
        fetchTrack.execute();
    }


    public void previousTrack() {
        int pos = allTracks.indexOf(currentTrack) - 1;
        if (pos < 0) {
            return;
        }
        currentTrack = allTracks.get(pos);
        reloadViewInternals();
    }

    public PlayerTrack getPlayerTrack() {
        return currentPlayerTrack;
    }

    public Parcelable getTrack() {
        return currentTrack;
    }

    public ServiceConnection unbindPlayerService(AppCompatActivity activity) {
        if (isRegistered) {
            activity.unbindService(playerConnection);
            isRegistered = false;
        }
        return null;
    }

    public void setCurrentPlayerTrack(PlayerTrack currentPlayerTrack) {
        this.currentPlayerTrack = currentPlayerTrack;
    }

    public void setAllTracks(ArrayList<ArtistTrack> allTracks) {
        this.allTracks = allTracks;
    }

    public void setCurrentTrack(ArtistTrack currentTrack) {
        this.currentTrack = currentTrack;
    }
}
