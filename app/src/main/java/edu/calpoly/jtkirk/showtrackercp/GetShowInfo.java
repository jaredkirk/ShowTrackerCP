package edu.calpoly.jtkirk.showtrackercp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.omertron.themoviedbapi.enumeration.ExternalSource;
import com.omertron.themoviedbapi.methods.TmdbFind;
import com.omertron.themoviedbapi.methods.TmdbSearch;
import com.omertron.themoviedbapi.methods.TmdbTV;
import com.omertron.themoviedbapi.model.keyword.Keyword;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVInfo;
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
public class GetShowInfo extends AsyncTask {
    private Exception exception;
    private String show;
    private String information;
    private Document doc;
    private ArrayList<TVInfo> tvInfos;

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected Object doInBackground(Object[] params) {
        try {
            HttpTools http = new HttpTools(new DefaultHttpClient());
            Log.d("search", "Search item: " + params[0].toString());
            TmdbSearch search = new TmdbSearch("dd3119f497ca9130415e36085a740fab", http);
            ResultList<TVBasic> result = search.searchTV(params[0].toString(), null, null, null, null);

            ArrayList<TVBasic> results = (ArrayList)result.getResults();
            String append[] = null;
            TmdbTV findTV = new TmdbTV("dd3119f497ca9130415e36085a740fab", http);
            tvInfos = new ArrayList<>();
            for(TVBasic key : results) {
                Log.d("search", "Found " + key.getName());
                int id = key.getId();

                tvInfos.add(findTV.getTVInfo(id, "en", append));
            }
            return tvInfos;
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        return "";
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
    }

    public ArrayList<TVInfo> getTvInfos() {
        return tvInfos;
    }
}
