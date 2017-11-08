package com.mnchapel.divinglogbook;

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
    private String buddy;

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
    private String townCountry;

    //
    private int visibility;



    /**
     * @brief Default constructor
     */
    public Dive() {
        bottomTemperature = 0.f;
        buddy = "";
        diveSampleList = new ArrayList<>();
        duration = 0;
        instructor = "";
        maxDepth = 0.f;
        objective = 0;
        sampleInterval = 0;
        site = "";
        startTime = Calendar.getInstance();
        townCountry = "";
        visibility = -1;
    }



    /**
     * @brief Constructor
     *
     * @param in: the parcel.
     */
    protected Dive(Parcel in) {
        bottomTemperature = in.readFloat();
        buddy = in.readString();
        diveSampleList = in.createTypedArrayList(DiveSample.CREATOR);
        duration = in.readInt();
        instructor = in.readString();
        maxDepth = in.readFloat();
        objective = in.readInt();
        sampleInterval = in.readInt();
        site = in.readString();
        startTime = (Calendar) in.readSerializable();
        townCountry = in.readString();
        visibility = in.readInt();
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

    public String getBuddy() {
        return buddy;
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

    public String getTownCountry() {
        return townCountry;
    }

    public int getVisibility() {
        return visibility;
    }



    // SETTER --------------------------------------------------------------------------------------

    public void setDate(Calendar startTime) { this.startTime = (Calendar) startTime.clone(); }

    public void setBuddy(String buddy) {
        this.buddy = buddy;
    }

    public void setBottomTemperature(float bottomTemperature) {
        this.bottomTemperature = bottomTemperature;
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

    public void setTownCountry(String townCountry) {
        this.townCountry = townCountry;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }



    /**
     * @brief Write object to parcel.
     *
     * @param parcel: the parcel.
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(bottomTemperature);
        parcel.writeString(buddy);
        parcel.writeTypedList(diveSampleList);
        parcel.writeInt(duration);
        parcel.writeString(instructor);
        parcel.writeFloat(maxDepth);
        parcel.writeInt(objective);
        parcel.writeInt(sampleInterval);
        parcel.writeString(site);
        parcel.writeSerializable(startTime);
        parcel.writeString(townCountry);
        parcel.writeInt(visibility);
    }
}
