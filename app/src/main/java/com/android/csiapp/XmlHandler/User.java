package com.android.csiapp.XmlHandler;

/**
 * Created by JOWE on 2016/10/18.
 */

public class User {
    private int id;
    private String mLoginName; // 登錄名
    private String mPassword;  // 密碼
    private String mUserName;  // 用戶姓名
    private String mUnitCode;  // 用戶單位代碼
    private String mUnitName;  // 用戶單位
    private String mIdCardNo;  // 用戶身分證號
    private String mContact;   // 用戶聯絡方式
    private String mDuty;      // 用戶職務

    public User(){
        this.id = 0;
        this.mLoginName = "";
        this.mPassword = "";
        this.mUserName = "";
        this.mUnitCode = "";
        this.mUnitName = "";
        this.mIdCardNo = "";
        this.mContact = "";
        this.mDuty = "";
    }

    public void setId(int id) {
        this.id=id;
    }

    public void setLoginName(String loginName) {
        this.mLoginName = loginName;
    }

    public void setPassword(String password) {this.mPassword = password;}

    public void setUserName(String userName) {this.mUserName =  userName; }

    public void setUnitCode(String unitCode) {
        this.mUnitCode =  unitCode;
    }

    public void setUnitName(String unitName) {
        this.mUnitName =  unitName;
    }

    public void setIdCardNo(String idCardNo) {
        this.mIdCardNo =  idCardNo;
    }

    public void setContact(String contact) {
        this.mContact =  contact;
    }

    public void setDuty(String duty) {
        this.mDuty =  duty;
    }

    public int getId() {
        return id;
    }

    public String getLoginName() { return mLoginName;}

    public String getPassword() { return mPassword;}

    public String getUserName() { return mUserName;}

    public String getUnitCode() { return mUnitCode;}

    public String getUnitName() { return mUnitName;}

    public String getIdCardNo() { return mIdCardNo;}

    public String getContact() { return mContact;}

    public String getDuty() { return mDuty;}

    @Override
    public String toString()     {
        return "Dictionary [LoginName="+ mLoginName + ", Password="+mPassword
                + "UserName ="+mUserName+", UnitCode ="+mUnitCode
                +", UnitName="+mUnitName+", IdCardNo=" +mIdCardNo
                +", Contact"+mContact+", Duty="+mDuty+"]";
    }
}

