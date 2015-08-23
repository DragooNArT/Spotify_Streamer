package com.example.dragoonart.spotifystreamer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MainActivityFragment extends Fragment implements TextView.OnEditorActionListener {

    public static final String SAVED_DATA_KEY = "SEARCH_DATA";
    public static final String EXTRA_ARTIST_NAME_KEY = "ARTIST_NAME";
    private ArrayList<DiscoveredArtist> artists;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_main_fragment, container, false);
        if (savedInstanceState != null) {
            ArrayList<DiscoveredArtist> artists = (ArrayList<DiscoveredArtist>) savedInstanceState.getSerializable(SAVED_DATA_KEY);
            renderList(artists);
        }
        // your text box
        EditText edit_txt = (EditText) view.findViewById(R.id.artist_keyword);
        edit_txt.setOnEditorActionListener(this);
        return view;
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

    public View findViewById(int id) {
        return view.findViewById(id);
    }


    /**
     * fill artist list with items from spotify
     *
     * @param artists
     */
    public void renderList(ArrayList<DiscoveredArtist> artists) {
        this.artists = artists;
        ListView artistList = (ListView) findViewById(R.id.artist_list);
        EditText inputBox = (EditText) findViewById(R.id.artist_keyword);
        if (artists == null || artists.isEmpty()) {
            if (inputBox.getText().length() < 3) {
                ListViewHelper.displayEmptyList(artistList, "Please enter more than 3 characters");
            } else {
                ListViewHelper.displayEmptyList(artistList, "No Artists found, please refine your search");
            }
        } else {
            ArtistRowView rowView = new ArtistRowView(getActivity(), artists.toArray(new DiscoveredArtist[artists.size()]));
            artistList.setAdapter(rowView);
            ArtistListClickListener listener = new ArtistListClickListener(this, artists);
            artistList.setOnItemClickListener(listener);
            rowView.notifyDataSetChanged();
        }
    }

    public ArrayList<DiscoveredArtist> getArtists() {
        return artists;
    }
}
