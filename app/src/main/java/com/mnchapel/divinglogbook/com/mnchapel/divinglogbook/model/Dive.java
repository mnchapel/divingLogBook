package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Marie-Neige on 11/08/2017.
 */

public class Dive implements Parcelable, Comparable<Dive> {

    //
    private float bottomTemperature;

    //
    private List<String> buddy;

    //
    private String country;

    //
    private List<Decompression> decompressionList;

    //
    private List<DiveSample> diveSampleList;

    // Duration in second
    private int duration;

    //
    private String instructor;

    //
    private float maxDepth;

    //
    private int objective;

    //
    private int sampleInterval;

    //
    private String site;

    // Date + Time in
    private Calendar startTime;

    //
    private String town;

    //
    private float visibility;



    /**
     * Default constructor
     */
    public Dive() {
        bottomTemperature = 0.f;
        buddy = new ArrayList<>();
        country = "";
        decompressionList = new ArrayList<>();
        diveSampleList = new ArrayList<>();
        duration = 0;
        instructor = "";
        maxDepth = 0.f;
        objective = 0;
        sampleInterval = 0;
        site = "";
        startTime = Calendar.getInstance();
        town = "";
        visibility = -1;
    }



    /**
     * Constructor
     *
     * @param in: the parcel.
     */
    protected Dive(Parcel in) {
        bottomTemperature = in.readFloat();
        buddy = in.createStringArrayList();
        country = in.readString();
        decompressionList = in.createTypedArrayList(Decompression.CREATOR);
        diveSampleList = in.createTypedArrayList(DiveSample.CREATOR);
        duration = in.readInt();
        instructor = in.readString();
        maxDepth = in.readFloat();
        objective = in.readInt();
        sampleInterval = in.readInt();
        site = in.readString();
        startTime = (Calendar) in.readSerializable();
        town = in.readString();
        visibility = in.readFloat();
    }



    /**
     *
     * @param dive:
     * @return 0 if dives are equals
     */
    @Override
    public int compareTo(@NonNull Dive dive) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String thisDive = dateFormat.format(startTime.getTime());
        String otherDive = dateFormat.format(dive.getStartTime().getTime());

        return thisDive.compareTo(otherDive)*-1;
    }



    /**
     * Creator
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
     *
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return false;
        if(!(obj instanceof Dive))
            return false;

        Dive other = (Dive) obj;
        boolean res;
        if(!startTime.equals(other.getStartTime()))
            res = false;
        else
            res = true;

        return res;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        return result;
    }



    // GETTER---------------------------------------------------------------------------------------

    public float getBottomTemperature() {
        return bottomTemperature;
    }

    public List<String> getBuddy() {
        return buddy;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        Date date = startTime.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
        return dateFormat.format(date);
    }

    public List<Decompression> getDecompressionList() {
        return decompressionList;
    }

    public List<DiveSample> getDiveSampleList() {
        return diveSampleList;
    }

    public int getDuration() {
        return duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getObjective() {
        return objective;
    }

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

    public String getSite() {
        return site;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public String getTown() {
        return town;
    }

    public float getVisibility() {
        return visibility;
    }



    // SETTER --------------------------------------------------------------------------------------

    public void setBuddy(List<String> buddyList) {
        this.buddy = buddyList;
    }

    public void setBottomTemperature(float bottomTemperature) {
        this.bottomTemperature = bottomTemperature;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDate(Calendar startTime)
    {
        this.startTime = (Calendar) startTime.clone();
    }

    public void setDecompressionList(List<Decompression> decompressionList) {
        this.decompressionList = decompressionList;
    }

    public void setDiveSampleList(List<DiveSample> diveSampleList) {
        this.diveSampleList = diveSampleList;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setObjective(int objective) {
        this.objective = objective;
    }

    public void setMaxDepth(float maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }



    /**
     * Write object to parcel.
     *
     * @param parcel: the parcel.
     * @param i:
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(bottomTemperature);
        parcel.writeStringList(buddy);
        parcel.writeString(country);
        parcel.writeTypedList(decompressionList);
        parcel.writeTypedList(diveSampleList);
        parcel.writeInt(duration);
        parcel.writeString(instructor);
        parcel.writeFloat(maxDepth);
        parcel.writeInt(objective);
        parcel.writeInt(sampleInterval);
        parcel.writeString(site);
        parcel.writeSerializable(startTime);
        parcel.writeString(town);
        parcel.writeFloat(visibility);
    }
}
