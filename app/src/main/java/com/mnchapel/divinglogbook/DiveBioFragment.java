package com.mnchapel.divinglogbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 *
 */
public class DiveBioFragment extends Fragment {

    /**
     * @brief Default constructor.
     */
    public DiveBioFragment() {
        // Required empty public constructor
    }



    /**
     * @brief
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dive_bio, container, false);
    }
}
