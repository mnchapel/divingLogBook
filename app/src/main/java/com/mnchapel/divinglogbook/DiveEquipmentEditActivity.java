package com.mnchapel.divinglogbook;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

public class DiveEquipmentEditActivity extends AppCompatActivity {

    //
    private Dive dive;



    /**
     *
     */
    private void fillForm() {

    }



    /**
     *
     * @param savedInstanceState:
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_equipment_edit);

        Bundle bundle = getIntent().getExtras();
        dive = bundle.getParcelable("dive");

        fillForm();

        ActionBar actionBar = getSupportActionBar();
        /*actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.layout_edit_menu, null);
        actionBar.setCustomView(customView);
        actionBar.setDisplayShowCustomEnabled(true);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setNavigationIcon(R.drawable.close_cross);
        parent.setContentInsetsAbsolute(0,0);

        parent.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
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

}
