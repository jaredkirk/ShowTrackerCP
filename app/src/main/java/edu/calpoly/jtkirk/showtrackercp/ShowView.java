package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import edu.calpoly.jtkirk.showtrackercp.R;

public class ShowView extends LinearLayout  {
    private ImageView image;
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

        image = (ImageView) findViewById(R.id.series_image);
    }

    public void initializeImage() {
        GetArtwork getArtwork = new GetArtwork();
        Log.d("artwork", "show id of " + show.getTvdbID());
        getArtwork.execute(show.getTvdbID());
        try {
            getArtwork.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Picasso.with(getContext()).load(getArtwork.getImagePath()).fit().into(image);
    }

    /**
     * Update visual attributes to reflect to new show.
     * @param show The show to update attributes for.
     */
    public void setShow(Show show) {
        this.show = show;
        this.name = show.getName();
        initializeImage();
        nameOfShow.setText(name); //set the title
        episodesSeen.setText(show.getEpisodesSeen() + "");
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
