package com.mnchapel.divinglogbook;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DiveActivity extends Fragment {

    // The dive to show
    private int diveKey;

    private TabLayout tab_layout;
    private ViewPager view_pager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dive, container, false);

        diveKey = getArguments().getInt("diveKey");

        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.summary_tab_icon));
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.chart_tab_icon));
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.equipment_tab_icon));
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.bio_tab_icon));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        view_pager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(view_pager);

        return view;
    }


    /**
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        Bundle args = new Bundle();
        args.putInt("diveKey", diveKey);

        DiveFragmentPager adapter = new DiveFragmentPager(getChildFragmentManager(), tab_layout.getTabCount(), args);
        viewPager.setAdapter(adapter);


        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
