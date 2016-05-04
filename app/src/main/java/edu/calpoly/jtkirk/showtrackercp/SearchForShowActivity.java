//package edu.calpoly.jtkirk.showtrackercp;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.view.ActionMode;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//
//public class SearchForShowActivity extends AppCompatActivity {
//
//    private android.support.v7.view.ActionMode mActionMode;
//    private android.support.v7.view.ActionMode.Callback mActionModeCallback;
//    private EditText searchShowEditText;
//    private Button searchShowButton;
//    private ArrayList<Show> showList;
//    protected ShowViewAdapter showViewAdapter;
//    private ListView showListView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_for_show);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        Intent intent = getIntent();
//        String value = intent.getStringExtra("searchValue");
//        searchShowEditText = new EditText(this);
//        searchShowButton = new Button(this);
//        showList = new ArrayList<>();
//        showListView = new ListView(this);
//
//        searchShowEditText = (EditText) findViewById(R.id.search_show_text);
//        searchShowButton = (Button) findViewById(R.id.search_show_button);
//        showViewAdapter = new ShowViewAdapter(this, showList);
//        showListView = (ListView) findViewById(R.id.listViewGroup);
//
//        if(value != null) {
//            Log.d("searchActivity", "Not null: " + value);
//            searchShow(value);
//        }
//
//        initLayout();
//        setListeners();
//
//        //GetSeriesBanner banner = new GetSeriesBanner(this);
//        //banner.execute();
//
//        //GetApi apiTest = new GetApi();
//        //apiTest.execute();
//    }
//
//    public void initLayout() {
//        //assert showListView != null;
//        showListView.setAdapter(showViewAdapter);
//        registerForContextMenu(showListView);
//
//        //Setup for a long press.
//        CharSequence text = "Long Press!";
//        int duration = Toast.LENGTH_SHORT;
//        final Toast toast = Toast.makeText(this, text, duration);
////        showListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
////            @Override
////            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                toast.show();
////                return false;
////            }
////                positionToRemove = position;
////                if (mActionMode != null) {
////                    return false;
////                }
////
////                // Start the CAB using the ActionMode.Callback defined above
////                mActionMode = startSupportActionMode(mActionModeCallback);
////                view.setSelected(true);
////                return true;
////            }
////        });
//    }
//
//    public void setListeners() {
//        //Allows the user to press the button to search for a show.
//        searchShowButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String show = searchShowEditText.getText().toString();
//                if (!show.equals("")) {
//                    showList.clear();
//                    searchShow(show); //Calls API
//                    //addShowView(show); //Currently doesn't call API for this
//                }
//                searchShowEditText.setText("");
//                InputMethodManager imm = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                imm.hideSoftInputFromWindow(searchShowEditText.getWindowToken(), 0);
//            }
//        });
//
//        // Allows the user to press "enter" to search for a show.
//        searchShowEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//                    if (event.getAction() == KeyEvent.ACTION_UP) {
//                        String show = searchShowEditText.getText().toString();
//                        if (!show.equals("")) {
//                            //displayResults();
//                        }
//                        searchShowEditText.setText("");
//                        InputMethodManager imm = (InputMethodManager)
//                                getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                        imm.hideSoftInputFromWindow(searchShowEditText.getWindowToken(), 0);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public String searchShow(String show) {
//        Log.d("searchShow", "Searching for " + show + "...");
//        //database.execSQL("INSERT INTO recent_search VALUES('" + show + "');");
//
//        GetShowInformation getShowInformation = new GetShowInformation();
//
//        getShowInformation.execute(show);
//        try {
//            getShowInformation.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        //Loop through all the series found
//        NodeList seriesList = getShowInformation.getDoc().getElementsByTagName("Series");
//        for (int temp = 0; temp < seriesList.getLength(); temp++) {
//            Node nNode = seriesList.item(temp);
//
//            //The node is the "Series" root node. Traverse this to find information about this show.
//            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                Element eElement = (Element) nNode;
//                Log.d("seriesname", "SeriesName: " +  eElement
//                        .getElementsByTagName("SeriesName")
//                        .item(0)
//                        .getTextContent());
//
//                //Adds a view to the screen
//                addShowView(eElement
//                        .getElementsByTagName("SeriesName")
//                        .item(0)
//                        .getTextContent());
//            }
//        }
//        return "";
//    }
//
////    public void getRecentSearches() {
////        Cursor resultSet = database.rawQuery("Select * from recent_search", null);
////        resultSet.moveToFirst();
////        int i = 0;
////        if(resultSet != null) {
////            String recent = "Recent searches...\n";
////            while (resultSet.isAfterLast() == false && !(i > 2)) {
////                recent += resultSet.getString(resultSet
////                        .getColumnIndex("recent_search"));
////
////                recent += '\n';
////                resultSet.moveToNext();
////                i++;
////            }
////            listView.setText(recent);
////        }
////    }
//
//    public void addShowView(String showName) {
//        showList.add(showName);
//        showViewAdapter.notifyDataSetChanged();
//    }
//
//    protected void addShow(Show show) {
//        Uri uri = Uri.parse(ShowContentProvider.CONTENT_URI + "/series/" + show.getId());
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(ShowTable.SERIES_KEY_TVDB_ID, show.getTvdbID());
//        contentValues.put(ShowTable.SERIES_KEY_LANGUAGE, show.getLanguage());
//        contentValues.put(ShowTable.SERIES_KEY_NAME, show.getName());
//        contentValues.put(ShowTable.SERIES_KEY_BANNER, show.getBanner());
//        contentValues.put(ShowTable.SERIES_KEY_OVERVIEW, show.getOverview());
//        contentValues.put(ShowTable.SERIES_KEY_FIRST_AIRED, show.getFirstAired());
//        contentValues.put(ShowTable.SERIES_KEY_NETWORK, show.getNetwork());
//        contentValues.put(ShowTable.SERIES_KEY_IMDB_ID, show.getImdbID());
//        contentValues.put(ShowTable.SERIES_KEY_STATUS, show.getStatus());
//        contentValues.put(ShowTable.SERIES_KEY_EPISODES_SEEN, show.getEpisodesSeen());
//
//        Uri uriReturn = getContentResolver().insert(uri, contentValues);
//        int id = Integer.parseInt(uriReturn.getLastPathSegment());
//        show.setId(id);
//        //fillData();
//    }
//
//}
