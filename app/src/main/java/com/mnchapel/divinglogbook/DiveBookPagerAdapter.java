package com.mnchapel.divinglogbook;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Dive;

import java.util.List;

/**
 * Created by Marie-Neige on 18/11/2017.
 */

public class DiveBookPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> monthGroup;
    private List<List<Dive>> monthOrderedDive;
    private ListViewDivingAdapter.OnActionCompleted callBack;


    /**
     * Constructor
     */
    DiveBookPagerAdapter(Context context,
                         ListViewDivingAdapter.OnActionCompleted callBack,
                         List<String> monthGroup,
                         List<List<Dive>> monthOrderedDive) {
        this.context = context;
        this.callBack = callBack;
        this.monthGroup = monthGroup;
        this.monthOrderedDive = monthOrderedDive;
    }



    /**
     *
     * @param container:
     * @param position:
     * @param object:
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)  {
        container.removeView((CoordinatorLayout)object);
    }


    /**
     * Get the number of element in carousel
     * @return the number of element
     */
    @Override
    public int getCount() {
        return monthGroup.size();
    }



    /**
     *
     * @param container:
     * @param position:
     * @return view
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dive_book_recycle_view, container, false);

        initRecyclerView(view, position);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.diveBookCollapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(monthGroup.get(position));

        container.addView(view);

        return view;
    }


    private void initRecyclerView (View view, int position) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.diveBookRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ListViewDivingAdapter(monthGroup, monthOrderedDive.get(position), callBack));
        recyclerView.setNestedScrollingEnabled(true);
    }



    /**
     *
     * @param view: the current view.
     * @param object:
     * @return true
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CoordinatorLayout) object);
    }
}
