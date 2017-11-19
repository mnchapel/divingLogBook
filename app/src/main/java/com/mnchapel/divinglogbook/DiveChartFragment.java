package com.mnchapel.divinglogbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Dive;
import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.DiveSample;

import java.util.List;


public class DiveChartFragment extends Fragment {

    /**
     * Constructor
     */
    public DiveChartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dive_chart, container, false);


        HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        int diveKey = getArguments().getInt("diveKey");
        Dive dive = activity.getDive(diveKey);
        List<DiveSample> diveSampleList = dive.getDiveSampleList();

        LineGraph lineGraph = (LineGraph) view.findViewById(R.id.diveChartLineGraph);
        lineGraph.setDiveSample(diveSampleList);
        lineGraph.setSampleInterval(dive.getSampleInterval());

        // Inflate the layout for this fragment
        return view;
    }
}

