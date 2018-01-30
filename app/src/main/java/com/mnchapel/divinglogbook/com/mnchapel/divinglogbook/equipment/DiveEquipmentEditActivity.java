package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.equipment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Equipment;
import com.mnchapel.divinglogbook.R;

public class DiveEquipmentEditActivity
        extends AppCompatActivity {

    //
    private Equipment equipment;



    /**
     *
     * @param hotspotId:
     * @param x:
     * @param y:
     * @return the color.
     */
    private int getTouchColor(int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }



    /**
     *
     * @param savedInstanceState:
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_equipment_suit_edit);

        Bundle bundle = getIntent().getExtras();
        equipment = bundle.getParcelable("equipment");

        switch (equipment.getType()) {
            case TANK:
                setContentView(R.layout.activity_dive_equipment_tank_edit);
                break;
            case SUIT:
                setContentView(R.layout.activity_dive_equipment_suit_edit);
                break;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.layout_edit_menu, null);
        actionBar.setCustomView(customView);
        actionBar.setDisplayShowCustomEnabled(true);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setNavigationIcon(R.drawable.close_cross_black);
        parent.setContentInsetsAbsolute(0,0);

        parent.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    /**
     * @param menu:
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);

        return true;
    }



    /**
     * Defined actions on menu item.
     * @param item: the menu item.
     * @return true if success.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenuSaveItem:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Handle touch on screen.
     * @param e: the motion event.
     * @return true if success
     */
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        int x = (int)e.getX();
        int y = (int)e.getY();

        int touchColor = 0;

        switch(e.getAction()) {
            case MotionEvent.ACTION_UP:
                touchColor = getTouchColor(R.id.diveEquipmentEditTankTouchZone, x, y);
                break;
            default:
                break;
        }

        switch(touchColor) {
            case Color.RED:
                int a = 0;
                break;
            default:
                break;
        }

        return true;
    }*/
}
