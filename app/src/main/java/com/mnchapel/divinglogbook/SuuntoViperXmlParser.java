package com.mnchapel.divinglogbook;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Marie-Neige on 13/08/2017.
 */

public class SuuntoViperXmlParser extends ComputerModelXmlParser {

    //
    private static final String ns = null;



    public Dive parse(Context context,
                      InputStream is) throws XmlPullParserException, IOException {
        try {
            Dive dive = new Dive();
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            readDive(parser, dive);

            return dive;
        }
        finally {
            is.close();
        }
    }



    private void readDive(XmlPullParser parser, Dive dive) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Dive");
        while(parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            Log.d("parser", name);

            if(name.equals("BottomTemperature")) {
                int bottomTemperature = readInt(parser);
                dive.setBottomTemperature(bottomTemperature);
                Log.d("readDive", bottomTemperature+"");
                parser.nextTag();
            }
            else if(name.equals("DiveSamples")) {
                List<DiveSample> diveSampleList = readDiveSample(parser);
                dive.setDiveSampleList(diveSampleList);
            }
            else if(name.equals("Duration")) {
                int duration = readInt(parser);
                dive.setDuration(duration);
                parser.nextTag();
            }
            else if(name.equals("MaxDepth")) {
                float max_depth = readFloat(parser);
                dive.setMaxDepth(max_depth);
                parser.nextTag();
            }
            else if(name.equals("StartTime")) {
                Date date = readDate(parser);
                Calendar startTime = Calendar.getInstance();
                startTime.setTime(date);
                dive.setDate(startTime);
                parser.nextTag();
            }
            else if(name.equals("SampleInterval")) {
                int sampleInterval = readInt(parser);
                dive.setSampleInterval(sampleInterval);
                parser.nextTag();
            }
            else {
                skip(parser);
            }
        }
    }


    private List<DiveSample> readDiveSample(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<DiveSample> diveSampleList = new ArrayList<DiveSample>();

        parser.require(XmlPullParser.START_TAG, ns, "DiveSamples");
        while(parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            Log.d("parser", name);

            if (name.equals("Dive.Sample")) {
                DiveSample diveSample = readOneDiveSample(parser);
                diveSampleList.add(diveSample);
            }
            else {
                skip(parser);
            }
        }

        return diveSampleList;
    }


    private DiveSample readOneDiveSample(XmlPullParser parser) throws IOException, XmlPullParserException {
        DiveSample diveSample = new DiveSample();

        parser.require(XmlPullParser.START_TAG, ns, "Dive.Sample");
        while(parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            Log.d("parser", name);

            if (name.equals("Depth")) {
                float depth = readFloat(parser);
                diveSample.setDepth(depth);
                parser.nextTag();
            }
            else if(name.equals("Time")) {
                int time = readInt(parser);
                diveSample.setTime(time);
                parser.nextTag();
            }
            else {
                skip(parser);
            }
        }

        return diveSample;
    }



    private float readFloat(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String floatValue = parser.getText();
            return Float.parseFloat(floatValue);
        }
        return 0.0f;
    }



    private int readInt(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String intValue = parser.getText();
            return Integer.parseInt(intValue);
        }
        return 0;
    }



    private Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String date_str = parser.getText();
            Log.d("test", date_str);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                return format.parse(date_str);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }
        else
            return new Date();
    }



    /**
     *
     * @param parser
     * @throws IOException
     * @throws IOException
     * @throws XmlPullParserException
     */
    private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
