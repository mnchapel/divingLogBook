package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marie-Neige on 17/11/2017.
 */

public class Equipment implements Parcelable {

    /**
     * Type equipment enum
     */
    public enum Type {
        TANK,
        SUIT,
        WEIGHT;
    }

    //
    private Type type;


    /**
     * Default constructor
     */
    public Equipment(Type type) {
        this.type = type;
    }



    /**
     * Parcelable constructor
     * @param in: parcel.
     */
    protected Equipment(Parcel in) {
        type = Type.valueOf(in.readString());
    }



    /**
     * Parcel creator.
     * Parcel creator.
     */
    public static final Creator<Equipment> CREATOR = new Creator<Equipment>() {
        @Override
        public Equipment createFromParcel(Parcel in) {
            return new Equipment(in);
        }

        @Override
        public Equipment[] newArray(int size) {
            return new Equipment[size];
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



    /**
     * Write equipment to parcel.
     * @param parcel: the parcel.
     * @param flags: flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(type.name());
    }



    /**
     * Get type.
     * @return the equipment type.
     */
    public Type getType() {
        return type;
    }
}
