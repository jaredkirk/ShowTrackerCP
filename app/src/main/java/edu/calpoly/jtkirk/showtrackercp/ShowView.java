package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.calpoly.jtkirk.showtrackercp.R;

public class ShowView extends LinearLayout  {
    private TextView nameOfShow;
    private Show show;
    private String name;
    private OnShowChangeListener showChangeListener;
    private TextView episodesSeen;

    public ShowView(Context context, Show show) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.show_view, this, true);

        //Initialize the number of episodes seen
        episodesSeen = new TextView(context);
        episodesSeen = (TextView) findViewById(R.id.episodesSeen);
        episodesSeen.setText(String.valueOf(show.getEpisodesSeen()));

        name = show.getName();
        nameOfShow = (TextView) findViewById(R.id.series_name);
        nameOfShow.setText(name); //set the title
    }

    /**
     * Update visual attributes to reflect to new show.
     * @param show The show to update attributes for.
     */
    public void setShow(Show show) {
        this.show = show;
        this.name = show.getName();
        nameOfShow.setText(name); //set the title
        episodesSeen.setText(show.getEpisodesSeen());
        notifyOnShowChangeListener();
    }

    //Set the show listener
    public void setOnShowChangeListener(OnShowChangeListener listener) {
        showChangeListener = listener;
    }

    //Notify the show has changed
    protected void notifyOnShowChangeListener() {
        if(showChangeListener != null) {
            showChangeListener.onShowChanged(this, show);
        }
    }

    /**
     * Interface definition for a callback to be invoked when the underlying
     * Show is changed in this ShowView object.
     */
    public static interface OnShowChangeListener {
        public void onShowChanged(ShowView view, Show show);
    }

    public Show getShow() {
        return show;
    }

}
