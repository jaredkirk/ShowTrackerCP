package edu.calpoly.jtkirk.showtrackercp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    String status;
    int showID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_show_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.extended_toolbar);
        toolbar.setTitle("Show Information");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        initializeText(intent.getExtras());
        initializeStatusSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.extended_show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.edit) {
            Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialze the text to display show information.
     * @param extras The bundle containing the show information.
     */
    public void initializeText(Bundle extras) {
        showID = extras.getInt(ShowTable.SERIES_KEY_ID);
        status = extras.getString(ShowTable.SERIES_KEY_STATUS);

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

//        status = (TextView) findViewById(R.id.extended_status);
//        status.setText(extras.getString(ShowTable.SERIES_KEY_STATUS));
    }

    /**
     * Initialize the spinner that displays the status of the show you are watching.
     */
    public void initializeStatusSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.status_spinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Set the initial position of the Spinner
        if(status.toString().equals("Watching")) {
            spinner.setSelection(0);
        }
        else if(status.toString().equals("Completed")) {
            spinner.setSelection(1);
        }
        else if(status.toString().equals("To Watch")) {
            spinner.setSelection(2);
        }

        final Uri uri = Uri.parse(ShowContentProvider.CONTENT_URI + "/series/" + showID);
        final ContentValues contentValues = new ContentValues();

        //Update the show's status when an item is selected.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case ("Watching"):
                        contentValues.put(ShowTable.SERIES_KEY_STATUS, "Watching");
                        getContentResolver().update(uri, contentValues, null, null);
                        break;
                    case ("Completed"):
                        contentValues.put(ShowTable.SERIES_KEY_STATUS, "Completed");
                        getContentResolver().update(uri, contentValues, null, null);
                        break;
                    case ("To Watch"):
                        contentValues.put(ShowTable.SERIES_KEY_STATUS, "To Watch");
                        getContentResolver().update(uri, contentValues, null, null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
