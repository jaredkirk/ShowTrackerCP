package edu.calpoly.jtkirk.showtrackercp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.methods.TmdbTV;
import com.omertron.themoviedbapi.model.artwork.Artwork;
import com.omertron.themoviedbapi.results.ResultList;
import com.omertron.themoviedbapi.tools.HttpTools;

import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jaredkirk on 4/17/16.
 */
public class GetArtwork extends AsyncTask {
    private String imagePath;

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected Object doInBackground(Object[] params) {
        int id = (int)params[0];
        try {
            HttpTools http = new HttpTools(new DefaultHttpClient());
            TmdbTV tmdbTV = new TmdbTV("dd3119f497ca9130415e36085a740fab", http);

            try {
                ResultList<Artwork> art = tmdbTV.getTVImages(id, "en", "en");
                ArrayList<Artwork> artwork = (ArrayList)art.getResults();

                Log.d("test", "Artwork: " + artwork.get(0).getFilePath());
                imagePath = artwork.get(0).getFilePath();
            } catch (MovieDbException e) {
                Log.d("uhoh", "movie exception");
                e.printStackTrace();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        return "";
    }

    protected void onPostExecute(String response) {

    }

    public String getImagePath() {
        return "https://image.tmdb.org/t/p/original" +imagePath;
    }
}
