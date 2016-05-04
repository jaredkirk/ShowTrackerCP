package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jaredkirk on 4/17/16.
 */
public class GetSeriesBanner extends AsyncTask {
    private Exception exception;
    private String information;
    private Context context;

    public GetSeriesBanner(Context context) {
        this.context = context;
    }

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
            Log.d("test", "url:" + "http://thetvdb.com/api/9EC195CB619149ED/series/268943/all/en.zip");
            URL url = new URL("http://thetvdb.com/api/9EC195CB619149ED/series/268943/all/en.zip");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inStream = urlConnection.getInputStream();
            FileOutputStream outputStream;
            try {
                outputStream = context.openFileOutput("download.zip", Context.MODE_PRIVATE);
                copy(inStream, outputStream, 1024);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
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

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int n = input.read(buf);
        while (n >= 0) {
            output.write(buf, 0, n);
            n = input.read(buf);
        }
        output.flush();
    }

    protected void onPostExecute(String response) {
        information = response;
        //Log.d("getBanner", "response: " + response);
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        //progressBar.setVisibility(View.GONE);
        //Log.d("INFO", response);
        //responseView.setText(response);
    }

    public String getInformation() {
        return information;
    }
}
