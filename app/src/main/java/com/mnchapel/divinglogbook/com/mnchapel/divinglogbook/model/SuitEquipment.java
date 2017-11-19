package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model;

import android.os.Parcel;

/**
 * Created by Marie-Neige on 17/11/2017.
 */

public class SuitEquipment extends Equipment {



    /**
     * Default constructor
     */
    public SuitEquipment() {
        super(Type.SUIT);
    }



    /**
     * Parcelable constructor
     * @param in: the parcel.
     */
    protected SuitEquipment(Parcel in) {
        super(in);
    }



    /**
     * Parcel creator.
     */
    public static final Creator<SuitEquipment> CREATOR = new Creator<SuitEquipment>() {
        @Override
        public SuitEquipment createFromParcel(Parcel in) {
            return new SuitEquipment(in);
        }

        @Override
        public SuitEquipment[] newArray(int size) {
            return new SuitEquipment[size];
        }
    };



    /**
     * Write equipment to parcel.
     * @param parcel: the parcel.
     * @param flags: flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
    }
}
