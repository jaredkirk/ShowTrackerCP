package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Created by jaredkirk on 4/26/16.
 */
public class ShowDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
    /** The name of the database. */
    public static final String DATABASE_NAME = "showtrackerdatabase.db";

    /** The starting database version. */
    public static final int DATABASE_VERSION = 1;

    /**
     * Create a helper object to create, open, and/or manage a database.
     *
     * @param context
     * 					The application context.
     * @param name
     * 					The name of the database.
     * @param factory
     * 					Factory used to create a cursor. Set to null for default behavior.
     * @param version
     * 					The starting database version.
     */
    public ShowDatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        ShowTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ShowTable.onUpgrade(database, oldVersion,  newVersion);
    }
}
