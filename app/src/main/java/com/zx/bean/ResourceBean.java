package com.zx.bean;

/**
 * Created by 八神火焰 on 2017/1/14.
 */

public class ResourceBean
{
    private boolean  resourceVisible;
    private DuelBean duelBean;

    public ResourceBean(DuelBean duelBean) {
        this.duelBean = duelBean;
        this.resourceVisible = true;
    }

    public boolean isResourceVisible() {
        return resourceVisible;
    }

    public DuelBean getDuelBean() {
        return duelBean;
    }

    public void setResourceVisible(boolean resourceVisible) {
        this.resourceVisible = resourceVisible;
    }
}
