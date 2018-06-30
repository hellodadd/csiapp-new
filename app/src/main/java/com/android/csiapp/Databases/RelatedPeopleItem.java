package com.android.csiapp.Databases;

import java.io.Serializable;

/**
 * Created by user on 2016/10/4.
 */
public class RelatedPeopleItem implements Serializable {
    private long id;
    private String mUuid;
    private String mPeople_relation;
    private String mPeople_name;
    private String mPeople_sex;
    private String mPeople_id;
    private String mPeople_number;
    private String mPeople_address;

    public RelatedPeopleItem(){
        this.id = 0;
        this.mUuid = "";
        this.mPeople_relation = "";
        this.mPeople_name = "";
        this.mPeople_sex = "";
        this.mPeople_id = "";
        this.mPeople_number = "";
        this.mPeople_address = "";
    }

    public RelatedPeopleItem(long id, String uuid, String releation, String name, String sex, String identity, String number, String address) {
        this.id = id;
        this.mUuid = uuid;
        this.mPeople_relation = releation;
        this.mPeople_name = name;
        this.mPeople_sex = sex;
        this.mPeople_id = identity;
        this.mPeople_number = number;
        this.mPeople_address = address;
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

    public String getPeopleRelation() {return mPeople_relation; }

    public void setPeopleRelation(String people_relation) {this.mPeople_relation = people_relation; }

    public String getPeopleName() {return mPeople_name; }

    public void setPeopleName(String people_name) {this.mPeople_name = people_name; }

    public String getPeopleSex() {return mPeople_sex; }

    public void setPeopleSex(String people_sex) {this.mPeople_sex = people_sex; }

    public String getPeopleId() {return mPeople_id; }

    public void setPeopleId(String people_id) {this.mPeople_id = people_id; }

    public String getPeopleNumber() {return mPeople_number; }

    public void setPeopleNumber(String people_number) {this.mPeople_number = people_number; }

    public String getPeopleAddress() {return mPeople_address; }

    public void setPeopleAddress(String people_address) {this.mPeople_address = people_address; }

    //Check Information
    public boolean checkInformation(){
        boolean result = false;
        if(!mPeople_name.isEmpty()
                &&!mPeople_number.isEmpty()
                &&!mPeople_address.isEmpty())
            result = true;
        return result;
    }
}
