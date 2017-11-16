package com.mnchapel.divinglogbook;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DiveSummaryEditActivity extends AppCompatActivity {

    //
    private Dive dive;



    /**
     *
     */
    private void addNewBuddy() {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diveSummaryEditLayoutBuddy);
        final LinearLayout buddyField = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_edit_new_buddy, linearLayout, false);
        linearLayout.addView(buddyField);

        EditText buddy = (EditText) buddyField.getChildAt(1);

        buddy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() == 0 && linearLayout.getChildCount() > 1) {
                    linearLayout.removeView(buddyField);
                }
                else if(charSequence.length()>0 && (before+start) == 0) {
                    addNewBuddy();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }



    /**
     * Add decompression field
     */
    private void addNewDeco() {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diveSummaryEditLayoutDeco);
        final LinearLayout decoField = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_edit_new_deco, linearLayout, false);
        linearLayout.addView(decoField);

        EditText decoMeters = (EditText) decoField.getChildAt(1);

        decoMeters.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() == 0 && linearLayout.getChildCount() > 1) {
                    linearLayout.removeView(decoField);
                }
                else if(charSequence.length()>0 && (before+start) == 0) {
                    addNewDeco();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }



    /**
     */
    private void fillForm() {
        fillGeneralForm();
        fillConditionsForm();
        fillSummaryForm();
    }



    /**
     */
    private void fillConditionsForm() {
        // Visibility
        RatingBar visibility = (RatingBar) findViewById(R.id.diveSummaryEditVisibilityValue);
        visibility.setRating(dive.getVisibility());

        // Bottom temperature
        EditText bottomTemperature = (EditText) findViewById(R.id.diveSummaryEditBottomTemperatureValue);
        bottomTemperature.setText(String.valueOf(dive.getBottomTemperature()));
    }



    /**
     */
    private void fillGeneralForm() {
        // Date
        TextView date = (TextView) findViewById(R.id.diveSummaryEditDateValue);
        date.setText(dive.getDate());

        // Site
        EditText site = (EditText) findViewById(R.id.diveSummaryEditSiteValue);
        site.setText(dive.getSite());

        // Town
        EditText townCountry = (EditText) findViewById(R.id.diveSummaryEditTownValue);
        townCountry.setText(dive.getTown());

        // Objective
        Spinner objective = (Spinner) findViewById(R.id.diveSummaryEditObjectiveValue);
        objective.setSelection(dive.getObjective());

        // Buddy
        addNewBuddy();
        List<String> buddies = dive.getBuddy();
        for(String buddyName: buddies) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diveSummaryEditLayoutBuddy);
            LinearLayout buddyField = (LinearLayout) linearLayout.getChildAt(linearLayout.getChildCount()-1);
            EditText buddy = (EditText) buddyField.getChildAt(1);
            buddy.setText(buddyName);
        }

        objective.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView instructorIcon  = (ImageView) findViewById(R.id.diveSummaryEditInstructorIcon);
                EditText instructorValue = (EditText) findViewById(R.id.diveSummaryEditInstructorValue);
                switch (i) {
                    case 0:
                        instructorIcon.setVisibility(View.GONE);
                        instructorValue.setVisibility(View.GONE);
                        break;
                    case 1:
                        instructorIcon.setVisibility(View.VISIBLE);
                        instructorValue.setVisibility(View.VISIBLE);
                        instructorValue.setText(dive.getInstructor());
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    /**
     */
    private void fillSummaryForm() {
        // Duration
        EditText duration = (EditText) findViewById(R.id.diveSummaryEditDurationValue);
        duration.setText(DateUtils.formatElapsedTime(dive.getDuration()));

        // Max depth
        EditText maxDepth = (EditText) findViewById(R.id.diveSummaryEditMaxDepthValue);
        maxDepth.setText(String.valueOf(dive.getMaxDepth()));

        // Time in
        EditText timeIn = (EditText) findViewById(R.id.diveSummaryEditTimeInValue);
        timeIn.setText(String.valueOf(dive.getTimeIn()));

        // Time out
        EditText timeOut = (EditText) findViewById(R.id.diveSummaryEditTimeOutValue);
        timeOut.setText(String.valueOf(dive.getTimeOut()));

        // Decompression
        addNewDeco();
        List<Decompression> decompressionList = dive.getDecompressionList();
        for(Decompression decompression: decompressionList) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diveSummaryEditLayoutDeco);
            LinearLayout decoField = (LinearLayout) linearLayout.getChildAt(linearLayout.getChildCount()-1);
            EditText decoMeters = (EditText) decoField.getChildAt(1);
            decoMeters.setText(String.valueOf(decompression.getMeter()));
            EditText decoMinutes = (EditText) decoField.getChildAt(3);
            decoMinutes.setText(String.valueOf(decompression.getMinute()));
        }
    }



    /**
     * @param savedInstanceState:
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_summary_edit);

        Bundle bundle = getIntent().getExtras();
        dive = bundle.getParcelable("dive");

        fillForm();
        setListeners();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenuSaveItem:
                updateDive();
                DiveXmlDocument diveXmlDocument = new DiveXmlDocument();
                try {
                    diveXmlDocument.writeDive(this, dive);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("diveUpdated", dive);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     */
    private void setListeners() {
        final EditText duration = (EditText) findViewById(R.id.diveSummaryEditDurationValue);
        final EditText timeIn = (EditText) findViewById(R.id.diveSummaryEditTimeInValue);
        final EditText timeOut = (EditText) findViewById(R.id.diveSummaryEditTimeOutValue);
        try {
            dateDatePicker();
            timePicker(duration);
            timePicker(timeIn);
            timePicker(timeOut);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    /**
     */
    private void timePicker(final EditText editText) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        final TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker,
                                  int hour,
                                  int minute) {
                editText.setText(hour+":"+minute);
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar =  Calendar.getInstance();
                try {
                    calendar.setTime(dateFormat.parse(editText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new TimePickerDialog(DiveSummaryEditActivity.this,
                                     timeListener,
                                     calendar.get(Calendar.HOUR_OF_DAY),
                                     calendar.get(Calendar.MINUTE),
                                     true).show();
            }
        });
    }



    /**
     */
    private void dateDatePicker() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
        final EditText date = (EditText) findViewById(R.id.diveSummaryEditDateValue);

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view,
                                  int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormat.format(newDate.getTime()).toString());
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar =  Calendar.getInstance();
                try {
                    calendar.setTime(dateFormat.parse(date.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(DiveSummaryEditActivity.this,
                                     dateListener,
                                     calendar.get(Calendar.YEAR),
                                     calendar.get(Calendar.MONTH),
                                     calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }



    /**
     */
    private void updateDive() {

        updateGeneral();
        updateConditions();
        updateSummary();
    }



    /**
     */
    private void updateConditions() {
        // Visibility
        RatingBar visibility = (RatingBar) findViewById(R.id.diveSummaryEditVisibilityValue);
        dive.setVisibility(visibility.getRating());

        // Bottom temperature
        EditText bottomTemperature = (EditText) findViewById(R.id.diveSummaryEditBottomTemperatureValue);
        if(!bottomTemperature.getText().toString().isEmpty())
            dive.setBottomTemperature(Float.parseFloat(bottomTemperature.getText().toString()));
    }



    /**
     */
    private void updateGeneral() {
        // Date

        // Site
        EditText site = (EditText) findViewById(R.id.diveSummaryEditSiteValue);
        dive.setSite(site.getText().toString());

        // Town/Country
        EditText town = (EditText) findViewById(R.id.diveSummaryEditTownValue);
        dive.setTown(town.getText().toString());

        // Objective
        Spinner objective = (Spinner) findViewById(R.id.diveSummaryEditObjectiveValue);
        int objectiveId = objective.getSelectedItemPosition();
        dive.setObjective(objectiveId);

        // Buddy
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diveSummaryEditLayoutBuddy);
        int nbChild = linearLayout.getChildCount();
        List<String> buddyList = new ArrayList<>();
        for(int i=0; i<nbChild; i++) {
            LinearLayout buddyField = (LinearLayout) linearLayout.getChildAt(i);
            EditText buddy = (EditText) buddyField.getChildAt(1);
            if(!buddy.getText().toString().isEmpty()) {
                String buddyName = buddy.getText().toString();
                buddyList.add(buddyName);
            }
        }
        dive.setBuddy(buddyList);

        // Instructor
        EditText instructor = (EditText) findViewById(R.id.diveSummaryEditInstructorValue);
        dive.setInstructor(instructor.getText().toString());
    }



    /**
     */
    private void updateSummary() {
        // Duration

        // Max depth

        // Time in

        // Time out

        // Decompression
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.diveSummaryEditLayoutDeco);
        int nbChild = linearLayout.getChildCount();
        List<Decompression> decompressionList = new ArrayList<>();
        for(int i=0; i<nbChild; i++) {
            LinearLayout decoField = (LinearLayout) linearLayout.getChildAt(i);
            EditText decoMeters = (EditText) decoField.getChildAt(1);
            EditText decoMinutes = (EditText) decoField.getChildAt(3);
            Decompression decompression = new Decompression();
            if(!decoMeters.getText().toString().isEmpty()
            && !decoMinutes.getText().toString().isEmpty()) {
                decompression.setMeter(Integer.parseInt(decoMeters.getText().toString()));
                decompression.setMinute(Integer.parseInt(decoMinutes.getText().toString()));
                decompressionList.add(decompression);
            }
        }
        dive.setDecompressionList(decompressionList);
    }


}
