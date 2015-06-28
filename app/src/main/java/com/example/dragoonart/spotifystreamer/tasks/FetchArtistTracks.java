package com.example.dragoonart.spotifystreamer.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragoonart.spotifystreamer.ArtistTracksActivity;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.helpers.FetchTasksHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class FetchArtistTracks extends AsyncTask<String, Void, ArrayList<ArtistTrack>> {
    private SpotifyApi api = new SpotifyApi();
    private String artistId;
    private ArtistTracksActivity activity;
    private Exception e;

    public FetchArtistTracks(String artistId, ArtistTracksActivity activity) {
        this.artistId = artistId;
        this.activity = activity;
    }

    @Override
    protected ArrayList<ArtistTrack> doInBackground(String[] gg) {

        if (artistId == null || artistId.equals("")) {
            return null;
        }

        ArrayList<ArtistTrack> artistTracks = new ArrayList<ArtistTrack>();
        SpotifyService spotifyService = api.getService();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("country", "US");
        Tracks tracksResult = null;
        try {
            tracksResult = spotifyService.getArtistTopTrack(artistId, queryMap);
            for (Track track : tracksResult.tracks) {
                ArtistTrack artistTrack = new ArtistTrack();
                artistTrack.setAlbumName(track.album.name);
                artistTrack.setTrackName(track.name);
                if (!track.album.images.isEmpty())
                    artistTrack.setAlbumCoverImgLoc(FetchTasksHelper.getThumbnailImage(track.album.images));

                artistTracks.add(artistTrack);
            }
        } catch (Exception e) {
            this.e = e;
            return null;
        }
        return artistTracks;

    }

    @Override
    protected void onPostExecute(ArrayList<ArtistTrack> tracks) {
        if (e != null) {
            Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            activity.renderList(tracks);
        }
        super.onPostExecute(tracks);
    }
}
