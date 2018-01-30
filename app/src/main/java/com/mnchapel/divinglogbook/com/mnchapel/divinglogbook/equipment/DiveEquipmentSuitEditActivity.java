package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.equipment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mnchapel.divinglogbook.R;

public class DiveEquipmentSuitEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_equipment_suit_edit);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.equipment_suit_type, R.layout.layout_spinner);
        adapter.setDropDownViewResource(R.layout.layout_drop_down_spinner);
        Spinner spinnerSuitType = (Spinner) findViewById(R.id.diveEquipmentSuitEditType);
        spinnerSuitType.setAdapter(adapter);
    }
}
