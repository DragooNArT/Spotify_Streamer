package com.example.dragoonart.spotifystreamer.helpers;

import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dragoonart.spotifystreamer.R;

/**
 * Created by xnml on 2015-06-25.
 */
public class ListViewHelper {


    private ListViewHelper() {

    }

    public static void displayEmptyList(AppCompatActivity activity, ListView list,String textToDisplay) {
        String[] text = new String[]{textToDisplay};
        ArrayAdapter adapter = new ArrayAdapter(activity.getBaseContext(), R.layout.notification_media_action,text);
        list.setAdapter(adapter);
    }
}
