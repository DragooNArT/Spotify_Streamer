package com.example.dragoonart.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dragoonart.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class ArtistTrackView extends ArrayAdapter<Track> {
    private final Context context;
    private final Track[] values;

    public ArtistTrackView(Context context, Track[] values) {
        super(context, R.layout.artist_track_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.artist_track_row, parent, false);
        TextView trackName = (TextView) rowView.findViewById(R.id.track_name);
        TextView albumName = (TextView) rowView.findViewById(R.id.track_album);
        ImageView albumImg = (ImageView) rowView.findViewById(R.id.track_icon);
        Track track = values[position];
        albumName.setText(track.album.name);
        trackName.setText(track.name);
        // Change the icon for Windows and iPhone
        if (!track.album.images.isEmpty()) {
            Picasso.with(context).load(track.album.images.get(0).url).into(albumImg);
        } else {
            Picasso.with(context).load("http://prozrachniplanini.org/img/2/avatar-profile.png").into(albumImg);
        }
        return rowView;
    }
}
