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
    public MediaPlayer getPlayer() {
        return mMediaPlayer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        return START_NOT_STICKY;
    }

    public MediaPlayer preparePlayer(Intent intent, boolean doContinue) {
        if (!doContinue) {
            try {
                String dataUri = intent.getStringExtra(DATA_SOURCE_URI);
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(dataUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync();
        } else {
            onPrepared(mMediaPlayer);
            activity.getPlayerListener().startPlayerWorkers();
        }
        return mMediaPlayer;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
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

    /**
     * Called when MediaPlayer is ready
     */

    public void onPrepared(MediaPlayer player) {
        Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        remaining.setText(formatter.format(player.getDuration()));
        SeekBar seekBar = activity.getSeekBar();
        seekBar.setOnSeekBarChangeListener(activity.getPlayerListener());
        seekBar.setMax(player.getDuration());
        activity.togglePlayerControls(true);
        activity.startPlayingMusic();
        initNotification();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (activity != null) {
            activity.nextTrack();

        }
    }


    private void initNotification() {
            String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) activity.getActivity().getSystemService(ns);
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

        Context context = getApplicationContext();
        Intent intent;
        if (activity.getActivity().findViewById(R.id.tablet_masterpane) == null) {
            intent = new Intent(activity.getActivity(), AudioPlayerActivity.class);
        } else {
            intent = new Intent(activity.getActivity(), MainActivity.class);


        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        //notification.setLatestEventInfo(context, "fada", "bada", contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.getNotification());

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
