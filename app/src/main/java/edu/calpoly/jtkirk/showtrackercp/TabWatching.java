package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jaredkirk on 5/3/16.
 */
public class TabWatching extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private ListView showListView;
    private View view;
    protected ShowViewAdapter showViewAdapter;
    private ArrayList<Show> showList;
    private ShowCursorAdapter showCursorAdapter;
    private int selected_position;

    private ActionMode.Callback mActionModeCallback;
    private ActionMode mActionMode;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
//        ((MainActivity)getActivity()).addShow(new Show(0, 0, "en", "Community", "", "", "", "", "", "Completed", 37));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TabWatching", "TabWatching resumed");
        getLoaderManager().restartLoader(4, null, this);
        showListView.setAdapter(showCursorAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_watching, container, false);
        //Must be called after the view is created to avoid null pointers.
        showListView = (ListView) view.findViewById(R.id.listViewGroupWatching);
        initLayout();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showList = new ArrayList<Show>();
        showViewAdapter = new ShowViewAdapter(getActivity(), showList);
        showCursorAdapter = new ShowCursorAdapter(getContext(), null, 0);

        LoaderManager loaderManagerWatching = getActivity().getSupportLoaderManager();
        loaderManagerWatching.initLoader(4, null, this);

        showListView.setAdapter(showCursorAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("tabwatching", "createloader");

        switch(id) {
            case(4):
                String[] projection2 = {ShowTable.SERIES_KEY_ID, ShowTable.SERIES_KEY_TVDB_ID,
                        ShowTable.SERIES_KEY_LANGUAGE, ShowTable.SERIES_KEY_NAME, ShowTable.SERIES_KEY_BANNER,
                        ShowTable.SERIES_KEY_OVERVIEW, ShowTable.SERIES_KEY_FIRST_AIRED, ShowTable.SERIES_KEY_NETWORK,
                        ShowTable.SERIES_KEY_IMDB_ID, ShowTable.SERIES_KEY_STATUS, ShowTable.SERIES_KEY_EPISODES_SEEN};

                Uri uri2 = Uri.parse(ShowContentProvider.CONTENT_URI + "/filter/" + 4);

                return new android.support.v4.content.CursorLoader(getActivity(), uri2, projection2, null, null, null);
        }
        Log.d("TabWatching", "Invalid ID passed in: " + id);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("tabwatching", "swapcursor");
        showCursorAdapter.swapCursor(data);
        //showCursorAdapter.setOnShowChangeListener((MainActivity) getActivity());
        //showCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(isAdded()) {
            showCursorAdapter.swapCursor(null);
            getLoaderManager().restartLoader(4, null, this);
            showListView.setAdapter(showCursorAdapter);
        }
    }

    public void initLayout() {
        showListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).setSelectedPosition(position);
                //TODO Bug, cannot long click most recently added show.
                ((MainActivity)getActivity()).setSelectedShow(((ShowView) showListView.getChildAt(position)).getShow());
                ((MainActivity)getActivity()).setSelectedShowView(((ShowView) showListView.getChildAt(position)));

                // Start the CAB using the ActionMode.Callback defined above
                ((MainActivity)getActivity()).startSupportActionMode(((MainActivity) getActivity()).getMActionModeCallback());
                return true;
            }
        });
    }
}
