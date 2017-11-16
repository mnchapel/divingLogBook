package com.mnchapel.divinglogbook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiveEquipmentFragment extends Fragment {

    private Dive dive;
    private int diveKey;

    /**
     * Constructor
     */
    public DiveEquipmentFragment() {
        // Required empty public constructor
    }



    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dive_equipment, container, false);

        HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        diveKey = getArguments().getInt("diveKey");
        dive = activity.getDive(diveKey);

        // Edit dive button
        ImageButton diveSummaryFab = (ImageButton) view.findViewById(R.id.diveEquipmentFab);

        diveSummaryFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), DiveEquipmentEditActivity.class);
                intent.putExtra("dive", dive);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

}
