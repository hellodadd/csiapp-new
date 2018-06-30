package com.android.csiapp.XmlHandler;

/**
 * Created by JOWE on 2016/10/18.
 */

public class Dictionary {
    private int id;
    private String mDictKey;
    private String mParentKey;
    private String mRootKey;
    private String mDictValue;
    private String mDictRemark;
    private String mDictSpell;

    public Dictionary(){
        this.id = 0;
        this.mDictKey = "";
        this.mParentKey = "";
        this.mRootKey = "";
        this.mDictValue = "";
        this.mDictRemark = "";
        this.mDictSpell = "";
    }

    public int getId() {
        return id;
    }

    public String getDictKey() { return mDictKey;}

    public String getParentKey() { return mParentKey;}

    public String getRootKey() { return mRootKey;}

    public String getDictValue() { return mDictValue;}

    public String getDictRemark() { return mDictRemark;}

    public String getDictSpell() { return mDictSpell;}

    public void setId(int id) {
        this.id=id;
    }

    public void setDictKey(String dictKey) {
        this.mDictKey = dictKey;
    }

    public void setParentKey(String parentKey) {
        this.mParentKey = parentKey;
    }

    public void setRootKey(String rootKey) {
        this.mRootKey =  rootKey;
    }

    public void setDictValue(String dictValue) {
        this.mDictValue =  dictValue;
    }

    public void setDictRemark(String dictRemark) {
        this.mDictRemark =  dictRemark;
    }

    public void setDictSpell(String dictSpell) {
        this.mDictSpell =  dictSpell;
    }


    @Override
    public String toString() {
        return "Dictionary [dictkey ="+mDictKey+", parentkey ="+mParentKey
                +", rootkey="+mRootKey+", mDictValue=" +mDictValue
                +", mDictRemark"+mDictRemark+", mDictSpell="+mDictSpell+"]";
    }
}
