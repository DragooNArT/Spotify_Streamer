package com.example.dragoonart.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DragooNART on 6/28/2015.
 * <p/>
 * This bean holds relevant data for a single artist track
 */
public class ArtistTrack implements Parcelable {
    public static final Parcelable.Creator<ArtistTrack> CREATOR = new
            Parcelable.Creator<ArtistTrack>() {

                @Override
                public ArtistTrack createFromParcel(Parcel source) {
                    return new ArtistTrack(source);
                }

                @Override
                public ArtistTrack[] newArray(int size) {
                    return new ArtistTrack[size];
                }
            };
    private String trackName;
    private String albumName;
    private String albumCoverThumbnail;
    private String albumCoverFull;

    public ArtistTrack() {

    }

    public ArtistTrack(Parcel in) {
        this.trackName = in.readString();
        this.albumName = in.readString();
        this.albumCoverThumbnail = in.readString();
        this.albumCoverFull = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(albumCoverThumbnail);
        dest.writeString(albumCoverFull);
    }
}
