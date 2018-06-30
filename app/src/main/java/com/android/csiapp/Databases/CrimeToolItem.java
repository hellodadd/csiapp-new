package com.android.csiapp.Databases;

import java.io.Serializable;

/**
 * Created by user on 2016/10/4.
 */
public class CrimeToolItem implements Serializable {
    private long id;
    private String mUuid;
    private String mTool_name;
    private String mTool_category;
    private String mTool_source;

    public CrimeToolItem(){
        this.id = 0;
        this.mUuid = "";
        this.mTool_name = "";
        this.mTool_category = "";
        this.mTool_source = "";
    }

    public CrimeToolItem(long id,String uuid, String name, String category, String source) {
        this.id = id;
        this.mUuid = uuid;
        this.mTool_name = name;
        this.mTool_category = category;
        this.mTool_source = source;
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

    public String getToolName() {return mTool_name; }

    public void setToolName(String tool_name) {this.mTool_name = tool_name; }

    public String getToolCategory() {return mTool_category; }

    public void setToolCategory(String tool_category) {this.mTool_category = tool_category; }

    public String getToolSource() {return mTool_source; }

    public void setToolSource(String tool_source) {this.mTool_source = tool_source; }

    //Check Information
    public boolean checkInformation(){
        boolean result = false;
        if(!mTool_name.isEmpty())
            result = true;
        return result;
    }
}
