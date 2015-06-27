package com.example.dragoonart.spotifystreamer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dragoonart.spotifystreamer.adapters.ArtistRowView;
import com.example.dragoonart.spotifystreamer.beans.DiscoveredArtist;
import com.example.dragoonart.spotifystreamer.helpers.ListViewHelper;
import com.example.dragoonart.spotifystreamer.listeners.ArtistListClickListener;
import com.example.dragoonart.spotifystreamer.tasks.FetchArtists;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    public static final String SAVED_DATA_KEY = "SEARCH_DATA";
    public static final String EXTRA_ARTIST_NAME_KEY = "ARTIST_NAME";
    private ArrayList<DiscoveredArtist> artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            ArrayList<DiscoveredArtist> artists = (ArrayList<DiscoveredArtist>) savedInstanceState.getSerializable(SAVED_DATA_KEY);
            renderList(artists);
        }
        // your text box
        EditText edit_txt = (EditText) findViewById(R.id.artist_keyword);
        edit_txt.setOnEditorActionListener(this);

    }

    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            FetchArtists fetchArtistsTask = new FetchArtists(textView.getText().toString(), this);
            fetchArtistsTask.execute();
            InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText textView = (EditText) this.findViewById(R.id.artist_keyword);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (artists != null) {
            outState.putSerializable(SAVED_DATA_KEY, artists);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * fill artist list with items from spotify
     *
     * @param artists
     */
    public void renderList(ArrayList<DiscoveredArtist> artists) {
        this.artists = artists;
        ListView artistList = (ListView) findViewById(R.id.artist_list);
        if (artists == null || artists.isEmpty()) {
            ListViewHelper.displayEmptyList(this, artistList, "No Artists found");
        } else {
            ArtistRowView rowView = new ArtistRowView(getBaseContext(), artists.toArray(new DiscoveredArtist[artists.size()]));
            artistList.setAdapter(rowView);
            ArtistListClickListener listener = new ArtistListClickListener(this, artists);
            artistList.setOnItemClickListener(listener);
            rowView.notifyDataSetChanged();
        }
    }

}
