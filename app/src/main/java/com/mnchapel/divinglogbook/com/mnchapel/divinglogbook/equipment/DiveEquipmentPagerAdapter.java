package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.equipment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Equipment;
import com.mnchapel.divinglogbook.R;

import java.util.ArrayList;

/**
 * Created by Marie-Neige on 16/11/2017.
 */



public class DiveEquipmentPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Equipment> equipmentItemList;



    /**
     * Default constructor
     * @param context: context of activity
     */
    DiveEquipmentPagerAdapter(Context context, ArrayList<Equipment> equipmentItemList){
        this.context = context;
        this.equipmentItemList = equipmentItemList;
    }



    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return 0;
    }



    /**
     *
     * @param container:
     * @param position:
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View equipmentItemView = LayoutInflater.from(context).inflate(R.layout.layout_equipment_item, container, false);

        ImageView itemImageView = (ImageView) equipmentItemView.findViewById(R.id.equipmentItemImageView);

        Equipment equipment = equipmentItemList.get(position);
        switch (equipment.getType()) {
            case TANK:
                itemImageView.setImageResource(R.drawable.tank_background);
                break;
            case SUIT:
                itemImageView.setImageResource(R.drawable.equipment_suit_background);
                break;
            default:
                break;
        }

        container.addView(equipmentItemView);

        return equipmentItemView;
    }



    /**
     *
     * @param view:
     * @param object:
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }
}
