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

import org.w3c.dom.Text;

public class ExtendedShowView extends AppCompatActivity {
    TextView seriesName;
    TextView overview;
    TextView episodesSeen;
    TextView firstAired;
    TextView language;
    TextView network;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_show_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Show Information");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        initializeText(intent.getExtras());
    }

    /**
     * Initialze the text to display show information.
     * @param extras The bundle containing the show information.
     */
    public void initializeText(Bundle extras) {
        seriesName = (TextView) findViewById(R.id.extended_series_name);

        String name = extras.getString(ShowTable.SERIES_KEY_NAME);
        if(!name.equals("")) {
            seriesName.setText(name);
        }

        String showOverview = extras.getString(ShowTable.SERIES_KEY_OVERVIEW);
        overview = (TextView) findViewById(R.id.extended_overview);
        if(!showOverview.equals("") && overview != null) {
            overview.setText(showOverview);
        }

        episodesSeen = (TextView) findViewById(R.id.extended_episodes_seen);
        episodesSeen.setText(extras.getInt(ShowTable.SERIES_KEY_EPISODES_SEEN) + "");

        String showFirstAired = extras.getString(ShowTable.SERIES_KEY_FIRST_AIRED);
        firstAired = (TextView) findViewById(R.id.extended_first_aired);
        if(!showFirstAired.equals("") && firstAired != null) {
            firstAired.setText(showFirstAired);
        }

        String showLanguage = extras.getString(ShowTable.SERIES_KEY_LANGUAGE);
        language = (TextView) findViewById(R.id.extended_language);
        if(!showLanguage.equals("") && language != null) {
            language.setText(showLanguage);
        }

        String showNetwork = extras.getString(ShowTable.SERIES_KEY_NETWORK);
        network = (TextView) findViewById(R.id.extended_network);
        if(!showNetwork.equals("") && network != null) {
            network.setText(showNetwork);
        }

        status = (TextView) findViewById(R.id.extended_status);
        status.setText(extras.getString(ShowTable.SERIES_KEY_STATUS));
    }

}
