package com.example.dragoonart.spotifystreamer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dragoonart.spotifystreamer.adapters.ArtistRowView;
import com.example.dragoonart.spotifystreamer.helpers.ListViewHelper;
import com.example.dragoonart.spotifystreamer.listeners.ArtistListClickListener;
import com.example.dragoonart.spotifystreamer.tasks.FetchArtists;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        FetchArtists fetchArtistsTask = new FetchArtists(textView.getText().toString(), this);
        fetchArtistsTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void renderList(List<Artist> artists) {

        ListView artistList = (ListView) findViewById(R.id.artist_list);
        if (artists == null || artists.isEmpty()) {
            ListViewHelper.displayEmptyList(this,artistList,"No Artists found");
        } else {
            ArtistRowView rowView = new ArtistRowView(getBaseContext(), artists.toArray(new Artist[artists.size()]));
            artistList.setAdapter(rowView);
            ArtistListClickListener listener = new ArtistListClickListener(this, artists);
            artistList.setOnItemClickListener(listener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
