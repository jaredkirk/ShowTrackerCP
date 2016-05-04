package edu.calpoly.jtkirk.showtrackercp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
public class GetShowInformation extends AsyncTask {
    private Exception exception;
    private String show;
    private String information;
    private Document doc;

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    protected Object doInBackground(Object[] params) {
        try {
            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            String show = (String)params[0].toString().replaceAll(" ", "+");
            Log.d("test", "url:" + "http://thetvdb.com/api/GetSeries.php?seriesname=" + show);
            URL url = new URL("http://thetvdb.com/api/GetSeries.php?seriesname=" + show);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                Log.d("test", "got: " + stringBuilder.toString());

                //Creates a document of the XML obtained.
                ByteArrayInputStream input =  new ByteArrayInputStream(
                        stringBuilder.toString().getBytes("UTF-8"));
                doc = builder.parse(input);
                //

                information = stringBuilder.toString();
                return stringBuilder;
            }
            finally{
                urlConnection.disconnect();
                Log.d("test", "Disconnected.");
                return "nope";
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        return "";
    }

    protected void onPostExecute(String response) {
        information = response;
        Log.d("getshow", "response: " + response);
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        //progressBar.setVisibility(View.GONE);
        //Log.d("INFO", response);
        //responseView.setText(response);
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getInformation() {
        return information;
    }

    public Document getDoc() {
        return doc;
    }
}
