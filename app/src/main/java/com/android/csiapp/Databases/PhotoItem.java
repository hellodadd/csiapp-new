package com.android.csiapp.Databases;

import java.io.Serializable;

/**
 * Created by user on 2016/10/7.
 */
public class PhotoItem implements Serializable {
    private long id;
    private String mUuid;
    private String mPhotoPath;
    private String mPhotoInfo;

    public PhotoItem(){
        this.id = 0;
        this.mUuid = "";
        this.mPhotoPath = "";
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

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.mPhotoPath = photoPath;
    }

    public String getPhotoInfo() {
        return mPhotoInfo;
    }

    public void setPhotoInfo(String photoInfo) {
        this.mPhotoInfo = photoInfo;
    }
}
