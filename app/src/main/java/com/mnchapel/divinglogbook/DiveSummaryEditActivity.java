package com.mnchapel.divinglogbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DiveSummaryEditActivity extends AppCompatActivity {

    Dive dive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_summary_edit);

        Bundle bundle = getIntent().getExtras();
        dive = bundle.getParcelable("dive");

        // Date
        TextView date = (TextView) findViewById(R.id.diveSummaryDateValue);
        date.setText(dive.getDate());
    }

}
