package com.dab.zx.bean;

import java.io.Serializable;

public class UpdateBean implements Serializable {
    private String versionname;
    private String url;
    private String info;
    private int    versioncode;
    private long   size;

    public UpdateBean() {
        versioncode = -1;
        size = -1;
    }

    public String getVersionname() {
        return versionname;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public String getUrl() {
        return url;
    }

    public String getInfo() {
        return info;
    }

    public long getSize() {
        return size;
    }
}
