package com.mnchapel.divinglogbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;


public class DiveBookActivity extends Fragment {

    //
    List<Dive> diveList;

    //
    List<String> monthGroup;

    //
    List<List<Dive>> monthOrderedDive;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dive_book, container, false);
        setHasOptionsMenu(true);

        final HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        diveList = activity.getDiveList();

        createGroups();

        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.listview);
        listView.setAdapter(new ListViewDivingAdapter(getActivity(), monthGroup, monthOrderedDive));

        listView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView,
                                                View view,
                                                int groupPosition,
                                                int childPosition,
                                                long l) {
                        Dive dive = (Dive) monthOrderedDive.get(groupPosition).get(childPosition);
                        Fragment fragment = new DiveActivity();
                        Bundle args = new Bundle();

                        args.putInt("diveKey", getDiveId(groupPosition, childPosition)); // TODO : compute the dive id
                        fragment.setArguments(args);

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragment_manager = getActivity().getSupportFragmentManager();
                        fragment_manager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        getActivity().setTitle("Dive #" + (activity.getDiveSize() - getDiveId(groupPosition, childPosition)));

                        return false;
                    }
                }
        );

        return view;
    }



    /**
     * @param menu :
     * @return boolean
     */
    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.add_dive_menu, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMenuItem:
                Dive dive = new Dive();
                Intent intent = new Intent(getActivity(), DiveSummaryEditActivity.class);
                intent.putExtra("dive", dive);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * @brief Fill monthGroup and montOrderedDive
     */
    private void createGroups() {
        monthOrderedDive = new ArrayList<>();
        monthGroup = new ArrayList<>();

        if(diveList.size() == 0)
            return;

        Date startTime = diveList.get(0).getStartTime().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        monthGroup.add(dateFormat.format(startTime));

        List<Dive> diveGroup = new ArrayList<>();

        for(Dive dive: diveList) {
            String monthYear = dateFormat.format(dive.getStartTime().getTime());

            if(monthYear.compareTo(monthGroup.get(monthGroup.size()-1)) == 0) {
                diveGroup.add(dive);
            }
            else {
                monthOrderedDive.add(diveGroup);
                diveGroup = new ArrayList<>();
                diveGroup.add(dive);
                monthGroup.add(dateFormat.format(dive.getStartTime().getTime()));
            }
        }
        monthOrderedDive.add(diveGroup);
    }



    /**
     * @brief
     *
     * @param groupPosition
     * @param childPosition
     */
    private int getDiveId(int groupPosition, int childPosition) {
        int index = 0;

        for(int i=0; i<groupPosition; i++)
            index += monthOrderedDive.get(i).size();

        index += childPosition;

        return index;
    }
}
