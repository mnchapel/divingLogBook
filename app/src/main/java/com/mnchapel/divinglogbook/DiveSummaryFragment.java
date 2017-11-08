package com.mnchapel.divinglogbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;


public class DiveSummaryFragment extends Fragment {

    private Dive dive;

    private int diveKey;

    /**
     * @brief Default constructor.
     */
    public DiveSummaryFragment(){
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                dive = (Dive) data.getParcelableExtra("diveUpdated");

                fillGeneralData(this.getView(), dive);
                fillConditionsData(this.getView(), dive);
                fillSummaryData(this.getView(), dive);

                HomeMenuActivity activity = (HomeMenuActivity) getActivity();
                activity.setDive(diveKey, dive);
            }
        }
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

        HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        diveKey = getArguments().getInt("diveKey");
        dive = activity.getDive(diveKey);

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
                startActivityForResult(intent, 1);
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
        TextView site = (TextView) view.findViewById(R.id.diveSummarySiteValue);
        site.setText(dive.getSite());

        // Town/Country
        TextView townCountry = (TextView) view.findViewById(R.id.diveSummaryTownCountryValue);
        townCountry.setText(dive.getTownCountry());

        // Objective
        TextView objective = (TextView) view.findViewById(R.id.diveSummaryObjectiveValue);
        String objectiveValue = getResources().getStringArray(R.array.objective_name)[dive.getObjective()];
        objective.setText(objectiveValue);

        // Buddy
        TextView buddy = (TextView) view.findViewById(R.id.diveSummaryBuddyValue);
        buddy.setText(dive.getBuddy());

        // Instructor
        ImageView instructorText = (ImageView) view.findViewById(R.id.diveSummaryInstructorIcon);
        TextView instructorValue = (TextView) view.findViewById(R.id.diveSummaryInstructorValue);
        if(dive.getObjective() == 0) // Exploration
        {
            instructorText.setVisibility(View.GONE);
            instructorValue.setVisibility(View.GONE);
        }
        else // Technical
            instructorValue.setText(dive.getInstructor());
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
