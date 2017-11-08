package com.mnchapel.divinglogbook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marie-Neige on 15/08/2017.
 */

public class DiveSample implements Parcelable {

    // The depth of the sample.
    float depth;

    // The time of the sample.
    int time;



    /**
     * @brief Default constructor
     */
    public DiveSample() {}



    /**
     * @brief Constructor for parcelable object.
     *
     * @param in: the parcel.
     */
    protected DiveSample(Parcel in) {
        depth = in.readFloat();
        time = in.readInt();
    }



    /**
     * @brief Creator for parcelable.
     */
    public static final Creator<DiveSample> CREATOR = new Creator<DiveSample>() {
        @Override
        public DiveSample createFromParcel(Parcel in) {
            return new DiveSample(in);
        }

        @Override
        public DiveSample[] newArray(int size) {
            return new DiveSample[size];
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



    // GETTER --------------------------------------------------------------------------------------

    public float getDepth() {
        return depth;
    }

    public int getTime() {
        return time;
    }



    // SETTER --------------------------------------------------------------------------------------

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public void setTime(int time) {
        this.time = time;
    }



    /**
     * @brief Write the object to parcel.
     *
     * @param parcel: the parcel.
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(depth);
        parcel.writeInt(time);
    }
}
