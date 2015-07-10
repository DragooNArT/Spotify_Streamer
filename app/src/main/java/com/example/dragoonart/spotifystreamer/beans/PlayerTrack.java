package com.example.dragoonart.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xnml on 2015-07-10.
 */
public class PlayerTrack implements Parcelable {



    private String previewUrl;

    public PlayerTrack() {
    }

    public PlayerTrack(Parcel in) {
        this.previewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(previewUrl);
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }


    public static final Parcelable.Creator<PlayerTrack> CREATOR = new
            Parcelable.Creator<PlayerTrack>() {

                @Override
                public PlayerTrack createFromParcel(Parcel source) {
                    return new PlayerTrack(source);
                }

                @Override
                public PlayerTrack[] newArray(int size) {
                    return new PlayerTrack[size];
                }
            };
}
