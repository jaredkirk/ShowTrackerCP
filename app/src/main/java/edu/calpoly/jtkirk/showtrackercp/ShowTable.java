package edu.calpoly.jtkirk.showtrackercp;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jaredkirk on 4/26/16.
 */
public class ShowTable {

    /** Show table in the database. */
    public static final String DATABASE_TABLE_SHOW = "show_table";

    /** Show table column names and IDs for database access. */
    public static final String SERIES_KEY_ID = "_id";
    public static final int SERIES_COL_ID = 0;

    public static final String SERIES_KEY_TVDB_ID = "series_id";
    public static final int SERIES_COL_TVDB_ID = SERIES_COL_ID + 1;

    public static final String SERIES_KEY_LANGUAGE = "series_language";
    public static final int SERIES_COL_LANGUAGE = SERIES_COL_ID + 2;

    public static final String SERIES_KEY_NAME = "series_name";
    public static final int SERIES_COL_NAME = SERIES_COL_ID + 3;

    public static final String SERIES_KEY_BANNER = "banner";
    public static final int SERIES_COL_BANNER = SERIES_COL_ID + 4;

    public static final String SERIES_KEY_OVERVIEW = "overview";
    public static final int SERIES_COL_OVERVIEW = SERIES_COL_ID + 5;

    public static final String SERIES_KEY_FIRST_AIRED = "first_aired";
    public static final int SERIES_COL_FIRST_AIRED = SERIES_COL_ID + 6;

    public static final String SERIES_KEY_NETWORK = "network";
    public static final int SERIES_COL_NETWORK = SERIES_COL_ID + 7;

    public static final String SERIES_KEY_IMDB_ID = "imdb_id";
    public static final int SERIES_COL_IMDB_ID = SERIES_COL_ID + 8;

    public static final String SERIES_KEY_STATUS = "status"; //Watching, completed, to watch
    public static final int SERIES_COL_STATUS = SERIES_COL_ID + 9;

    public static final String SERIES_KEY_EPISODES_SEEN = "episodes_seen";
    public static final int SERIES_COL_EPISODES_SEEN = SERIES_COL_ID + 10;

    /** SQLite database creation statement. Auto-increments IDs of inserted SHOWs.
     * SHOW IDs are set after insertion into the database. */
    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_SHOW + " (" +
            SERIES_KEY_ID + " integer primary key autoincrement, " +
            SERIES_KEY_TVDB_ID + " integer not null, " +
            SERIES_KEY_LANGUAGE + " text not null, " +
            SERIES_KEY_NAME + " text not null, " +
            SERIES_KEY_BANNER + " text not null, " +
            SERIES_KEY_OVERVIEW + " text not null, " +
            SERIES_KEY_FIRST_AIRED + " text not null, " +
            SERIES_KEY_NETWORK + " text not null, " +
            SERIES_KEY_IMDB_ID + " integer not null, " +
            SERIES_KEY_STATUS + " text not null, " +
            SERIES_KEY_EPISODES_SEEN + " integer not null);";

    /** SQLite database table removal statement. Only used if upgrading database. */
    public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_SHOW;

    /**
     * Initializes the database.
     *
     * @param database
     * 				The database to initialize.
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /**
     * Upgrades the database to a new version.
     *
     * @param database
     * 					The database to upgrade.
     * @param oldVersion
     * 					The old version of the database.
     * @param newVersion
     * 					The new version of the database.
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(ShowTable.class.getName(), "Database is being upgraded from " + oldVersion
                + " to " + newVersion);
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }
}
