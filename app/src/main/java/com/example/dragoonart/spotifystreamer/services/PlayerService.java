package com.example.dragoonart.spotifystreamer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.AudioPlayerActivityFragment;
import com.example.dragoonart.spotifystreamer.MainActivity;
import com.example.dragoonart.spotifystreamer.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by DragooNART on 8/18/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "com.example.dragoonart.spotifystreamer.services.PlayerService.action.PLAY";
    public static final String DATA_SOURCE_URI = "SOURCE_URI_DATA";
    public static final int NOTIFICATION_ID = -92415;
    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mMediaPlayer = new MediaPlayer();
    private AudioPlayerActivityFragment activity;
    private boolean playerPrepared = false;
    private NotificationManager notificationManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        return START_NOT_STICKY;
    }

    public boolean isPlayerPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public PlayerService preparePlayer(Intent intent, boolean doContinue) {
        if (!doContinue) {
            try {
                String dataUri = intent.getStringExtra(DATA_SOURCE_URI);
                playerPrepared = false;
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(dataUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync();
        } else {
            doContinue();
            activity.getPlayerListener().startPlayerWorkers();
        }
        return this;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
            notificationManager = null;
        }
        if (activity != null) {
            activity.getPlayerListener().killPlayerWorkers();
            activity = null;
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerPrepared = false;
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        /*
    } else if(action.equals(ACTION_PAUSE)) {

    } else if(action.equals(ACTION_SEEK)) {

    }
*/
        return mBinder;
    }

    private void doContinue() {

        Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        remaining.setText(formatter.format(getDuration()));
        SeekBar seekBar = activity.getSeekBar();
        seekBar.setOnSeekBarChangeListener(activity.getPlayerListener());
        seekBar.setMax(getDuration());
    }

    /**
     * Called when MediaPlayer is ready
     */
    public void onPrepared(MediaPlayer player) {
        playerPrepared = true;
        Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        remaining.setText(formatter.format(player.getDuration()));
        SeekBar seekBar = activity.getSeekBar();
        seekBar.setOnSeekBarChangeListener(activity.getPlayerListener());
        seekBar.setMax(player.getDuration());
        activity.togglePlayerControls(true);
        startPlayingMusic();
        initNotification();
    }

    private void startPlayingMusic() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying() && playerPrepared) {

            mMediaPlayer.start();
            if (activity != null) {
                ToggleButton playButton = (ToggleButton) activity.findViewById(R.id.player_playButton);

                playButton.setChecked(true);
            }
        }


    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if (activity != null) {
            playerPrepared = false;
            activity.nextTrack();

        }

    }


    private void initNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        if (activity != null && activity.getActivity() != null) {
            notificationManager = (NotificationManager) activity.getActivity().getSystemService(ns);
            Notification.Builder builder = new Notification.Builder(activity.getActivity());
            ImageView imageView = activity.getAlbumImageView();
            imageView.buildDrawingCache();
            builder.setLargeIcon(imageView.getDrawingCache());
            builder.setSmallIcon(R.drawable.notification_template_icon_bg);

            builder.setTicker("Playing music");
            builder.setWhen(System.currentTimeMillis());
            builder.setOngoing(true);
            builder.setContentTitle("Now Playing - \"" + activity.getTrack().getTrackName() + "\"" + " by " + "\"" + activity.getTrack().getArtistName() + "\"");
            builder.setContentText("Album \"" + activity.getTrack().getAlbumName() + "\"");


            Intent intent;
            if (activity.getActivity().findViewById(R.id.tablet_masterpane) == null) {
                intent = new Intent(activity.getActivity(), AudioPlayerActivity.class);
            } else {
                intent = new Intent(activity.getActivity(), MainActivity.class);


            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Context context = getApplicationContext();
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(contentIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.getNotification());
        }

    }

    public void playerStart() {
        if (playerPrepared && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void playerPause() {
        if (playerPrepared && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public int getDuration() {
        if (playerPrepared) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (playerPrepared) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void playerSeekTo(int progress) {
        if (playerPrepared) {
            mMediaPlayer.seekTo(progress);
        }
    }

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }

        public void setActivity(AudioPlayerActivityFragment activity) {
            PlayerService.this.activity = activity;

        }
    }

}
