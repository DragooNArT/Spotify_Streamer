package com.example.dragoonart.spotifystreamer.helpers;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by DragooNART on 6/28/2015.
 */
public class FetchTasksHelper {

    private FetchTasksHelper() {

    }

    public static String getThumbnailImage(List<Image> images) {
        String result = null;
        long resolution = 0;
        for (Image img : images) {
            long currSize = img.height * img.width;
            if (currSize < resolution || resolution == 0) {
                resolution = currSize;
                result = img.url;
            }
        }
        return result;
    }
}
