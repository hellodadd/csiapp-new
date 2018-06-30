package com.android.csiapp.XmlHandler;

/**
 * Created by JOWE on 2016/9/29.
 */

public class Device {
    private String mDeviceId;
    private String mInitStatus;
    private String mSwVersion;
    private String mMapVersion;

    public String[] getDeviceId() {
        String[] deviceid = new String[2];
        deviceid[0] = "deviceId";
        deviceid[1]= mDeviceId;
        return deviceid;
    }

    public String[] getInitStatus() {
        String[] deviceid = new String[2];
        deviceid[0] = "init";
        deviceid[1]= mInitStatus;
        return deviceid;
    }

    public String[] getSwVersion() {
        String[] deviceid = new String[2];
        deviceid[0] = "swVersion";
        deviceid[1]= mSwVersion;
        return deviceid;
    }

    public String[] getMapVersion() {
        String[] deviceid = new String[2];
        deviceid[0] = "mapVersion";
        deviceid[1]= mMapVersion;
        return deviceid;
    }

    public void setDeviceId(String deviceid) {
        this.mDeviceId = deviceid;
    }

    public void setInitStatus(String initstatus) {
        this.mInitStatus = initstatus;
    }

    public void setSwVersion(String swversion) {
        this.mSwVersion = swversion;
    }

    public void setMapVersion(String mapversion) {
        this.mMapVersion = mapversion;
    }
}
