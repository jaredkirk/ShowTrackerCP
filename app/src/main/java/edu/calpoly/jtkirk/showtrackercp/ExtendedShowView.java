package edu.calpoly.jtkirk.showtrackercp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ExtendedShowView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_show_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Show Information");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        /**
         * Set all the text views.
         */
        TextView seriesName = (TextView) findViewById(R.id.extended_series_name);
        seriesName.setText(extras.getString(ShowTable.SERIES_KEY_NAME));

        TextView overview = (TextView) findViewById(R.id.extended_overview);
        overview.setText(extras.getString(ShowTable.SERIES_KEY_OVERVIEW));

        TextView episodesSeen = (TextView) findViewById(R.id.extended_episodes_seen);
        episodesSeen.setText(extras.getInt(ShowTable.SERIES_KEY_EPISODES_SEEN) + "");

        TextView firstAired = (TextView) findViewById(R.id.extended_first_aired);
        firstAired.setText(extras.getString(ShowTable.SERIES_KEY_FIRST_AIRED));

        Log.d("test", extras.getString(ShowTable.SERIES_KEY_FIRST_AIRED) + "");

    }

}
