package com.example.dragoonart.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DragooNART on 6/28/2015.
 * <p/>
 * This bean holds relevant data for a single artist
 */
public class DiscoveredArtist implements Parcelable {

    public static final Parcelable.Creator<DiscoveredArtist> CREATOR = new
            Parcelable.Creator<DiscoveredArtist>() {

                @Override
                public DiscoveredArtist createFromParcel(Parcel source) {
                    return new DiscoveredArtist(source);
                }

                @Override
                public DiscoveredArtist[] newArray(int size) {
                    return new DiscoveredArtist[size];
                }
            };
    private String id;
    private String name;
    private String imageLoc;

    public DiscoveredArtist() {

    }

    public DiscoveredArtist(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.imageLoc = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLoc() {
        return imageLoc;
    }

    public void setImageLoc(String imageLoc) {
        this.imageLoc = imageLoc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageLoc);
    }
}

