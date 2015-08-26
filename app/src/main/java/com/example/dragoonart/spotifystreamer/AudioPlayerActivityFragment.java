package com.example.dragoonart.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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


public class AudioPlayerActivityFragment extends DialogFragment {


    private static Intent playerIntent;
    private ArtistTrack currentTrack;
    private ArrayList<ArtistTrack> allTracks;
    private PlayerTrack currentPlayerTrack;
    private AudioPlayerListener listener;
    private AudioPlayerViewListener viewListener;
    private boolean isRegistered = false;
    private boolean doContinue = false;
    private PlayerService playerService;
    private View view;
    //connect to the service
    private ServiceConnection playerConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            //set the player
            binder.setActivity(AudioPlayerActivityFragment.this);
            playerService = binder.getService().preparePlayer(playerIntent, doContinue);
            doContinue = false;
            isRegistered = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isRegistered = false;
        }
    };

    public AudioPlayerActivityFragment() {
        listener = new AudioPlayerListener(this);
        viewListener = new AudioPlayerViewListener(this);
    }

    public AudioPlayerListener getPlayerListener() {
        return listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_audio_player_fragment, container, false);
        togglePlayButton(false);

        if (currentPlayerTrack != null) {
            doContinue = true;
            if (getActivity() != null && !isRegistered) {
                getActivity().bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            }
        } else {
            if (currentTrack != null) {
                togglePlayerControls(false);
                FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
                fetchTrack.execute();
            }
        }
        if (currentTrack != null) {
            setPlayerData();
        }
        return view;
    }

    //   public void startPlayingMusic() {
    //     if (player != null && !player.isPlaying() && view != null) {
    //          ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
//
    //         playButton.setChecked(true);
    //         player.start();
    //      }
    //  }

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


    public ImageView getAlbumImageView() {
        if (view != null) {
            return (ImageView) findViewById(R.id.player_albumImage);
        }
        return null;
    }


    public void togglePlayerControls(boolean enabled) {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        ImageButton rewindButton = (ImageButton) findViewById(R.id.player_rewind);
        ImageButton fForwardButton = (ImageButton) findViewById(R.id.player_fastForward);

        ImageButton prevTrackButton = (ImageButton) findViewById(R.id.player_previousTrack);
        ImageButton nextTrackButton = (ImageButton) findViewById(R.id.player_nextTrack);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.player_progressBar);
        if (enabled) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        playButton.setEnabled(enabled);
        rewindButton.setEnabled(enabled);
        fForwardButton.setEnabled(enabled);
        if (allTracks.indexOf(currentTrack) == 0) {
            prevTrackButton.setEnabled(false);
        } else {
            prevTrackButton.setEnabled(enabled);
        }
        if (allTracks.indexOf(currentTrack) == allTracks.size() - 1) {
            nextTrackButton.setEnabled(false);
        } else {
            nextTrackButton.setEnabled(enabled);
        }


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
            if (getActivity() != null && !isRegistered) {
                getActivity().bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            }
        }
    }


    public void togglePlayButton(boolean b) {
        ToggleButton playButton = (ToggleButton) findViewById(R.id.player_playButton);
        playButton.setChecked(b);
    }

    public void nextTrack() {
        if (allTracks == null || currentTrack == null) {
            return;
        }
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

        togglePlayButton(false);
        togglePlayerControls(false);
        resetNumericControls();
        setPlayerData();

        unbindPlayerService(getActivity());
        FetchTrack fetchTrack = new FetchTrack(currentTrack.getTrackId(), this);
        fetchTrack.execute();
    }


    public void previousTrack() {
        if (allTracks == null || currentTrack == null) {
            return;
        }
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

    public ArtistTrack getTrack() {
        return currentTrack;
    }

    public ServiceConnection unbindPlayerService(FragmentActivity activity) {
        if (isRegistered) {
            if (activity != null) {
                activity.unbindService(playerConnection);
            } else if (getActivity() != null) {
                getActivity().unbindService(playerConnection);
            }
            isRegistered = false;
        }
        return null;
    }

    public void setCurrentPlayerTrack(PlayerTrack currentPlayerTrack) {
        this.currentPlayerTrack = currentPlayerTrack;
    }

    public void setCurrentTrack(ArtistTrack currentTrack) {
        this.currentTrack = currentTrack;
    }

    public ArrayList<ArtistTrack> getAllTracks() {
        return allTracks;
    }

    public void setAllTracks(ArrayList<ArtistTrack> allTracks) {
        this.allTracks = allTracks;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }
}
