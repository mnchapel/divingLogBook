package com.mnchapel.divinglogbook;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
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
 * Created by Marie-Neige on 22/10/2017.
 */

public class DiveXmlDocument {

    private final static String diveLogDir = "/DivingLogBook/";

    //
    private static final String ns = null;



    private boolean isFileExist(Context context,
                                String filename) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        return file.exists();
    }



    /**
     * @brief Write dive to the internal storage.
     */
    public void writeDive(Context context,
                          Dive dive) throws IOException {
        Date startTimeFilename = dive.getStartTime().getTime();
        SimpleDateFormat dateFormatFilename = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String diveFilename = dateFormatFilename.format(startTimeFilename)+".xml";

        boolean res = isFileExist(context, diveFilename);
        if(res)
            return;

        FileOutputStream fos;

        String state = Environment.getExternalStorageState();
        Boolean test = Environment.MEDIA_MOUNTED.equals(state);

        // External storage
        //fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+diveLogDir+diveFilename);

        // Internal storage
        fos = context.openFileOutput(diveFilename, Context.MODE_PRIVATE);

        XmlSerializer xmlSerializer = Xml.newSerializer();
        xmlSerializer.setOutput(fos, "UTF-8");
        xmlSerializer.startDocument(null, Boolean.valueOf(true));
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        xmlSerializer.startTag(null, "Dive");

        // BottomTemperature
        writeATag(xmlSerializer, "BottomTemperature", dive.getBottomTemperature()+"");

        // Buddy
        writeATag(xmlSerializer, "Buddy", dive.getBuddy());

        // DiveSamples
        xmlSerializer.startTag(null, "DiveSamples");
        List<DiveSample> diveSampleList = dive.getDiveSampleList();
        for(DiveSample diveSample: diveSampleList) {
            xmlSerializer.startTag(null, "Dive.Sample");
            writeATag(xmlSerializer, "Depth", diveSample.getDepth()+"");
            writeATag(xmlSerializer, "Time", diveSample.getTime()+"");
            xmlSerializer.endTag(null, "Dive.Sample");
        }
        xmlSerializer.endTag(null, "DiveSamples");

        // Duration
        writeATag(xmlSerializer, "Duration", dive.getDuration()+"");

        // Instructor
        writeATag(xmlSerializer, "Instructor", dive.getInstructor());

        // Max Depth
        writeATag(xmlSerializer, "MaxDepth", dive.getMaxDepth()+"");

        // Objective
        writeATag(xmlSerializer, "Objective", dive.getObjective()+"");

        // Site
        writeATag(xmlSerializer, "Site", dive.getSite());

        // Start time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date startTime = dive.getStartTime().getTime();
        writeATag(xmlSerializer, "StartTime", dateFormat.format(startTime));

        // Town
        writeATag(xmlSerializer, "Town", dive.getTownCountry());


        xmlSerializer.endTag(null, "Dive");
        xmlSerializer.endDocument();

        xmlSerializer.flush();
        fos.close();

        // Show files in external storage usb
        /*String[] path = {Environment.getExternalStorageDirectory().toString()+"/DivingLogBook/"};
        String[] mimeTypes = {"xml"};
        MediaScannerConnection.scanFile(context, path, mimeTypes,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    // scanned path and uri
                }
        });*/
    }



    private void writeATag(XmlSerializer xmlSerializer,
                           String tag,
                           String value)
            throws IOException {
        xmlSerializer.startTag(null, tag);
        xmlSerializer.text(value);
        xmlSerializer.endTag(null, tag);
    }



    /**
     * @brief Read dive from the internal storage.
     */
    public Dive readDive(Context context,
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


    /**
     * @brief
     * @param parser
     * @param dive
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void readDive(XmlPullParser parser, Dive dive) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Dive");
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();

            if(name.equals("BottomTemperature")) {
                float bottomTemperature = readFloat(parser);
                dive.setBottomTemperature(bottomTemperature);
                Log.d("readDive", bottomTemperature+"");
                parser.next();
            }
            else if(name.equals("Buddy")) {
                String buddy = readString(parser); // second to millisecond
                dive.setBuddy(buddy);
                parser.next();
            }
            else if(name.equals("DiveSamples")) {
                List<DiveSample> diveSampleList = readDiveSample(parser);
                dive.setDiveSampleList(diveSampleList);
                parser.next();
            }
            else if(name.equals("Duration")) {
                int duration = readInt(parser); // second to millisecond
                dive.setDuration(duration);
                parser.next();
            }
            else if(name.equals("Instructor")) {
                String instructor = readString(parser); // second to millisecond
                dive.setInstructor(instructor);
                parser.next();
            }
            else if(name.equals("MaxDepth")) {
                float max_depth = readFloat(parser);
                dive.setMaxDepth(max_depth);
                parser.next();
            }
            else if(name.equals("Objective")) {
                int objective = readInt(parser);
                dive.setObjective(objective);
                parser.next();
            }
            else if(name.equals("Site")) {
                String site = readString(parser);
                dive.setSite(site);
                parser.next();
            }
            else if(name.equals("StartTime")) {
                Date date = readDate(parser);
                Calendar startTime = Calendar.getInstance();
                startTime.setTime(date);
                dive.setDate(startTime);
                parser.next();
            }
            else if(name.equals("Town")) {
                String town = readString(parser);
                dive.setTownCountry(town);
                parser.next();
            }
            else {
                skip(parser);
            }
        }
    }



    /**
     * @brief Read dive samples.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private List<DiveSample> readDiveSample(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<DiveSample> diveSampleList = new ArrayList<DiveSample>();

        parser.require(XmlPullParser.START_TAG, ns, "DiveSamples");
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();

            if (name.equals("Dive.Sample")) {
                DiveSample diveSample = readOneDiveSample(parser);
                diveSampleList.add(diveSample);
                parser.next();
            }
            else {
                skip(parser);
            }
        }

        return diveSampleList;
    }



    /**
     * @brief Read one dive sample.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private DiveSample readOneDiveSample(XmlPullParser parser) throws IOException, XmlPullParserException {
        DiveSample diveSample = new DiveSample();

        parser.require(XmlPullParser.START_TAG, ns, "Dive.Sample");
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();

            if (name.equals("Depth")) {
                float depth = readFloat(parser);
                diveSample.setDepth(depth);
                parser.next();
            }
            else if(name.equals("Time")) {
                int time = readInt(parser);
                Log.d("parser diveSample time", time+"");
                diveSample.setTime(time);
                parser.next();
            }
            else {
                skip(parser);
            }
        }

        return diveSample;
    }



    /**
     * @brief Read date format.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String date_str = parser.getText();
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
     * @brief Read float format.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private float readFloat(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String floatValue = parser.getText();
            return Float.parseFloat(floatValue);
        }
        return 0.0f;
    }



    /**
     * @brief Read int format.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private int readInt(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String intValue = parser.getText();
            return Integer.parseInt(intValue);
        }
        return 0;
    }



    /**
     * @brief Read String format.
     * @param parser:
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readString(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.next() == XmlPullParser.TEXT) {
            String stringValue = parser.getText();
            return stringValue;
        }
        return "";
    }



    /**
     * @brief Skip XML tag.
     * @param parser:
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
