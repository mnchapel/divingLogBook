package com.mnchapel.divinglogbook;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;


public class DiveSummaryEditActivity extends AppCompatActivity {

    //
    Dive dive;



    /**
     * @ brief
     */
    private void fillForm() {
        fillGeneralForm();
        fillConditionsForm();
        fillSummaryForm();
    }



    /**
     * @brief
     */
    private void fillConditionsForm() {
        // Visibility
        EditText visibility = (EditText) findViewById(R.id.diveSummaryEditVisibilityValue);
        if(dive.getVisibility() > 0)
            visibility.setText(String.valueOf(dive.getVisibility()));

        // Bottom temperature
        EditText bottomTemperature = (EditText) findViewById(R.id.diveSummaryEditBottomTemperatureValue);
        bottomTemperature.setText(String.valueOf(dive.getBottomTemperature()));
    }



    /**
     * @brief
     */
    private void fillGeneralForm() {
        // Date
        TextView date = (TextView) findViewById(R.id.diveSummaryEditDateValue);
        date.setText(dive.getDate());

        // Site
        EditText site = (EditText) findViewById(R.id.diveSummaryEditSiteValue);
        site.setText(dive.getSite());

        // Town/Country
        EditText townCountry = (EditText) findViewById(R.id.diveSummaryEditTownCountryValue);
        townCountry.setText(dive.getTownCountry());

        // Objective
        Spinner objective = (Spinner) findViewById(R.id.diveSummaryEditObjectiveValue);
        objective.setSelection(dive.getObjective());

        // Buddy
        EditText buddy = (EditText) findViewById(R.id.diveSummaryEditBuddyValue);
        buddy.setText(dive.getBuddy());

        objective.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView instructorText  = (TextView) findViewById(R.id.diveSummaryEditInstructorText);
                EditText instructorValue = (EditText) findViewById(R.id.diveSummaryEditInstructorValue);
                switch (i) {
                    case 0:
                        instructorText.setVisibility(View.GONE);
                        instructorValue.setVisibility(View.GONE);
                        break;
                    case 1:
                        instructorText.setVisibility(View.VISIBLE);
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
     * @brief
     */
    private void fillSummaryForm() {
        // Duration
        EditText duration = (EditText) findViewById(R.id.diveSummaryEditDurationValue);
        duration.setText(String.valueOf(dive.getDuration()));

        // Max depth
        EditText maxDepth = (EditText) findViewById(R.id.diveSummaryEditMaxDepthValue);
        maxDepth.setText(String.valueOf(dive.getMaxDepth()));

        // Time in
        EditText timeIn = (EditText) findViewById(R.id.diveSummaryEditTimeInValue);
        timeIn.setText(String.valueOf(dive.getTimeIn()));

        // Time out
        EditText timeOut = (EditText) findViewById(R.id.diveSummaryEditTimeOutValue);
        timeOut.setText(String.valueOf(dive.getTimeOut()));
    }



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_summary_edit);

        Bundle bundle = getIntent().getExtras();
        dive = bundle.getParcelable("dive");

        fillForm();

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
     *
     * @param menu
     * @return
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
     * @brief
     */
    private void updateDive() {

        updateGeneral();
        updateConditions();
        updateSummary();
    }



    /**
     * @brief
     */
    private void updateConditions() {
        // Visibility
        EditText visibility = (EditText) findViewById(R.id.diveSummaryEditVisibilityValue);
        if(!visibility.getText().toString().isEmpty())
            dive.setVisibility(Integer.parseInt(visibility.getText().toString()));

        // Bottom temperature
        EditText bottomTemperature = (EditText) findViewById(R.id.diveSummaryEditBottomTemperatureValue);
        if(!bottomTemperature.getText().toString().isEmpty())
            dive.setBottomTemperature(Float.parseFloat(bottomTemperature.getText().toString()));
    }



    /**
     * @brief
     */
    private void updateGeneral() {
        // Date

        // Site
        EditText site = (EditText) findViewById(R.id.diveSummaryEditSiteValue);
        dive.setSite(site.getText().toString());

        // Town/Country
        EditText townCountry = (EditText) findViewById(R.id.diveSummaryEditTownCountryValue);
        dive.setTownCountry(townCountry.getText().toString());

        // Objective
        Spinner objective = (Spinner) findViewById(R.id.diveSummaryEditObjectiveValue);
        int objectiveId = objective.getSelectedItemPosition();
        dive.setObjective(objectiveId);

        // Buddy
        EditText buddy = (EditText) findViewById(R.id.diveSummaryEditBuddyValue);
        dive.setBuddy(buddy.getText().toString());

        // Instructor
        EditText instructor = (EditText) findViewById(R.id.diveSummaryEditInstructorValue);
        dive.setInstructor(instructor.getText().toString());
    }



    /**
     * @brief
     */
    private void updateSummary() {
        // Duration

        // Max depth

        // Time in

        // Time out
    }


}
