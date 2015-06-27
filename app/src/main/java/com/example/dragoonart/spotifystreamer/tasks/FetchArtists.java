package com.example.dragoonart.spotifystreamer.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragoonart.spotifystreamer.MainActivity;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class FetchArtists extends AsyncTask<String, Void, ArrayList<DiscoveredArtist>> {
    Exception e = null;
    private SpotifyApi api = new SpotifyApi();
    private String keyword;
    private MainActivity activity;
    public FetchArtists(String keyword, MainActivity activity) {
        this.keyword = keyword;
        this.activity = activity;
    }

    @Override
    protected ArrayList<DiscoveredArtist> doInBackground(String[] gg) {

        if (keyword == null || keyword.equals("") || keyword.length() < 3) {
            return null;
        }

        ArrayList<DiscoveredArtist> artists = new ArrayList<DiscoveredArtist>();
        SpotifyService spotifyService = api.getService();
        try {
            ArtistsPager artistsResult = spotifyService.searchArtists(keyword);
            for (Artist artist : artistsResult.artists.items) {
                DiscoveredArtist discArtist = new DiscoveredArtist();
                discArtist.setId(artist.id);
                discArtist.setName(artist.name);
                if (!artist.images.isEmpty())
                    discArtist.setImageLoc(artist.images.get(0).url);

                artists.add(discArtist);
            }
        } catch (Exception e) {
            this.e = e;
            return null;
        }

        return artists;

    }

    @Override
    protected void onPostExecute(ArrayList<DiscoveredArtist> artists) {
        if (e != null) {
            Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            activity.renderList(artists);
        }
        super.onPostExecute(artists);
    }
}
