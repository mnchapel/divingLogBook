package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.equipment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Dive;
import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Equipment;
import com.mnchapel.divinglogbook.HomeMenuActivity;
import com.mnchapel.divinglogbook.R;
import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.SuitEquipment;
import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.TankEquipment;

import java.util.ArrayList;

import static com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Equipment.Type.TANK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiveEquipmentFragment
        extends Fragment
        implements ViewPager.OnPageChangeListener {

    private CardView cardViewAddItem;
    private Dive dive;
    private int diveKey;
    private int dotsCount;
    private ImageView[] dots;
    private DiveEquipmentPagerAdapter diveEquipmentAdapterView;
    private LinearLayout pagerIndicator;
    ArrayList<Equipment> equipmentItemList;



    /**
     * Constructor
     */
    public DiveEquipmentFragment() {
        equipmentItemList = new ArrayList<>();
    }



    /**
     *
     * @param inflater:
     * @param container:
     * @param savedInstanceState:
     * @return the current view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dive_equipment, container, false);

        ViewPager diveEquipmentViewPager = (ViewPager) view.findViewById(R.id.diveEquipmentViewPager);
        diveEquipmentAdapterView = new DiveEquipmentPagerAdapter(getActivity(), equipmentItemList);
        diveEquipmentViewPager.setAdapter(diveEquipmentAdapterView);
        diveEquipmentViewPager.addOnPageChangeListener(this);

        pagerIndicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);

        setUiPageViewController();

        ListView listAddItem = (ListView) view.findViewById(R.id.diveEquipmentListAdd);
        ArrayAdapter<String> addItem = new ArrayAdapter<>(getActivity(), R.layout.layout_equipment_list_add_item, getResources().getStringArray(R.array.dive_equipment_add_item_list));
        listAddItem.setAdapter(addItem);

        cardViewAddItem = (CardView) view.findViewById(R.id.diveEquipmentCardView);

        HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        diveKey = getArguments().getInt("diveKey");
        dive = activity.getDive(diveKey);

        // Edit dive button
        ImageButton diveSummaryFab = (ImageButton) view.findViewById(R.id.diveEquipmentEditFab);

        diveSummaryFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), DiveEquipmentEditActivity.class);
                intent.putExtra("dive", dive);
                startActivityForResult(intent, 1);
            }
        });

        // Add dive button
        ImageButton addFab = (ImageButton) view.findViewById(R.id.diveEquipmentAddFab);
        addFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cardViewAddItem.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout linearLayoutViewPager = (LinearLayout) view.findViewById(R.id.diveEquipmentLinearLayoutViewPager);
        linearLayoutViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewAddItem.setVisibility(View.INVISIBLE);
            }
        });

        final ListView listView = (ListView) view.findViewById(R.id.diveEquipmentListAdd);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.diveEquipmentAddItemText);
                String equipmentType = textView.getText().toString();
                cardViewAddItem.setVisibility(View.INVISIBLE);

                Intent intent;
                Equipment equipment;
                switch (equipmentType) {
                    case "Tank":
                        equipment = new TankEquipment();
                        intent = new Intent(getActivity(), DiveEquipmentTankEditActivity.class);
                        break;
                    case "Suit":
                        equipment = new SuitEquipment();
                        intent = new Intent(getActivity(), DiveEquipmentSuitEditActivity.class);
                        break;
                    default:
                        equipment = new Equipment(TANK);
                        intent = new Intent(getActivity(), DiveEquipmentEditActivity.class);
                        break;
                }


                intent.putExtra("equipment", equipment);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }



    /**
     *
     * @param position:
     * @param positionOffset:
     * @param positionOffsetPixels:
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }



    /**
     *
     * @param position:
     */
    @Override
    public void onPageSelected(int position) {
        for(int i=0; i<dotsCount; i++)
            dots[i].setImageDrawable(getContext().getDrawable(R.drawable.carousel_dot_non_selected));

        dots[position].setImageDrawable(getContext().getDrawable(R.drawable.carousel_dot_selected));
    }



    /**
     *
     * @param state:
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }



    /**
     *
     */
    private void setUiPageViewController() {

        dotsCount = diveEquipmentAdapterView.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(getContext().getDrawable(R.drawable.carousel_dot_non_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pagerIndicator.addView(dots[i], params);
        }

        if(dotsCount>0)
            dots[0].setImageDrawable(getContext().getDrawable(R.drawable.carousel_dot_selected));
    }
}
