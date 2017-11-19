package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model;

import android.os.Parcel;

/**
 * Created by Marie-Neige on 17/11/2017.
 */

public class TankEquipment extends Equipment {

    public enum Material {
        ALUMINUM,
        STEEL;
    }

    public enum Mixture {
        AIR,
        NITROX,
        TRIMIX,
        HELIOX,
        OXYGEN;
    }

    public enum PressureUnit {
        BAR,
        PSI;
    }

    public enum Type {
        SIMPLE,
        TWIN;
    }

    public enum VolumeUnit {
        L;
    }

    public enum WeightUnit {
        KG;
    }



    private int endPressure;
    private Material material;
    private Mixture mixture;
    private PressureUnit pressureUnit;
    private int startPressure;
    private float volume;
    private VolumeUnit volumeUnit;
    private float weight;
    private WeightUnit weightUnit;



    /**
     * Default constructor
     */
    public TankEquipment() {
        super(Equipment.Type.TANK);
        endPressure = 50;
        material = Material.STEEL;
        mixture = Mixture.AIR;
        pressureUnit = PressureUnit.BAR;
        startPressure = 200;
        volume = 12;
        volumeUnit = VolumeUnit.L;
        weight = 15;
        weightUnit = WeightUnit.KG;
    }



    /**
     * Parcelable constructor.
     * @param in: the parcel.
     */
    protected TankEquipment(Parcel in) {
        super(in);
        endPressure = in.readInt();
        material = Material.valueOf(in.readString());
        mixture = Mixture.valueOf(in.readString());
        pressureUnit = PressureUnit.valueOf(in.readString());
        startPressure = in.readInt();
        volume = in.readFloat();
        volumeUnit = VolumeUnit.valueOf(in.readString());
        weight = in.readFloat();
        weightUnit = WeightUnit.valueOf(in.readString());
    }



    /**
     * Parcel creator.
     */
    public static final Creator<TankEquipment> CREATOR = new Creator<TankEquipment>() {
        @Override
        public TankEquipment createFromParcel(Parcel in) {
            return new TankEquipment(in);
        }

        @Override
        public TankEquipment[] newArray(int size) {
            return new TankEquipment[size];
        }
    };


    // GETTER --------------------------------------------------------------------------------------
    public int getEndPressure() {
        return endPressure;
    }

    public Material getMaterial() {
        return material;
    }

    public Mixture getMixture() {
        return mixture;
    }

    public PressureUnit getPressureUnit() {
        return pressureUnit;
    }

    public int getStartPressure() {
        return startPressure;
    }

    public float getVolume() {
        return volume;
    }

    public VolumeUnit getVolumeUnit() {
        return volumeUnit;
    }

    public float getWeight() {
        return weight;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }



    // SETTER --------------------------------------------------------------------------------------

    public void setEndPressure(int endPressure) {
        this.endPressure = endPressure;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setMixture(Mixture mixture) {
        this.mixture = mixture;
    }

    public void setPressureUnit(PressureUnit pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public void setStartPressure(int startPressure) {
        this.startPressure = startPressure;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setVolumeUnit(VolumeUnit volumeUnit) {
        this.volumeUnit = volumeUnit;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }



    /**
     * Write equipment to parcel.
     * @param parcel: the parcel.
     * @param flags: flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeInt(endPressure);
        parcel.writeString(material.name());
        parcel.writeString(mixture.name());
        parcel.writeString(pressureUnit.name());
        parcel.writeInt(startPressure);
        parcel.writeFloat(volume);
        parcel.writeString(volumeUnit.name());
        parcel.writeFloat(weight);
        parcel.writeString(weightUnit.name());
    }
}
