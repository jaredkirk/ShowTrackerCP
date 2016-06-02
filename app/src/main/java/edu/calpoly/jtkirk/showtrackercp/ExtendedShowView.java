package edu.calpoly.jtkirk.showtrackercp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtendedShowView extends AppCompatActivity {
    TextView seriesName;
    TextView overview;
    TextView episodesSeen;
    TextView firstAired;
    TextView language;
    TextView network;
    String status;
    int showID;
    int movieDBID;
    String banner;
    ImageView image;
    OkHttpDownloader myOKHttpDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_show_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.extended_toolbar);
        toolbar.setTitle("Show Information");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        myOKHttpDownloader = new OkHttpDownloader(this);
        image = (ImageView)findViewById(R.id.image);
        initializeText(intent.getExtras());
        initializeStatusSpinner();
        initializeImage();
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
        movieDBID = extras.getInt(ShowTable.SERIES_KEY_TVDB_ID);
        status = extras.getString(ShowTable.SERIES_KEY_STATUS);
        banner = extras.getString(ShowTable.SERIES_KEY_BANNER);
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
//
//    //save image
//    public static void imageDownload(Context ctx, String url){
//        Picasso.with(ctx)
//                .load(url)
//                .into(getTarget(url));
//    }

    //target to save
    private static Target getTarget(final String url){
        Log.d("image", "Getting target...");
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("image", "Bit map loading...");

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("image", "Running...");

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    /**
     * Currently called from the SearchForShowActivity for now
     */
    public void initializeImage() {
        Log.d("image", "Image path: " + banner);
        if(banner != null) {
            Picasso.with(this).load(banner).into(image);
            Log.d("searchForShow", "finished...");
        }
        else {
            Log.d("show", "Had no image.");
        }
    }

//    //Source: Stack Overflow
//    // Returns the URI path to the Bitmap displayed in specified ImageView
//    public Uri getLocalBitmapUri(ImageView imageView) {
//        // Extract Bitmap from ImageView drawable
//        Drawable drawable = imageView.getDrawable();
//        Bitmap bmp = null;
//        if (drawable instanceof BitmapDrawable){
//            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        } else {
//            return null;
//        }
//        // Store image to default external storage directory
//        Uri bmpUri = null;
//        try {
//            File file =  new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
//            file.getParentFile().mkdirs();
//            FileOutputStream out = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
//            out.close();
//            bmpUri = Uri.fromFile(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bmpUri;
//    }


}
