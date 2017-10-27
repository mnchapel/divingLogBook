package com.mnchapel.divinglogbook;


import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Upload Dive class
 */
public class UploadDiveActivity {

    private final static String diveLogDir = "/DivingLogBook/";

    private List<Dive> uploadDiveList;



    /**
     * @brief Enum of computer model.
     */
    public enum ComputerModel {
        SUUNTO_VYPER("SuuntoVyper"),
        NON_CM("NonCM");

        private final String cm;

        /**
         * Constructor
         * @param cm: the model of the computer.
         */
        private ComputerModel(String cm){
            this.cm = cm;
        }



        public static ComputerModel fromString(String value) {
            for (ComputerModel fname : values()) {
                if (fname.cm.equals(value)) {
                    return fname;
                }
            }
            return NON_CM;
        }


        @Override
        public String toString() {
            return cm;
        }
    }



    /**
     * @brief Upload dive from the USB external storage.
     */
    public void uploadDive(Context context,
                           List<Dive> diveList) throws IOException, XmlPullParserException {
        uploadDiveList = new ArrayList<Dive>();

        loadDive(context);

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, uploadDiveList.size()+" new dives loaded.", duration);
        toast.show();

        // TODO : check if a dive exists before save it on internal storage.
        saveDive(context);
    }



    /**
     * @brief Load dive from the USB external storage in the dive list.
     */
    private void loadDive(Context context) throws IOException, XmlPullParserException {

        File rootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+diveLogDir);
        if(rootDir.isDirectory()) {
            String[] files = rootDir.list();

            for(String directory: files) {
                ComputerModelXmlParser cmXmlParser;

                switch (ComputerModel.fromString(directory)) {
                    case SUUNTO_VYPER:
                        cmXmlParser = new SuuntoViperXmlParser();
                        File cmDirectory = new File(rootDir.getAbsolutePath() + "/" + ComputerModel.SUUNTO_VYPER.toString());
                        loadDiveFile(context, cmXmlParser, cmDirectory);
                    default:
                        break;
                }
            }
        }
    }



    /**
     * @brief Load dive from the USB external storage in the dive list.
     */
    private void loadDiveFile(Context context,
                              ComputerModelXmlParser cmXmlParser,
                              File cmDirectory)
            throws IOException, XmlPullParserException {

        if(cmDirectory.isDirectory()) {
            String[] files = cmDirectory.list();

            for (String diveFile : files) {
                InputStream is = new FileInputStream(cmDirectory.getAbsolutePath()+"/"+diveFile);
                Dive dive = cmXmlParser.parse(context, is);
                uploadDiveList.add(dive);
            }
        }
    }



    /**
     * @brief Save dives from the dive list to the internal storage.
     */
    private void saveDive(Context context) throws IOException, XmlPullParserException {

        for(Dive dive: uploadDiveList) {
            DiveXmlDocument diveXmlDocument = new DiveXmlDocument();

            diveXmlDocument.writeDive(context, dive);
        }


    }

}
