package com.dab.zx.bean;

/**
 * Created by 八神火焰 on 2017/1/13.
 */

public class DuelBean {
    private String numberEx;
    private String thumbnailPath;

    public DuelBean(String numberEx, String thumbnailPath) {
        this.numberEx = numberEx;
        this.thumbnailPath = thumbnailPath;
    }

    public String getNumberEx() {
        return numberEx;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }
}
