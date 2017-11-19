package com.mnchapel.divinglogbook;

import android.content.Context;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.Dive;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Marie-Neige on 23/10/2017.
 */

public abstract class ComputerModelXmlParser {

    public abstract Dive parse(Context context, InputStream is) throws XmlPullParserException, IOException;
}
