package edu.calpoly.jtkirk.showtrackercp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jaredkirk on 4/17/16.
 */
public class GetApi extends AsyncTask {
    private Exception exception;
    private ArrayList<String> xmlmirrors;
    private ArrayList<String> bannermirrors;
    private ArrayList<String> zipmirrors;

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected Object doInBackground(Object[] params) {
        try {
            Log.d("test", "Trying to connect...");
            URL url = new URL("http://thetvdb.com/api/9EC195CB619149ED/mirrors.xml");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                Log.d("test", stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
                Log.d("test", "Disconnected.");
                return "";
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        try {
            Log.d("time", "Getting server time...");
            URL url = new URL("http://thetvdb.com/api/9EC195CB619149ED/mirrors.xml");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                Log.d("time", stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
                Log.d("time", "Disconnected.");
                return "";
            }
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
        //progressBar.setVisibility(View.GONE);
        //Log.d("INFO", response);
        //responseView.setText(response);
    }
}
