package com.example.dragoonart.spotifystreamer.tasks;

import android.os.AsyncTask;

import com.example.dragoonart.spotifystreamer.AudioPlayerActivity;
import com.example.dragoonart.spotifystreamer.beans.PlayerTrack;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by xnml on 2015-07-10.
 */
public class FetchTrack extends AsyncTask<String, Void, Track> {

    private SpotifyApi api = new SpotifyApi();
    private String trackId;
    private AudioPlayerActivity activity;
    public FetchTrack(String trackId,AudioPlayerActivity activity) {
        this.trackId=trackId;
        this.activity=activity;
    }

    @Override
    protected Track doInBackground(String... strings) {

        return api.getService().getTrack(trackId);

    }

    @Override
    protected void onPostExecute(Track track) {
        PlayerTrack playerTrack = new PlayerTrack();
        activity.renderView(playerTrack);
        super.onPostExecute(track);
    }
}
