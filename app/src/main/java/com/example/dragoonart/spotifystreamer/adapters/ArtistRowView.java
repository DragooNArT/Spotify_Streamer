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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by DragooNART on 6/25/2015.
 */
public class ArtistRowView extends ArrayAdapter<Artist> {
    private final Context context;
    private final Artist[] values;

    public ArtistRowView(Context context, Artist[] values) {
        super(context, R.layout.artist_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.artist_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.artist_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.artist_icon);
        Artist artist = values[position];
        textView.setText(artist.name);
        // Change the icon for Windows and iPhone
        if (!artist.images.isEmpty()) {
            Picasso.with(context).load(artist.images.get(0).url).into(imageView);
        } else {
            Picasso.with(context).load("http://prozrachniplanini.org/img/2/avatar-profile.png").into(imageView);
        }
        return rowView;
    }
}
