package edu.calpoly.jtkirk.showtrackercp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchForShowActivity extends AppCompatActivity {

    private android.support.v7.view.ActionMode mActionMode;
    private android.support.v7.view.ActionMode.Callback mActionModeCallback;

    private EditText searchShowEditText;
    private Button searchShowButton;
    private ArrayList<Show> showList;
    protected ShowViewAdapter showViewAdapter;
    private ListView showListView;
    private int selected_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Search", "Started the Search Acvitity");
        setContentView(R.layout.activity_search_for_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchShowEditText = new EditText(this);
        searchShowButton = new Button(this);
        showList = new ArrayList<>();
        showListView = new ListView(this);

        searchShowEditText = (EditText) findViewById(R.id.search_show_text);
        searchShowButton = (Button) findViewById(R.id.search_show_button);
        showViewAdapter = new ShowViewAdapter(this, showList);
        showListView = (ListView) findViewById(R.id.listViewGroup);

        initLayout();
        setListeners();

        //GetSeriesBanner banner = new GetSeriesBanner(this);
        //banner.execute();

        //GetApi apiTest = new GetApi();
        //apiTest.execute();
    }

    public void initLayout() {
        //assert showListView != null;
        showListView.setAdapter(showViewAdapter);
        registerForContextMenu(showListView);

        mActionModeCallback = new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.actionmenu, menu);
                return true;
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
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        passShowBack(showList.get(selected_position));
                        mode.finish(); // Action picked, so close the CAB
                    default:
                        return false;
                }
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }
        };

        showListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //positionToRemove = position;
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startSupportActionMode(mActionModeCallback);
                selected_position = position;
                view.setSelected(true);
                return true;
            }
        });
    }

    public void setListeners() {
        //Allows the user to press the button to search for a show.
        searchShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String show = searchShowEditText.getText().toString();
                if (!show.equals("")) {
                    showList.clear();
                    searchShow(show); //Calls API
                    //addShowView(show); //Currently doesn't call API for this
                }
                searchShowEditText.setText("");
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(searchShowEditText.getWindowToken(), 0);
            }
        });

        // Allows the user to press "enter" to search for a show.
        searchShowEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        String show = searchShowEditText.getText().toString();
                        if (!show.equals("")) {
                            showList.clear();
                            searchShow(show);
                        }
                        searchShowEditText.setText("");
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        imm.hideSoftInputFromWindow(searchShowEditText.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Searches for a show.
     * @param show The name of the show to search for.
     * @return The show that was found.
     */
    public Show searchShow(String show) {
        Show searchedShow = null;
        Log.d("searchShow", "Searching for " + show + "...");

        GetShowInformation getShowInformation = new GetShowInformation();

        getShowInformation.execute(show);
        try {
            getShowInformation.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(getShowInformation.getDoc() != null) {
            //Loop through all the series found
            NodeList seriesList = getShowInformation.getDoc().getElementsByTagName("Series");
            for (int temp = 0; temp < seriesList.getLength(); temp++) {
                Node nNode = seriesList.item(temp);

                //The node is the "Series" root node. Traverse this to find information about this show.
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    searchedShow = new Show(0,
                            Integer.parseInt(getElementFromNode(eElement, "id")),
                            getElementFromNode(eElement, "language"),
                            getElementFromNode(eElement, "SeriesName"),
                            getElementFromNode(eElement, "banner"),
                            getElementFromNode(eElement, "Overview"),
                            getElementFromNode(eElement, "FirstAired"),
                            getElementFromNode(eElement, "Network"),
                            getElementFromNode(eElement, "IMDB_ID"),
                            "",
                            0);

                    addShowView(searchedShow);
                }
            }
        }

        return searchedShow;
    }

    /**
     * Add a view for a show to the screen.
     * @param show The show to add
     */
    public void addShowView(Show show) {
        showList.add(show);
        showViewAdapter.notifyDataSetChanged();
    }

    //May not need this method, it might be called from MainActivity instead.
    protected void addShow(Show show) {
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
        //fillData();
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

    /**
     * Saves the show's data and returns it to the MainActivity
     * @param show The show to send back.
     */
    public void passShowBack(Show show) {
        Log.d("passback", "Show passing back: " + show.getName());
        Intent resultIntent = new Intent();

        //Pass back the show's values to MainActivity to put in the database.

        resultIntent.putExtra(ShowTable.SERIES_KEY_ID, show.getId());
        resultIntent.putExtra(ShowTable.SERIES_KEY_TVDB_ID, show.getTvdbID());
        resultIntent.putExtra(ShowTable.SERIES_KEY_LANGUAGE, show.getLanguage());
        resultIntent.putExtra(ShowTable.SERIES_KEY_NAME, show.getName());
        resultIntent.putExtra(ShowTable.SERIES_KEY_BANNER, show.getBanner());
        resultIntent.putExtra(ShowTable.SERIES_KEY_OVERVIEW, show.getOverview());
        resultIntent.putExtra(ShowTable.SERIES_KEY_FIRST_AIRED, show.getFirstAired());
        resultIntent.putExtra(ShowTable.SERIES_KEY_NETWORK, show.getNetwork());
        resultIntent.putExtra(ShowTable.SERIES_KEY_IMDB_ID, show.getImdbID());
        resultIntent.putExtra(ShowTable.SERIES_KEY_STATUS, show.getStatus());
        resultIntent.putExtra(ShowTable.SERIES_KEY_EPISODES_SEEN, show.getEpisodesSeen());

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
