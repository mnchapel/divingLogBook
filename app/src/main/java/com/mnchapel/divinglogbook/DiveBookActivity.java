package com.mnchapel.divinglogbook;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.TreeMap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;



public class DiveBookActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dive_book, container, false);

        HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        final TreeMap<String,Dive> diveList = activity.getDiveList();

        ListView list_view = (ListView) view.findViewById(R.id.listview);
        list_view.setAdapter(new ListViewDivingAdapter(getActivity(), diveList));

        list_view.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Dive dive = (Dive) adapterView.getItemAtPosition(i);
                        Fragment fragment = new DiveActivity();
                        Bundle args = new Bundle();
                        args.putString("diveKey", dive.getDate()+dive.getTimeIn());
                        fragment.setArguments(args);

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragment_manager = getActivity().getSupportFragmentManager();
                        fragment_manager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        getActivity().setTitle("Dive #"+i);
                    }
                }
        );

        return view;
    }
}
