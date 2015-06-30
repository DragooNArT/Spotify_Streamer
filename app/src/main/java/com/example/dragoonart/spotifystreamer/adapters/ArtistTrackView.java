package com.example.dragoonart.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dragoonart.spotifystreamer.R;
import com.example.dragoonart.spotifystreamer.beans.ArtistTrack;
import com.squareup.picasso.Picasso;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class ArtistTrackView extends ArrayAdapter<ArtistTrack> {
    private final Context context;
    private final ArtistTrack[] values;

    public ArtistTrackView(Context context, ArtistTrack[] values) {
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
        ArtistTrack track = values[position];
        albumName.setText(track.getAlbumName());
        trackName.setText(track.getTrackName());
        // Change the icon for Windows and iPhone
        if (track.getAlbumCoverThumbnail() != null) {
            Picasso.with(context).load(track.getAlbumCoverThumbnail()).into(albumImg);
        } else {
            Picasso.with(context).load("http://prozrachniplanini.org/img/2/avatar-profile.png").into(albumImg);
        }
        return rowView;
    }
}
