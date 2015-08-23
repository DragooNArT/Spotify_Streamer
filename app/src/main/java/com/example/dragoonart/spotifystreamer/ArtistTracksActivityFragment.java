package com.example.dragoonart.spotifystreamer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dragoonart.spotifystreamer.adapters.ArtistTrackView;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;
import com.example.dragoonart.spotifystreamer.helpers.ListViewHelper;
import com.example.dragoonart.spotifystreamer.listeners.TrackListClickListener;
import com.example.dragoonart.spotifystreamer.tasks.FetchArtistTracks;

import java.util.ArrayList;

public class ArtistTracksActivityFragment extends Fragment {

    public static final String SAVED_ARTIST_OBJECT_KEY = "com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist";
    public static final String SAVED_DATA_KEY = "ARTISTS_DATA";

    private ArrayList<ArtistTrack> artistTracks;
    private DiscoveredArtist artist = null;
    private View view;

    public void setArtist(DiscoveredArtist artist) {
        this.artist = artist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_artist_tracks_detail, container, false);
        if (this.artist == null) {
            this.artist = getActivity().getIntent().getParcelableExtra(SAVED_ARTIST_OBJECT_KEY);
        }

        setArtistTitle();
        if (savedInstanceState != null) {
            ArrayList<ArtistTrack> artistTracks = savedInstanceState.getParcelableArrayList(SAVED_DATA_KEY);
            renderList(artistTracks);
        } else {
            FetchArtistTracks fetchTask = new FetchArtistTracks(artist, this);
            fetchTask.execute();
        }
        return view;
    }

    public View findViewById(int id) {
        return view.findViewById(id);
    }

    /**
     * Set the activity title to artist's name
     */
    private void setArtistTitle() {
        if (artist != null && artist.getName() != null) {
            getActivity().setTitle(artist.getName() + " Top Tracks");

        }
    }


    /**
     * fill artist tracks list with items from spotify
     *
     * @param tracks
     */
    public void renderList(ArrayList<ArtistTrack> tracks) {
        this.artistTracks = tracks;
        ListView trackList = (ListView) findViewById(R.id.artist_tracks_list);
        if (tracks == null) {
            ListViewHelper.displayEmptyList(trackList, "No Tracks found? Hmm....");
        } else {
            ArtistTrackView trackView = new ArtistTrackView(getActivity(), tracks.toArray(new ArtistTrack[tracks.size()]));
            trackList.setAdapter(trackView);
            TrackListClickListener listener = new TrackListClickListener(getActivity(), tracks);
            trackList.setOnItemClickListener(listener);

        }
    }


    public ArrayList<ArtistTrack> getArtistTracks() {
        return artistTracks;
    }
}
