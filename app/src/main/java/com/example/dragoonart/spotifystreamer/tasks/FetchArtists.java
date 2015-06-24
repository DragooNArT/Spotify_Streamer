package com.example.dragoonart.spotifystreamer.tasks;

import android.os.AsyncTask;

import com.example.dragoonart.spotifystreamer.MainActivity;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class FetchArtists extends AsyncTask<String, Void, List<Artist>> {
    private SpotifyApi api = new SpotifyApi();
    private String keyword;
    private MainActivity activity;

    public FetchArtists(String keyword, MainActivity activity) {
        this.keyword = keyword;
        this.activity = activity;
    }

    @Override
    protected List<Artist> doInBackground(String[] gg) {

        if (keyword == null || keyword.equals("") || keyword.length() < 3) {
            return null;
        }

        List<Artist> artists = new ArrayList<Artist>();
        SpotifyService spotifyService = api.getService();
        ArtistsPager artistsResult = spotifyService.searchArtists(keyword);

        return artistsResult.artists.items;

    }

    @Override
    protected void onPostExecute(List<Artist> artists) {
        activity.renderList(artists);
        super.onPostExecute(artists);
    }
}
