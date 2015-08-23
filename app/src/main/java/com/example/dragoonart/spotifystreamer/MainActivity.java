package com.example.dragoonart.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DragooNART on 8/23/2015.
 */
public class MainActivity extends AppCompatActivity {

    private MainActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new MainActivityFragment();
        if (findViewById(R.id.tablet_masterpane) != null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.tablet_masterpane, fragment)
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .add(R.id.main_activity_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fragment != null && fragment.getArtists() != null) {
            outState.putSerializable(MainActivityFragment.SAVED_DATA_KEY, fragment.getArtists());
        }
        super.onSaveInstanceState(outState);
    }
}
