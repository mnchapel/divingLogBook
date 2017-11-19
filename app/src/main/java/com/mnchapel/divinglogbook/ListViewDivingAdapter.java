package com.mnchapel.divinglogbook;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Dive;


/**
 * Created by Marie-Neige on 11/08/2017.
 */
public class ListViewDivingAdapter extends RecyclerView.Adapter<ListViewDivingAdapter.ViewHolder> {

    //
    //private static LayoutInflater inflater = null;

    //
    List<Dive> diveList;

    List<String> monthGroup;

    int currentMonthId;

    private OnActionCompleted callBack;



    /**
     * Constructor
     */
    public ListViewDivingAdapter(List<String> monthGroup,
                                 List<Dive> diveList,
                                 OnActionCompleted callBack) {
        this.callBack = callBack;
        this.monthGroup = monthGroup;
        this.diveList = diveList;
        currentMonthId = 0;
    }



    /**
     *
     * @param parent:
     * @param viewType:
     * @return the view holder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_dive_book_day, parent, false);
        return new ViewHolder(view, callBack);
    }



    /**
     *
     * @param holder:
     * @param position:
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dive dive = diveList.get(position);
        Date date = dive.getStartTime().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd\nEEE");

        String site = dive.getSite();
        String duration = DateUtils.formatElapsedTime(dive.getDuration());
        String maxDepth = String.valueOf(dive.getMaxDepth());
        holder.display(dateFormat.format(date), duration, maxDepth, site);
    }



    /**
     *
     * @param position:
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }



    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return diveList.size();
    }






    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private OnActionCompleted callBack;
        public TextView date;
        public TextView duration;
        public TextView maxDepth;
        public TextView site;

        public ViewHolder(final View view, OnActionCompleted callBack) {
            super(view);

            view.setOnClickListener(this);
            this.callBack = callBack;

            date = (TextView) view.findViewById(R.id.diveBookDayDate);
            duration = (TextView) view.findViewById(R.id.diveBookDayDurationValue);
            maxDepth = (TextView) view.findViewById(R.id.diveBookDayMaxDepthValue);
            site = (TextView) view.findViewById(R.id.diveBookDaySiteValue);
        }


        public void display(String dateValue,
                            String durationValue,
                            String maxValue,
                            String siteValue) {
            date.setText(dateValue);
            duration.setText(durationValue);
            maxDepth.setText(maxValue);
            site.setText(siteValue);
        }

        @Override
        public void onClick(View v) {
            callBack.onClickDiveDay(getLayoutPosition());
        }
    }

    public interface OnActionCompleted {
        public void onClickDiveDay(int position);
    }
}
