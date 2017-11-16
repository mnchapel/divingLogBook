package com.mnchapel.divinglogbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;


public class DiveChartFragment extends Fragment {

    /**
     * Constructor
     */
    public DiveChartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dive_chart, container, false);


        HomeMenuActivity activity = (HomeMenuActivity) getActivity();
        int diveKey = getArguments().getInt("diveKey");
        Dive dive = activity.getDive(diveKey);
        List<DiveSample> diveSampleList = dive.getDiveSampleList();

        LineGraph lineGraph = (LineGraph) view.findViewById(R.id.diveChartLineGraph);
        lineGraph.setDiveSample(diveSampleList);

        // initialize our XYPlot reference:
        XYPlot plot = (XYPlot) view.findViewById(R.id.plot);

        // remove the grid
        //plot.getGraph().getDomainGridLinePaint().setColor(getResources().getColor(R.color.grey));
        plot.getGraph().getRangeGridLinePaint().setColor(Color.TRANSPARENT);

        plot.getGraph().setPaddingTop(10);

        // remove the legend
        plot.getLayoutManager().remove(plot.getLegend());

        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 2);
        plot.setRangeLowerBoundary(-20, BoundaryMode.FIXED);

        plot.setDomainStepValue(10);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.TOP).setFormat(new GraphXLabelFormat());

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new GraphYLabelFormat());

        /*Widget gw = plot.getGraph();

        // FILL mode with values of 0 means fill 100% of container:
        SizeMetric width = new SizeMetric(0, SizeMode.FILL);
        SizeMetric height = new SizeMetric(0, SizeMode.FILL);
        Size sm = new Size(width, height);
        gw.setSize(sm);

        // get a handle to the layout manager:
        LayoutManager lm = plot.getLayoutManager();

        // position the upper left corner of gw in the upper left corner of the space
        // controlled by lm.
        gw.position(0, HorizontalPositioning.ABSOLUTE_FROM_LEFT, 0, VerticalPositioning.ABSOLUTE_FROM_TOP);*/



        // create a couple arrays of y-values to plot:
        Number[] series1Numbers = new Number[diveSampleList.size()];
        Log.i("nb dive sample", diveSampleList.size()+"");

        for(int i=0; i<diveSampleList.size(); i++)
            series1Numbers[i] = diveSampleList.get(i).getDepth()*-1;

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.BLACK, null, Color.WHITE, null);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        // Inflate the layout for this fragment
        return view;
    }
}





class GraphXLabelFormat extends Format {

    //
    private String[] xLabels = {"a","b","c"};

    @Override
    public StringBuffer format(Object arg0, StringBuffer arg1, FieldPosition arg2) {


        int parsedInt = Math.round(Float.parseFloat(arg0.toString()))*20/114;

        if(parsedInt == 0)
            return arg1;

        int diveTime = parsedInt*10;
        Log.i("GraphXLabelFormat ", parsedInt + " " + arg1 + " " + arg2);
        String labelString = diveTime+"'";//xLabels[parsedInt];
        arg1.append(labelString);


        return arg1;
    }



    @Override
    public Object parseObject(String arg0, ParsePosition arg1) {
        return java.util.Arrays.asList(xLabels).indexOf(arg0);
    }
}





class GraphYLabelFormat extends Format {

    private String[] xLabels = {"a","b","c"};

    @Override
    public StringBuffer format(Object arg0, StringBuffer arg1, FieldPosition arg2) {

        int parsedInt = Math.round(Float.parseFloat(arg0.toString()));

        if(parsedInt == 0)
            return arg1;

        int diveDepth = parsedInt*-1;
        Log.i("GraphYLabelFormat ", parsedInt + " " + arg1 + " " + arg2);
        String labelString = diveDepth+"'";
        arg1.append(labelString);


        return arg1;
    }



    @Override
    public Object parseObject(String arg0, ParsePosition arg1) {
        return java.util.Arrays.asList(xLabels).indexOf(arg0);
    }
}

