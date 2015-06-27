package com.example.dragoonart.spotifystreamer.beans;

import java.io.Serializable;

/**
 * Created by DragooNART on 6/28/2015.
 * <p/>
 * This bean holds relevant data for a single artist
 */
public class DiscoveredArtist implements Serializable {

    private String id;
    private String name;
    private String imageLoc;

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


}

