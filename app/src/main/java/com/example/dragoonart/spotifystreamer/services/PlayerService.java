package com.example.dragoonart.spotifystreamer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Chronometer;
import android.widget.SeekBar;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by DragooNART on 8/18/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "com.example.dragoonart.spotifystreamer.services.PlayerService.action.PLAY";
    public static final String DATA_SOURCE_URI = "SOURCE_URI_DATA";
    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mMediaPlayer = new MediaPlayer();
    WifiManager.WifiLock wifiLock;
    private AudioPlayerActivity activity;
    public MediaPlayer getPlayer() {
        return mMediaPlayer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        return START_NOT_STICKY;
    }

    public MediaPlayer preparePlayer(Intent intent) {
        try {
            String dataUri = intent.getStringExtra(DATA_SOURCE_URI);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(dataUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "bufferSong");

        wifiLock.acquire();
        mMediaPlayer.prepareAsync();
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
        //player.start();
        if (wifiLock != null) {
            wifiLock.release();
        }
        Chronometer remaining = (Chronometer) activity.findViewById(R.id.player_timeRemaining);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        remaining.setText(formatter.format(player.getDuration()));
        SeekBar seekBar = activity.getSeekBar();
        seekBar.setOnSeekBarChangeListener(activity.getPlayerListener());
        seekBar.setMax(player.getDuration());
        activity.enablePlayerControls();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }

    /*
        private void initNotification() {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(ns);
            Notification notification = new Notification(R.drawable.notification_template_icon_bg, "tutori", System.currentTimeMillis());
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            Context context = getApplicationContext();
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activity.getIntent(), 0);
            notification.setLatestEventInfo(context, "fada", "bada", contentIntent);
            mNotificationManager.notify(123, notification);
        }
    */
    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }

        public void setActivity(AudioPlayerActivity activity) {
            PlayerService.this.activity = activity;
            // initNotification();
        }
    }

}