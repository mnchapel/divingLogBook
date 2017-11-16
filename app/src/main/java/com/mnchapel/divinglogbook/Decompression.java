package com.mnchapel.divinglogbook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marie-Neige on 14/11/2017.
 */

public class Decompression implements Parcelable {

    //
    private int meter;

    //
    private int minute;



    /**
     * Default constructor
     */
    public Decompression() {

    }



    /**
     * Parcel constructor
     * @param in:
     */
    protected Decompression(Parcel in) {
        meter = in.readInt();
        minute = in.readInt();
    }



    /**
     *
     */
    public static final Creator<Decompression> CREATOR = new Creator<Decompression>() {
        @Override
        public Decompression createFromParcel(Parcel in) {
            return new Decompression(in);
        }

        @Override
        public Decompression[] newArray(int size) {
            return new Decompression[size];
        }
    };



    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }


    public int getMeter() {
        return meter;
    }

    public int getMinute() {
        return minute;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }



    /**
     *
     * @param parcel:
     * @param i:
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(meter);
        parcel.writeInt(minute);
    }
}
