package com.mnchapel.divinglogbook;

import android.content.Context;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;



/**
 * Created by Marie-Neige on 11/08/2017.
 */
public class ListViewDivingAdapter extends BaseAdapter {

    //
    private static LayoutInflater inflater = null;

    //
    List<Dive> diveList;



    /**
     * Constructor
     */
    public ListViewDivingAdapter(Context context, TreeMap<String,Dive> diveList) {
        this.diveList = new ArrayList<>(diveList.values());
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return diveList.size();
    }



    @Override
    public Object getItem(int position) {
        return diveList.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int i, View view, ViewGroup parent) {

        if(view == null)
            view = inflater.inflate(R.layout.row, parent, false);

        // Date
        TextView date = (TextView) view.findViewById(R.id.diveBookDate);
        date.setText(diveList.get(i).getDate());

        // Duration
        TextView duration = (TextView) view.findViewById(R.id.diveBookDuration);
        duration.setText(DateUtils.formatElapsedTime(diveList.get(i).getDuration()));

        // Max Depth
        TextView maxDepth = (TextView) view.findViewById(R.id.diveBookDepthMax);
        maxDepth.setText(Float.toString(diveList.get(i).getMaxDepth()));

        TextView diving_count = (TextView) view.findViewById(R.id.diving_count);
        diving_count.setText("1");

        return view;
    }
}
