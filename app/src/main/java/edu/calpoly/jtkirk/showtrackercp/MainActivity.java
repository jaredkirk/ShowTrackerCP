package edu.calpoly.jtkirk.showtrackercp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        ShowView.OnShowChangeListener {
    private TextView listView;
    private String mirrorPath = "http://thetvdb.com";
    protected ShowViewAdapter showViewAdapter;
    private ArrayList<Show> showList;
    private android.support.v7.view.ActionMode mActionMode;
    private android.support.v7.view.ActionMode.Callback mActionModeCallback;

    private ShowCursorAdapter showCursorAdapter;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    //For the database queries.
    private static final int COMPLETED_INT = 3;
    private static final int WATCHING_INT = 4;
    private static final int TO_WATCH_INT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Watching"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.addTab(tabLayout.newTab().setText("To Watch"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        listView = new TextView(this);
        showList = new ArrayList<Show>();
        showViewAdapter = new ShowViewAdapter(this, showList);

        listView = (TextView) findViewById(R.id.listView);

        showCursorAdapter = new ShowCursorAdapter(this, null, 0);

        initLayout();
        setListeners();
        //addShow(new Show(0, 0, "en", "Game of Thrones", "", "", "", "", "", "Watching", 10));
    }

    public void initLayout() {
        mActionModeCallback = new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
//                Log.d("menu", "menu inflater");
//                MenuInflater inflater = mode.getMenuInflater();
//                inflater.inflate(R.menu.actionmenu, menu);
//                return true;
                return false;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_remove:
//                        for(int i = 0; i < m_arrJokeList.size(); i++) {
//                            if(m_arrFilteredJokeList.get(positionToRemove).getJoke().equals(m_arrJokeList.get(i).getJoke())) {
//                                m_arrJokeList.remove(i);
//                            }
//                        }
//                        m_arrFilteredJokeList.remove(positionToRemove);
//                        m_jokeAdapter.notifyDataSetChanged();
//                        mode.finish(); // Action picked, so close the CAB
//                        return true;
//                    default:
//                        return false;
//                }
                return false;
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }
        };

        //Setup for a long press.
        CharSequence text = "Long Press!";
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(this, text, duration);
    }

    public void setListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        // This will create a new activity to search for shows.
        if (id == R.id.action_search) {
            Intent myIntent = new Intent(this, SearchForShowActivity.class);
            startActivityForResult(myIntent, 1);
            //MainActivity.this.startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Return the string from the API call that corresponds to the element given.
     */
    public String getElementFromNode(Element element, String elementName) {
        if(element.getElementsByTagName(elementName).item(0) != null) {
            return element.getElementsByTagName(elementName).item(0).getTextContent();
        }
        return "";
    }

    public void addShowView(String showName) {
        Show show = new Show(0, 0, "", showName, "", "", "", "", "", "", 0);
        showList.add(show);
        showViewAdapter.notifyDataSetChanged();
    }

    /**
     * Add a show to the database.
     * @param show Show to add.
     */
    protected void addShow(Show show) {
        Log.d("show", "Adding show");
        Uri uri = Uri.parse(ShowContentProvider.CONTENT_URI + "/series/" + show.getId());
        ContentValues contentValues = new ContentValues();

        contentValues.put(ShowTable.SERIES_KEY_TVDB_ID, show.getTvdbID());
        contentValues.put(ShowTable.SERIES_KEY_LANGUAGE, show.getLanguage());
        contentValues.put(ShowTable.SERIES_KEY_NAME, show.getName());
        contentValues.put(ShowTable.SERIES_KEY_BANNER, show.getBanner());
        contentValues.put(ShowTable.SERIES_KEY_OVERVIEW, show.getOverview());
        contentValues.put(ShowTable.SERIES_KEY_FIRST_AIRED, show.getFirstAired());
        contentValues.put(ShowTable.SERIES_KEY_NETWORK, show.getNetwork());
        contentValues.put(ShowTable.SERIES_KEY_IMDB_ID, show.getImdbID());
        contentValues.put(ShowTable.SERIES_KEY_STATUS, show.getStatus());
        contentValues.put(ShowTable.SERIES_KEY_EPISODES_SEEN, show.getEpisodesSeen());

        Uri uriReturn = getContentResolver().insert(uri, contentValues);
        int id = Integer.parseInt(uriReturn.getLastPathSegment());
        show.setId(id);
        fillData();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case(1):
                String[] projection = {ShowTable.SERIES_KEY_ID, ShowTable.SERIES_KEY_TVDB_ID,
                        ShowTable.SERIES_KEY_LANGUAGE, ShowTable.SERIES_KEY_NAME, ShowTable.SERIES_KEY_BANNER,
                        ShowTable.SERIES_KEY_OVERVIEW, ShowTable.SERIES_KEY_FIRST_AIRED, ShowTable.SERIES_KEY_NETWORK,
                        ShowTable.SERIES_KEY_IMDB_ID, ShowTable.SERIES_KEY_STATUS, ShowTable.SERIES_KEY_EPISODES_SEEN};

                Uri uri = Uri.parse(ShowContentProvider.CONTENT_URI + "/series/");

                return new android.support.v4.content.CursorLoader(this, uri, projection, null, null, null);
            case(COMPLETED_INT):
                String[] projection2 = {ShowTable.SERIES_KEY_ID, ShowTable.SERIES_KEY_TVDB_ID,
                        ShowTable.SERIES_KEY_LANGUAGE, ShowTable.SERIES_KEY_NAME, ShowTable.SERIES_KEY_BANNER,
                        ShowTable.SERIES_KEY_OVERVIEW, ShowTable.SERIES_KEY_FIRST_AIRED, ShowTable.SERIES_KEY_NETWORK,
                        ShowTable.SERIES_KEY_IMDB_ID, ShowTable.SERIES_KEY_STATUS, ShowTable.SERIES_KEY_EPISODES_SEEN};

                Uri uri2 = Uri.parse(ShowContentProvider.CONTENT_URI + "/filter/" + R.string.completed);

                return new android.support.v4.content.CursorLoader(this, uri2, projection2, null, null, null);
            case(WATCHING_INT):
                String[] projection3 = {ShowTable.SERIES_KEY_ID, ShowTable.SERIES_KEY_TVDB_ID,
                        ShowTable.SERIES_KEY_LANGUAGE, ShowTable.SERIES_KEY_NAME, ShowTable.SERIES_KEY_BANNER,
                        ShowTable.SERIES_KEY_OVERVIEW, ShowTable.SERIES_KEY_FIRST_AIRED, ShowTable.SERIES_KEY_NETWORK,
                        ShowTable.SERIES_KEY_IMDB_ID, ShowTable.SERIES_KEY_STATUS, ShowTable.SERIES_KEY_EPISODES_SEEN};

                Uri uri3 = Uri.parse(ShowContentProvider.CONTENT_URI + "/filter/" + R.string.watching);

                return new android.support.v4.content.CursorLoader(this, uri3, projection3, null, null, null);
            case(TO_WATCH_INT):
                String[] projection4 = {ShowTable.SERIES_KEY_ID, ShowTable.SERIES_KEY_TVDB_ID,
                        ShowTable.SERIES_KEY_LANGUAGE, ShowTable.SERIES_KEY_NAME, ShowTable.SERIES_KEY_BANNER,
                        ShowTable.SERIES_KEY_OVERVIEW, ShowTable.SERIES_KEY_FIRST_AIRED, ShowTable.SERIES_KEY_NETWORK,
                        ShowTable.SERIES_KEY_IMDB_ID, ShowTable.SERIES_KEY_STATUS, ShowTable.SERIES_KEY_EPISODES_SEEN};

                Uri uri4 = Uri.parse(ShowContentProvider.CONTENT_URI + "/filter/" + R.string.to_watch);

                return new android.support.v4.content.CursorLoader(this, uri4, projection4, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showCursorAdapter.swapCursor(data);
        showCursorAdapter.setOnJokeChangeListener(this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        showCursorAdapter.swapCursor(null);
    }

    @Override
    public void onShowChanged(ShowView view, Show show) {
        Uri uri = Uri.parse(ShowContentProvider.CONTENT_URI + "/series/" + show.getId());
        ContentValues contentValues = new ContentValues();


        contentValues.put(ShowTable.SERIES_KEY_TVDB_ID, show.getTvdbID());
        contentValues.put(ShowTable.SERIES_KEY_LANGUAGE, show.getLanguage());
        contentValues.put(ShowTable.SERIES_KEY_NAME, show.getName());
        contentValues.put(ShowTable.SERIES_KEY_BANNER, show.getBanner());
        contentValues.put(ShowTable.SERIES_KEY_OVERVIEW, show.getOverview());
        contentValues.put(ShowTable.SERIES_KEY_FIRST_AIRED, show.getFirstAired());
        contentValues.put(ShowTable.SERIES_KEY_NETWORK, show.getNetwork());
        contentValues.put(ShowTable.SERIES_KEY_IMDB_ID, show.getImdbID());
        contentValues.put(ShowTable.SERIES_KEY_STATUS, show.getStatus());
        contentValues.put(ShowTable.SERIES_KEY_EPISODES_SEEN, show.getEpisodesSeen());

        getContentResolver().update(uri, contentValues, null, null);

        showCursorAdapter.setOnJokeChangeListener(null);

        //fillData();
    }

    public void fillData() {
        Log.d("fillData", "current item: " + viewPager.getCurrentItem());
        pagerAdapter.notifyDataSetChanged();

//        if(viewPager.getCurrentItem() == 0) {
//            pagerAdapter.notifyDataSetChanged();
//            //getSupportLoaderManager().restartLoader(0, null, (TabWatching)pagerAdapter.getItem(viewPager.getCurrentItem()));
//        }
//        else if(viewPager.getCurrentItem() == 1) {
//            getSupportLoaderManager().restartLoader(0, null, (TabCompleted) pagerAdapter.getItem(viewPager.getCurrentItem()));
//        }
//        //showListView.setAdapter(showCursorAdapter);
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.submenu_like:
//                m_arrFilteredJokeList.clear();
//                for(Joke j : m_arrJokeList) {
//                    if(j.getRating() == Joke.LIKE) {
//                        m_arrFilteredJokeList.add(j);
//                    }
//                }
//                m_jokeAdapter.notifyDataSetChanged();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    public ShowCursorAdapter getShowCursorAdapter() {
        return showCursorAdapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    //String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    // TODO Update your TextView.
                    Log.d("test", "Returned from activity" + data.getStringExtra(ShowTable.SERIES_KEY_NAME));
                    Bundle extras = data.getExtras();
                    Show show = new Show(
                            extras.getInt(ShowTable.SERIES_KEY_ID),
                            extras.getInt(ShowTable.SERIES_KEY_TVDB_ID),
                            extras.getString(ShowTable.SERIES_KEY_LANGUAGE),
                            extras.getString(ShowTable.SERIES_KEY_NAME),
                            extras.getString(ShowTable.SERIES_KEY_BANNER),
                            extras.getString(ShowTable.SERIES_KEY_OVERVIEW),
                            extras.getString(ShowTable.SERIES_KEY_FIRST_AIRED),
                            extras.getString(ShowTable.SERIES_KEY_NETWORK),
                            extras.getString(ShowTable.SERIES_KEY_IMDB_ID),
                            extras.getString(ShowTable.SERIES_KEY_STATUS),
                            extras.getInt(ShowTable.SERIES_KEY_EPISODES_SEEN));
                    Log.d("code:", "Going to add show...");
                    addShow(show);
                }
                break;
            }
        }
    }
}
