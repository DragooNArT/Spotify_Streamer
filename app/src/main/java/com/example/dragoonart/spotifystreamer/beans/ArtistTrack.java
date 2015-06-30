package com.example.dragoonart.spotifystreamer.beans;

import java.io.Serializable;

/**
 * Created by DragooNART on 6/28/2015.
 * <p/>
 * This bean holds relevant data for a single artist track
 */
public class ArtistTrack implements Serializable {
    private String trackName;
    private String albumName;
    private String albumCoverThumbnail;
    private String albumCoverFull;

    public String getAlbumCoverFull() {
        return albumCoverFull;
    }

    public void setAlbumCoverFull(String albumCoverFull) {
        this.albumCoverFull = albumCoverFull;
    }

    public String getAlbumCoverThumbnail() {
        return albumCoverThumbnail;
    }

    public void setAlbumCoverThumbnail(String albumCoverThumbnail) {
        this.albumCoverThumbnail = albumCoverThumbnail;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
