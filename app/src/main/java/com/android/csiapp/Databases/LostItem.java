package com.android.csiapp.Databases;

import java.io.Serializable;

/**
 * Created by user on 2016/10/4.
 */
public class LostItem implements Serializable {
    private long id;
    private String mUuid;
    private String mItem_name;
    private String mItem_brand;
    private String mItem_amount;
    private String mItem_value;
    private String mItem_feature;

    public LostItem(){
        this.id = 0;
        this.mUuid = "";
        this.mItem_name = "";
        this.mItem_brand = "";
        this.mItem_amount = "";
        this.mItem_value = "";
        this.mItem_feature = "";
    }

    public LostItem(long id, String uuid, String name, String brand, String amount, String value, String feature) {
        this.id = id;
        this.mUuid = uuid;
        this.mItem_name = name;
        this.mItem_brand = brand;
        this.mItem_amount = amount;
        this.mItem_value = value;
        this.mItem_feature = feature;
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

    public String getItemName() {return mItem_name; }

    public void setItemName(String item_name) {this.mItem_name = item_name; }

    public String getItemBrand() {return mItem_brand; }

    public void setItemBrand(String item_brand) {this.mItem_brand = item_brand; }

    public String getItemAmount() {return mItem_amount; }

    public void setItemAmount(String item_amount) {this.mItem_amount = item_amount; }

    public String getItemValue() {return mItem_value; }

    public void setItemValue(String item_value) {this.mItem_value = item_value; }

    public String getItemFeature() {return mItem_feature; }

    public void setItemFeatue(String item_feature) {this.mItem_feature = item_feature; }

    //Check Information
    public boolean checkInformation(){
        boolean result = false;
        if(!mItem_name.isEmpty()
                &&!mItem_brand.isEmpty()
                &&!mItem_amount.isEmpty()
                &&!mItem_value.isEmpty())
            result = true;
        return result;
    }
}
