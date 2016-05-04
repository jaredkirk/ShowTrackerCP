package edu.calpoly.jtkirk.showtrackercp;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jaredkirk on 4/19/16.
 */
public class MyDataBase_OLD {
    private SQLiteDatabase database;

    public MyDataBase_OLD(SQLiteDatabase db) {
        database = db;
        Log.d("database", "Creating database...");
        database.execSQL("CREATE TABLE IF NOT EXISTS series(series_id INTEGER, series_language VARCHAR, " +
                "series_name VARCHAR, banner VARCHAR, overview VARCHAR, first_aired VARCHAR, network VARCHAR, " +
                "imdb_id INTEGER, id INTEGER);");

        database.execSQL("CREATE TABLE IF NOT EXISTS recent_search(recent_search VARCHAR);");

//        Log.d("database", "inserting into database...");
//        database.execSQL("INSERT INTO series VALUES(121361, 'en', 'Game of Thrones', 'graphical/121361-g19.jpg', 'Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Nights Watch, is all that stands between the realms of men and the icy horrors beyond.'," +
//                "'2011-04-17','HBO', 'tt0944947', 121361);");

    }

    public void insertSeries(SQLiteDatabase db, int seriesId, String series_language, String series_name,
                             String banner, String overview, String first_aired, String network, int imdb_id,
                             int id) {
        //TODO Remove quotes in overview or series name.
        db.execSQL("INSERT INTO series VALUES(" + seriesId + ", '" + series_language
                + "', '"+ series_name + "', '" + banner + "', '" + overview + "', '"
                + first_aired + "', '" + network + "', '" + imdb_id + "', "+ id + ");");
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
