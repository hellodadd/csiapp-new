package com.android.csiapp.Databases;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by user on 2016/10/4.
 */
public class WitnessItem implements Serializable {
    private long id;
    private String mUuid;
    private String mWitness_name;
    private String mWitness_sex;
    private long mWitness_birthday;
    private String mWitness_number;
    private String mWitness_address;
    private String mPhotoPath;

    public WitnessItem(){
        Calendar c = Calendar.getInstance();
        long time = c.getTimeInMillis();
        this.id = 0;
        this.mUuid = "";
        this.mWitness_name = "";
        this.mWitness_sex = "";
        this.mWitness_birthday = time;
        this.mWitness_number = "";
        this.mWitness_address = "";
        this.mPhotoPath = "";
    }

    public WitnessItem(long id, String uuid, String name, String sex, long birthday, String number, String address) {
        this.id = id;
        this.mUuid = uuid;
        this.mWitness_name = name;
        this.mWitness_sex = sex;
        this.mWitness_birthday = birthday;
        this.mWitness_number = number;
        this.mWitness_address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        this.mUuid = uuid;
    }

    public String getWitnessName() {return mWitness_name; }

    public void setWitnessName(String witness_name) {this.mWitness_name = witness_name; }

    public String getWitnessSex() {return mWitness_sex; }

    public void setWitnessSex(String witness_sex) {this.mWitness_sex = witness_sex; }

    public long getWitnessBirthday() {return mWitness_birthday; }

    public void setWitnessBirthday(long witness_birthday) {this.mWitness_birthday = witness_birthday; }

    public String getWitnessNumber() {return mWitness_number; }

    public void setWitnessNumber(String witness_number) {this.mWitness_number = witness_number; }

    public String getWitnessAddress() {return mWitness_address; }

    public void setWitnessAddress(String witness_address) {this.mWitness_address = witness_address; }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.mPhotoPath = photoPath;
    }

    //Check Information
    public boolean checkInformation(){
        boolean result = false;
        if(!mWitness_name.isEmpty())
            result = true;
        return result;
    }
}
