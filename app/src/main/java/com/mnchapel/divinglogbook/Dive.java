package com.mnchapel.divinglogbook;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Marie-Neige on 11/08/2017.
 */

public class Dive implements Parcelable, Comparable<Dive> {

    //
    private float bottomTemperature;

    // Date + Time in
    private Calendar startTime;

    //
    private List<DiveSample> diveSampleList;

    // Duration in second
    private int duration;

    //
    private String objective;

    //
    private float maxDepth;

    //
    private int sampleInterval;



    /**
     * @brief Default constructor
     */
    public Dive() {}



    /**
     * @brief Constructor
     *
     * @param in
     */
    protected Dive(Parcel in) {
        bottomTemperature = in.readFloat();
        duration = in.readInt();
        maxDepth = in.readFloat();
        sampleInterval = in.readInt();
        // TODO add calendar
    }



    /**
     *
     * @param dive
     * @return
     */
    @Override
    public int compareTo(@NonNull Dive dive) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String thisDive = dateFormat.format(startTime.getTime());
        String otherDive = dateFormat.format(dive.getStartTime().getTime());

        return thisDive.compareTo(otherDive)*-1;
    }



    /**
     * @brief
     */
    public static final Creator<Dive> CREATOR = new Creator<Dive>() {
        @Override
        public Dive createFromParcel(Parcel in) {
            return new Dive(in);
        }

        @Override
        public Dive[] newArray(int size) {
            return new Dive[size];
        }
    };



    /**
     * @brief
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }



    // GETTER---------------------------------------------------------------------------------------

    public float getBottomTemperature() {
        return bottomTemperature;
    }

    public String getDate() {
        Date date = startTime.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
        return dateFormat.format(date);
    }

    public List<DiveSample> getDiveSampleList() {
        return diveSampleList;
    }

    public int getDuration() {
        return duration;
    }

    public String getObjective() { return objective; }

    public String getTimeIn() {
        Date date = startTime.getTime();
        SimpleDateFormat timeInFormat = new SimpleDateFormat("HH:mm");
        return timeInFormat.format(date);
    }

    public String getTimeOut() {
        Calendar calendar = (Calendar) startTime.clone();
        calendar.add(Calendar.SECOND, duration);
        Date date = calendar.getTime();
        SimpleDateFormat timeOutFormat = new SimpleDateFormat("HH:mm");
        return timeOutFormat.format(date);
    }

    public float getMaxDepth() {
        return maxDepth;
    }

    public int getSampleInterval() {
        return sampleInterval;
    }

    public Calendar getStartTime() {
        return startTime;
    }



    // SETTER --------------------------------------------------------------------------------------

    public void setDate(Calendar startTime) { this.startTime = (Calendar) startTime.clone(); }

    public void setBottomTemperature(float bottomTemperature) {
        this.bottomTemperature = bottomTemperature;
    }

    public void setDiveSampleList(List<DiveSample> diveSampleList) {
        this.diveSampleList = diveSampleList;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setObjective(String objective) { this.objective = objective; }

    public void setMaxDepth(float maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
    }


    /**
     * @brief
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(bottomTemperature);
        parcel.writeInt(duration);
        parcel.writeFloat(maxDepth);
        parcel.writeInt(sampleInterval);
        // TODO add calendar
    }
}
