package com.mnchapel.divinglogbook;

import android.content.Context;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;



/**
 * Created by Marie-Neige on 11/08/2017.
 */
public class ListViewDivingAdapter extends BaseExpandableListAdapter {

    //
    private static LayoutInflater inflater = null;

    //
    List<List<Dive>> diveList;

    List<String> monthGroup;



    /**
     * Constructor
     */
    public ListViewDivingAdapter(Context context,
                                 List<String> monthGroup,
                                 List<List<Dive>> diveList) {
        this.monthGroup = monthGroup;
        this.diveList = diveList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return diveList.get(groupPosition).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return diveList.get(groupPosition).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean b,
                             View view,
                             ViewGroup parent) {
        if(view == null)
            view = inflater.inflate(R.layout.row, parent, false);

        Date startTime = diveList.get(groupPosition).get(childPosition).getStartTime().getTime();

        // Site
        TextView site = (TextView) view.findViewById(R.id.rowSiteValue);
        site.setText(diveList.get(groupPosition).get(childPosition).getSite());

        // Date
        TextView date = (TextView) view.findViewById(R.id.rowDateValue);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date.setText(dateFormat.format(startTime));

        // Time in
        TextView timeIn = (TextView) view.findViewById(R.id.rowTimeIn);
        SimpleDateFormat timeInFormat = new SimpleDateFormat("HH:mm");
        timeIn.setText(timeInFormat.format(startTime));

        // Duration
        TextView duration = (TextView) view.findViewById(R.id.rowDurationValue);
        duration.setText(DateUtils.formatElapsedTime(diveList.get(groupPosition).get(childPosition).getDuration()));

        // Max Depth
        TextView maxDepth = (TextView) view.findViewById(R.id.rowMaxDepthValue);
        maxDepth.setText(Float.toString(diveList.get(groupPosition).get(childPosition).getMaxDepth()));

        return view;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return monthGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return monthGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition,
                             boolean b,
                             View view,
                             ViewGroup parent) {
        if(view == null)
            view = inflater.inflate(R.layout.group_list_expandable, parent, false);

        TextView text = (TextView) view.findViewById(R.id.groupListExpandableMonthText);
        text.setText(monthGroup.get(groupPosition));

        TextView divingCount = (TextView) view.findViewById(R.id.groupListExpandableDivingCount);
        divingCount.setText(getChildrenCount(groupPosition)+"");

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,
                                     int childPosition) {
        return true;
    }
}
