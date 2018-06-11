package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.equipment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mnchapel.divinglogbook.R;



public class DiveEquipmentTankEditActivity extends DiveEquipmentEditActivity {

    private FrameLayout frameLayout;
    private LinearLayout mixtureLayout;
    private TextView startPressureValue;
    private TextView endPressureValue;
    private int yStartPressurePrevious;
    private int yEndPressurePrevious;

    private int maxStartPressureY;
    private int minStartPressureScreenY;
    private int minStartPressureLayoutY;
    private int maxPressureLayoutY;


    private int startRealY = 135;
    private int endRealY = 41;
    private int pressureHeight;

    /**
     *
     * @param savedInstanceState:
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_equipment_tank_edit);
        getSupportActionBar().setTitle("Edit tank");

        initEditText();

        mixtureLayout = (LinearLayout) findViewById(R.id.diveEquipmentTankEditMixture);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.equipment_tank_type, R.layout.layout_spinner_headline);
        adapter.setDropDownViewResource(R.layout.layout_drop_down_spinner);
        final Spinner spinnerTankType = (Spinner) findViewById(R.id.diveEquipmentTankEditType);
        spinnerTankType.setAdapter(adapter);

        final ImageView background = (ImageView) findViewById(R.id.diveEquipmentTankEditImageBackground);

        spinnerTankType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemName = spinnerTankType.getSelectedItem().toString();
                switch (itemName) {
                    case "Simple":
                        background.setImageDrawable(getResources().getDrawable(R.drawable.equipment_simple_tank_background, null));
                        break;
                    case "Twin":
                        background.setImageDrawable(getResources().getDrawable(R.drawable.equipment_twin_tank_background, null));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


/*        ArrayAdapter adapterMixture = ArrayAdapter.createFromResource(this,
                R.array.equipment_tank_mixture, R.layout.layout_spinner_headline);
        adapterMixture.setDropDownViewResource(R.layout.layout_drop_down_spinner);
        final Spinner spinnerTankMixture = (Spinner) findViewById(R.id.diveEquipmentTankEditMixtureValue);
        spinnerTankMixture.setAdapter(adapterMixture);

        spinnerTankMixture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearMixtureLayout();
                String itemName = spinnerTankMixture.getSelectedItem().toString();
                switch (itemName) {
                    case "Air":
                            View air_oxygen = View.inflate(getBaseContext(), R.layout.layout_oxygen_gaz, (ViewGroup)findViewById(R.id.oxygenGazLayout));
                            View air_nitrogen = View.inflate(getBaseContext(), R.layout.layout_nitrogen_gaz, (ViewGroup)findViewById(R.id.nitrogenGazLayout));
                            mixtureLayout.addView(air_oxygen);
                            mixtureLayout.addView(air_nitrogen);
                            EditText oxygen_value = (EditText) air_oxygen.findViewById(R.id.oxygenGazValue);
                            oxygen_value.setText("21");
                            EditText nitrogen_value = (EditText) air_nitrogen.findViewById(R.id.nitrogenGazValue);
                        nitrogen_value.setText("79");
                        break;
                    case "Nitrox":
                            View nitrox_oxygen = View.inflate(getBaseContext(), R.layout.layout_oxygen_gaz, (ViewGroup)findViewById(R.id.oxygenGazLayout));
                            View nitrox_nitrogen = View.inflate(getBaseContext(), R.layout.layout_nitrogen_gaz, (ViewGroup)findViewById(R.id.nitrogenGazLayout));
                            mixtureLayout.addView(nitrox_oxygen);
                            mixtureLayout.addView(nitrox_nitrogen);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        frameLayout = (FrameLayout) findViewById(R.id.diveEquipmentTankEditFrameLayout);
        startPressureValue = (TextView) findViewById(R.id.diveEquipmentTankEditStartPressureValue);
        endPressureValue = (TextView) findViewById(R.id.diveEquipmentTankEditEndPressureValue);

        maxStartPressureY = -1;
        minStartPressureScreenY = -1;
        yStartPressurePrevious = -1;
        yEndPressurePrevious = -1;

        LinearLayout startPressure = (LinearLayout) findViewById(R.id.diveEquipmentTankEditStartPressureValueAndUnit);
        LinearLayout endPressure = (LinearLayout) findViewById(R.id.diveEquipmentTankEditEndPressureValueAndUnit);


        startPressure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:

                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.diveEquipmentTankEditStartPressure);

                        int screenY = (int)event.getRawY();
                        if(yStartPressurePrevious < 0)
                            yStartPressurePrevious = screenY;

                        float viewY = screenY - yStartPressurePrevious;

                        LinearLayout test = (LinearLayout) findViewById(R.id.diveEquipmentTankEditStartPressureValueAndUnit);
                        int[] coords = new int[2];
                        test.getLocationOnScreen(coords);
                        int vY = coords[1];

                        if(minStartPressureScreenY < 0) {
                            minStartPressureScreenY = vY;
                            minStartPressureLayoutY = (int)relativeLayout.getY();
                        }

                        if(vY+viewY <= minStartPressureScreenY+pressureHeight
                        && vY+viewY >= minStartPressureScreenY) {
                            relativeLayout.setY(relativeLayout.getY() + viewY);
                            yStartPressurePrevious = screenY;
                            int pressureValue = (int)((pressureHeight - (vY+viewY- minStartPressureScreenY))*(250.0f/pressureHeight));
                            startPressureValue.setText(String.valueOf(pressureValue));
                        }
                        else if(vY+viewY < minStartPressureScreenY) {
                            relativeLayout.setY(minStartPressureLayoutY);
                            yStartPressurePrevious = minStartPressureScreenY;
                            startPressureValue.setText(String.valueOf(250));
                        }
                        else if(vY+viewY > minStartPressureScreenY+pressureHeight) {
                            relativeLayout.setY(minStartPressureLayoutY+pressureHeight);
                            yStartPressurePrevious = minStartPressureScreenY+pressureHeight;
                            startPressureValue.setText(String.valueOf(0));
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });



        endPressure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:

                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.diveEquipmentTankEditEndPressure);

                        int screenY = (int)event.getRawY();
                        if(yEndPressurePrevious < 0)
                            yEndPressurePrevious = screenY;

                        float viewY = screenY - yEndPressurePrevious;

                        LinearLayout test = (LinearLayout) findViewById(R.id.diveEquipmentTankEditEndPressureValueAndUnit);
                        int[] coords = new int[2];
                        test.getLocationOnScreen(coords);
                        int vY = coords[1];

                        if(maxStartPressureY < 0) {
                            maxStartPressureY = vY;
                            maxPressureLayoutY = (int)relativeLayout.getY();
                        }

                        if(vY+viewY >= maxStartPressureY-pressureHeight
                        && vY+viewY <= maxStartPressureY) {
                            relativeLayout.setY(relativeLayout.getY() + viewY);
                            yEndPressurePrevious = screenY;
                            int pressureValue = (int)(((maxStartPressureY - vY+viewY))*(250.0f/pressureHeight));
                            endPressureValue.setText(String.valueOf(pressureValue));
                        }
                        else if(vY+viewY > maxStartPressureY) {
                            relativeLayout.setY(maxPressureLayoutY);
                            yEndPressurePrevious = maxStartPressureY;
                            endPressureValue.setText(String.valueOf(0));
                        }
                        else if(vY+viewY < maxStartPressureY-pressureHeight) {
                            relativeLayout.setY(maxPressureLayoutY-pressureHeight);
                            yEndPressurePrevious = maxStartPressureY-pressureHeight;
                            endPressureValue.setText(String.valueOf(250));
                        }


                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    private void clearMixtureLayout() {
        while(mixtureLayout.getChildCount() > 1)
            mixtureLayout.removeViewAt(1);
    }



    private void initEditText() {
        TextView volumeValue = (TextView) findViewById(R.id.diveEquipmentTankEditVolumeValueUnit);
        volumeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void editTextClearFocus(final EditText editText) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    editText.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                return true;
            }
        });
    }



    /**
     *
     * @param hasFocus:
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int bgRealWidth = 68;
        int bgRealHeight = 208;

        int firstLineY = bgRealHeight-startRealY;

        ImageView tankBackground = (ImageView) findViewById(R.id.diveEquipmentTankEditImageBackground);
        LinearLayout startPressureValueUnit = (LinearLayout) findViewById(R.id.diveEquipmentTankEditStartPressureValueAndUnit);
        RelativeLayout startPressure = (RelativeLayout) findViewById(R.id.diveEquipmentTankEditStartPressure);
        View startPressureLine = findViewById(R.id.diveEquipmentTankEditStartPressureLine);

        int bgScaledWidth = tankBackground.getWidth();
        int bgScaledHeight = tankBackground.getHeight();
        FrameLayout.LayoutParams paramsTest = (FrameLayout.LayoutParams)tankBackground.getLayoutParams();
        int bgPaddingLeft = paramsTest.getMarginStart();

        int widthStartPressureValueUnit = startPressureValueUnit.getWidth();
        int heightStartPressureValueUnit = startPressureValueUnit.getHeight();

        int scaledW = bgRealWidth * bgScaledHeight / bgRealHeight;
        int scaledFirstLineY = firstLineY * bgScaledHeight / bgRealHeight;

        // Set line width
        RelativeLayout.LayoutParams line  = (RelativeLayout.LayoutParams) startPressureLine.getLayoutParams();
        line.width = widthStartPressureValueUnit + scaledW;

        int marginLeft = (bgScaledWidth - scaledW)/2 + bgPaddingLeft - widthStartPressureValueUnit;
        int marginTop = scaledFirstLineY - heightStartPressureValueUnit;
        int marginRight = 0;
        int marginBottom = 0;

        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(startPressure.getLayoutParams());
        marginParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(marginParams);
        startPressure.setLayoutParams(params);

        placeEndPressure(bgRealWidth, bgRealHeight, bgScaledWidth, bgScaledHeight, bgPaddingLeft);
        placeVolume(bgRealWidth, bgRealHeight, bgScaledWidth, bgScaledHeight, bgPaddingLeft);

        pressureHeight = (startRealY-endRealY) * bgScaledHeight / bgRealHeight;
    }


    /**
     *
     * @param bgRealWidth:
     * @param bgRealHeight:
     * @param ivScaledWidth:
     * @param ivScaledHeight:
     * @param bgPaddingLeft:
     */
    private  void placeEndPressure(int bgRealWidth, int bgRealHeight,
                                   int ivScaledWidth, int ivScaledHeight,
                                   int bgPaddingLeft) {
        int realY = bgRealHeight-endRealY;

        LinearLayout endPressureValueUnit = (LinearLayout) findViewById(R.id.diveEquipmentTankEditEndPressureValueAndUnit);
        RelativeLayout endPressure = (RelativeLayout) findViewById(R.id.diveEquipmentTankEditEndPressure);
        View endPressureLine = findViewById(R.id.diveEquipmentTankEditEndPressureLine);

        int scaledW = bgRealWidth * ivScaledHeight / bgRealHeight;
        int scaledY = realY * ivScaledHeight / bgRealHeight;

        int bgScaledWidth = bgRealWidth * ivScaledHeight / bgRealHeight;

        int endPressureValueUnitWidth = endPressureValueUnit.getWidth();
        int endPressureValueUnitHeight = endPressureValueUnit.getHeight();

        // Resize the line
        RelativeLayout.LayoutParams line  = (RelativeLayout.LayoutParams) endPressureLine.getLayoutParams();
        line.width = endPressureValueUnitWidth + scaledW;

        int marginLeft = (ivScaledWidth - bgScaledWidth)/2 + bgPaddingLeft - endPressureValueUnitWidth;
        int marginTop = scaledY - endPressureValueUnitHeight;
        int marginRight = 0;
        int marginBottom = 0;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        endPressure.setLayoutParams(params);
    }


    /**
     *
     * @param bgRealWidth:
     * @param bgRealHeight:
     * @param ivScaledWidth:
     * @param ivScaledHeight:
     * @param bgPaddingLeft:
     */
    private  void placeVolume(int bgRealWidth, int bgRealHeight,
                               int ivScaledWidth, int ivScaledHeight,
                               int bgPaddingLeft) {

        int realY = bgRealHeight-92;

        LinearLayout volume = (LinearLayout) findViewById(R.id.diveEquipmentTankEditVolume);

        int volumeWidth = volume.getWidth();
        int volumeHeight = volume.getHeight();

        int scaledY = realY * ivScaledHeight / bgRealHeight;

        int marginLeft = ivScaledWidth/2 + bgPaddingLeft - volumeWidth/2;
        int marginTop = scaledY - volumeHeight/2;
        int marginRight = 0;
        int marginBottom = 0;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        volume.setLayoutParams(params);
    }
}
