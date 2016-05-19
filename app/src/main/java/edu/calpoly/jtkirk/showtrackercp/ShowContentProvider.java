package edu.calpoly.jtkirk.showtrackercp;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
/**
 * Created by jaredkirk on 4/26/16.
 */

/**
 * Class that provides content from a SQLite database to the application.
 *
 * Note that CursorLoaders require a ContentProvider, which is why this application wraps a
 * SQLite database into a content provider instead of managing the database<-->application
 * transactions manually.
 */
public class ShowContentProvider extends ContentProvider {

    /** The show database. */
    private ShowDatabaseHelper database;

    /** Values for the URIMatcher. */
    private static final int SHOW_ID = 1;
    private static final int SHOW_FILTER = 2;
    public static final int SHOW_COMPLETED = 3;
    public static final int SHOW_WATCHING = 4;
    public static final int SHOW_TO_WATCH = 5;

    private static final String COMPLETED = "Completed";
    private static final String WATCHING = "Watching";
    private static final String TO_WATCH = "To Watch";

    /** The authority for this content provider. */
    private static final String AUTHORITY = "edu.calpoly.jtkirk.showtrackercp.contentprovider";

    /** The database table to read from and write to, and also the root path for use in the URI matcher. */
    private static final String BASE_PATH = "show_table";

    /** This provider's content location. Used by accessing applications to
     * interact with this provider. */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    /** Matches content URIs requested by accessing applications with possible
     * expected content URI formats to take specific actions in this provider. */
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/series/#", SHOW_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/filter/#", SHOW_FILTER);
    }

    @Override
    public boolean onCreate() {
        database = new ShowDatabaseHelper(getContext(), ShowDatabaseHelper.DATABASE_NAME, null,
                ShowDatabaseHelper.DATABASE_VERSION);
        return false;
    }

    /**
     * Fetches rows from the show table. Given a specified URI that contains a
     * filter, returns a list of shows from the joke table matching that filter in the
     * form of a Cursor.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        /** Use a helper class to perform a query for us. */
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        /** Make sure the projection is proper before querying. */
        checkColumns(projection);
        /** Set up helper to query our shows table. */
        queryBuilder.setTables(ShowTable.DATABASE_TABLE_SHOW);
        /** Match the passed-in URI to an expected URI format. */
        int uriType = sURIMatcher.match(uri);
        switch(uriType) {
            case SHOW_FILTER:
                /** Fetch the last segment of the URI, which should be a filter number. */
                String filter = uri.getLastPathSegment();
                /** Leave selection as null to fetch all rows if filter is Show All. Otherwise,
                 * fetch rows with a specific rating according to the parsed filter. */
                if(Integer.parseInt(filter) == SHOW_COMPLETED) {
                    queryBuilder.appendWhere(ShowTable.SERIES_KEY_STATUS + "=" + "'" + COMPLETED + "'");
                }
                else if(Integer.parseInt(filter) == SHOW_WATCHING) {
                    queryBuilder.appendWhere(ShowTable.SERIES_KEY_STATUS + "=" + "'" + WATCHING + "'");
                }
                else if(Integer.parseInt(filter) == SHOW_TO_WATCH) {
                    queryBuilder.appendWhere(ShowTable.SERIES_KEY_STATUS + "=" + "'" + TO_WATCH + "'");
                }
                else {
                    selection = null;
                }
                break;
            default:
                break;
                //throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        /** Perform the database query. */
        SQLiteDatabase db = this.database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, null, null, null, null);
        /** Set the cursor to automatically alert listeners for content/view refreshing. */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /** We don't really care about this method for this application. */
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Inserts a show into the show table. Given a specific URI that contains a
     * show it writes a new row in the table filled with that show's information
     * then returns a URI containing the ID of the inserted joke.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        /** Open the database for writing. */
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();

        /** Will contain the ID of the inserted show. */
        long id = 0;

        /** Match the passed-in URI to an expected URI format. */
        int uriType = sURIMatcher.match(uri);

        switch(uriType)	{
            case SHOW_ID:
                /** Perform the database insert, placing the show at the bottom of the table. */
                id = sqlDB.insert(ShowTable.DATABASE_TABLE_SHOW, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        /** Alert any watchers of an underlying data change for content/view refreshing. */
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    /**
     * Removes a show from the show table. Given a specific URI containing a show ID,
     * removes rows in the table that match the ID and returns the number of rows removed.
     * Since IDs are automatically incremented on insertion, this will only ever remove
     * a single row from the joke table.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();
        int numRowsDeleted = 0;

        int uriType = sURIMatcher.match(uri);

        switch(uriType)	{
            case SHOW_ID:
                String idToDelete = uri.getLastPathSegment();

                String whereClause = ShowTable.SERIES_KEY_ID + "=" + idToDelete;

                numRowsDeleted += sqlDB.delete(ShowTable.DATABASE_TABLE_SHOW, whereClause, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(numRowsDeleted > 0 ) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    /**
     * Updates a row in the show table. Given a specific URI containing a show ID and the
     * new show values, updates the values in the row with the matching ID in the table.
     * Since IDs are automatically incremented on insertion, this will only ever update
     * a single row in the joke table.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();
        int numRowsUpdated = 0;

        int uriType = sURIMatcher.match(uri);

        switch(uriType)	{
            case SHOW_ID:
                String idToUpdate = uri.getLastPathSegment();

                String whereClause = ShowTable.SERIES_KEY_ID + "=" + idToUpdate;
                numRowsUpdated += sqlDB.update(ShowTable.DATABASE_TABLE_SHOW, values, whereClause, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(numRowsUpdated > 0 ) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsUpdated;
    }

    /**
     * Verifies the correct set of columns to return data from when performing a query.
     *
     * @param projection
     * 						The set of columns about to be queried.
     */
    private void checkColumns(String[] projection) {
        String[] available = {ShowTable.SERIES_KEY_ID, ShowTable.SERIES_KEY_TVDB_ID,
                ShowTable.SERIES_KEY_LANGUAGE, ShowTable.SERIES_KEY_NAME, ShowTable.SERIES_KEY_BANNER,
                ShowTable.SERIES_KEY_OVERVIEW, ShowTable.SERIES_KEY_FIRST_AIRED, ShowTable.SERIES_KEY_NETWORK,
                ShowTable.SERIES_KEY_IMDB_ID, ShowTable.SERIES_KEY_STATUS, ShowTable.SERIES_KEY_EPISODES_SEEN};

        if(projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if(!availableColumns.containsAll(requestedColumns))	{
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
