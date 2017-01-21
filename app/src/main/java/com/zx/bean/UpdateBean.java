package com.zx.bean;

import java.io.Serializable;

public class UpdateBean implements Serializable
{
    private String versionName;
    private String url;
    private String versionNote;
    private String md5;
    private int    versionCode;
    private long   size;

    public UpdateBean() {
        versionCode = -1;
        size = -1;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getUrl() {
        return url;
    }

    public String getVersionNote() {
        return versionNote;
    }

    public String getMd5() {
        return md5;
    }

    public long getSize() {
        return size;
    }
}
