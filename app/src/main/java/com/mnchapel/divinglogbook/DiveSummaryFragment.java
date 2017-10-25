package com.mnchapel.divinglogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class DiveSummaryFragment extends Fragment {

    /**
     * @brief Default constructor.
     */
    public DiveSummaryFragment(){
        // Required empty public constructor
    }



    /**
     * @brief onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return the current view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dive_summary, container, false);

        final Dive dive = (Dive) getArguments().getParcelable("dive");

        fillGeneralData(view, dive);
        fillConditionsData(view, dive);
        fillSummaryData(view, dive);

        // Edit dive button
        ImageButton diveSummaryFab = (ImageButton) view.findViewById(R.id.diveSummaryFab);

        diveSummaryFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), DiveSummaryEditActivity.class);
                intent.putExtra("dive", dive);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }



    /**
     * @brief
     *
     * @param dive
     */
    public void fillConditionsData(View view, Dive dive) {
        // Visibility

        // Bottom temperature
        TextView bottomTemperature = (TextView) view.findViewById(R.id.diveSummaryBottomTemperatureValue);
        bottomTemperature.setText(dive.getBottomTemperature()+"");

    }



    /**
     * @brief
     *
     * @param dive
     */
    public void fillGeneralData(View view, Dive dive) {
        // Date
        TextView date = (TextView) view.findViewById(R.id.diveSummaryDateValue);
        date.setText(dive.getDate());

        // Site

        // Town/Country

        // Objective
        TextView objective = (TextView) view.findViewById(R.id.diveSummaryObjectiveValue);
        objective.setText(dive.getObjective());

        // Buddy
    }



    /**
     * @brief
     *
     * @param dive
     */
    public void fillSummaryData(View view, Dive dive) {
        // Duration
        TextView duration = (TextView) view.findViewById(R.id.diveSummaryDurationValue);
        duration.setText(DateUtils.formatElapsedTime(dive.getDuration()));

        // Max depth
        TextView maxDepth = (TextView) view.findViewById(R.id.diveSummaryMaxDepthValue);
        maxDepth.setText(dive.getMaxDepth()+"");

        // Time in
        TextView timeIn = (TextView) view.findViewById(R.id.diveSummaryTimeInValue);
        timeIn.setText(dive.getTimeIn());

        // Time out
        TextView timeOut = (TextView) view.findViewById(R.id.diveSummaryTimeOutValue);
        timeOut.setText(dive.getTimeOut());
    }
}
