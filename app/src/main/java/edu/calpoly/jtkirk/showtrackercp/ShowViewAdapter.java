package edu.calpoly.jtkirk.showtrackercp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by jaredkirk on 4/19/16.
 */
public class ShowViewAdapter extends BaseAdapter {
    /** The application Context in which this JokeListAdapter is being used. */
    private Context m_context;

    /** The data set to which this Show is bound. */
    private List<Show> showList;

    public ShowViewAdapter(Context context, List<Show> showList) {
        m_context = context;
        this.showList = showList;
    }


    public int getCount() {
        return showList.size();
    }

    public Object getItem(int position) {
        return showList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ShowView view;
        if(convertView != null) {
            view = (ShowView)convertView;
        }
        else {
            view = new ShowView(m_context, (Show)getItem(position));
        }
        //view.setJoke((Joke) getItem(position));
        return new ShowView(m_context, (Show)getItem(position));
    }
}
