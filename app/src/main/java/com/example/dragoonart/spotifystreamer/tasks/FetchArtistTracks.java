package com.example.dragoonart.spotifystreamer.tasks;

import android.os.AsyncTask;

import com.example.dragoonart.spotifystreamer.ArtistTracksActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class FetchArtistTracks extends AsyncTask<String, Void, List<Track>> {
    private SpotifyApi api = new SpotifyApi();
    private String artistId;
    private ArtistTracksActivity activity;

    public FetchArtistTracks(String artistId, ArtistTracksActivity activity) {
        this.artistId = artistId;
        this.activity = activity;
    }

    @Override
    protected List<Track> doInBackground(String[] gg) {

        if (artistId == null || artistId.equals("")) {
            return null;
        }

        List<Artist> artists = new ArrayList<Artist>();
        SpotifyService spotifyService = api.getService();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("country", "US");
        Tracks tracksResult = spotifyService.getArtistTopTrack(artistId, queryMap);

        return tracksResult.tracks;

    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        activity.renderList(tracks);
        super.onPostExecute(tracks);
    }
}
