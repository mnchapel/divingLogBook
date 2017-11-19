package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.equipment;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mnchapel.divinglogbook.R;

public class DiveEquipmentTankEditActivity extends DiveEquipmentEditActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_equipment_tank_edit);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int firstLineY = 47;

        int realW = 92;
        int realH = 171;

        ImageView tankBackground = (ImageView) findViewById(R.id.diveEquipmentEditImageBackground);
        RelativeLayout startPressure = (RelativeLayout) findViewById(R.id.diveEquipmentTankEditStartPressure);

        int width = tankBackground.getWidth();
        int height = tankBackground.getHeight();
        int tankBackgroundPaddingLeft = (int)(tankBackground.getPaddingStart()/3.5f);

        int widthStartPressure = startPressure.getWidth();
        int heightStartPressure = startPressure.getHeight();

        int scaledW = realW * height / realH;
        int scaledFirstLineY = firstLineY * height / realH;

        int marginLeft = (width - scaledW)/2 - widthStartPressure - 10 + tankBackgroundPaddingLeft;
        int marginTop = scaledFirstLineY - heightStartPressure/2;
        int marginRight = 0;
        int marginBottom = 0;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        startPressure.setLayoutParams(params);
    }
}
