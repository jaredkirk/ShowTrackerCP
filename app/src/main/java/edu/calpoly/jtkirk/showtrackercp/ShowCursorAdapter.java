package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jaredkirk on 4/26/16.
 */
public class ShowCursorAdapter extends android.support.v4.widget.CursorAdapter {

    /** The OnShowChangeListener that should be connected to each of the
     * ShowViews created/managed by this Adapter. */
    private ShowView.OnShowChangeListener showListener;

    public ShowCursorAdapter(Context context, Cursor showCursor, int flags) {
        super(context, showCursor, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Extract the Show from the cursor.
        int id = cursor.getInt(ShowTable.SERIES_COL_ID);
        int tvdbID = cursor.getInt(ShowTable.SERIES_COL_TVDB_ID);
        String language = cursor.getString(ShowTable.SERIES_COL_LANGUAGE);
        String name = cursor.getString(ShowTable.SERIES_COL_NAME);
        String banner = cursor.getString(ShowTable.SERIES_COL_BANNER);
        String overview = cursor.getString(ShowTable.SERIES_COL_OVERVIEW);
        String firstAired = cursor.getString(ShowTable.SERIES_COL_FIRST_AIRED);
        String network = cursor.getString(ShowTable.SERIES_COL_NETWORK);
        String imdbID = cursor.getString(ShowTable.SERIES_COL_IMDB_ID);
        String status = cursor.getString(ShowTable.SERIES_COL_STATUS);
        int episodesSeen = cursor.getInt(ShowTable.SERIES_COL_EPISODES_SEEN);

        Show show = new Show(id, tvdbID, language, name, banner, overview, firstAired,
                network, imdbID, status, episodesSeen);

        ShowView view = new ShowView(context, show);
        view.setOnShowChangeListener(showListener);

        return view;
    }

    //TODO bindView is getting old views from when the app started, every time something is updated.
    //Could be not clearing a list of data from somewhere.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        if(view != null && ((ShowView)view).getShow() != null) {
//            Log.d("bindview", "bind view: " + ((ShowView) view).getShow().getEpisodesSeen());
//            Log.d("bindview", "show: " + ((ShowView) view).getShow().getName());
//        }
        //Extract the Show from the cursor.
        int id = cursor.getInt(ShowTable.SERIES_COL_ID);
        int tvdbID = cursor.getInt(ShowTable.SERIES_COL_TVDB_ID);
        String language = cursor.getString(ShowTable.SERIES_COL_LANGUAGE);
        String name = cursor.getString(ShowTable.SERIES_COL_NAME);
        String banner = cursor.getString(ShowTable.SERIES_COL_BANNER);
        String overview = cursor.getString(ShowTable.SERIES_COL_OVERVIEW);
        String firstAired = cursor.getString(ShowTable.SERIES_COL_FIRST_AIRED);
        String network = cursor.getString(ShowTable.SERIES_COL_NETWORK);
        String imdbID = cursor.getString(ShowTable.SERIES_COL_IMDB_ID);
        String status = cursor.getString(ShowTable.SERIES_COL_STATUS);
        int episodesSeen = cursor.getInt(ShowTable.SERIES_COL_EPISODES_SEEN);

        Show show = new Show(id, tvdbID, language, name, banner, overview, firstAired,
                network, imdbID, status, episodesSeen);
        ((ShowView)view).setOnShowChangeListener(null);
        ((ShowView)view).setShow(show);
        ((ShowView)view).setOnShowChangeListener(showListener);
    }

    /**
     * Mutator method for changing the OnShowChangeListener.
     */
    public void setOnShowChangeListener(ShowView.OnShowChangeListener showListener) {
        this.showListener = showListener;
    }
}
