package com.zx.bean;

/**
 * Created by 八神火焰 on 2017/1/13.
 */

public class HandBean
{
    private boolean  topVisible;
    private boolean  bottomVisible;
    private DuelBean duelBean;

    public HandBean(DuelBean duelBean) {
        this.duelBean = duelBean;
        this.topVisible = true;
        this.bottomVisible = false;
    }

    public boolean isTopVisible() {
        return topVisible;
    }

    public boolean isBottomVisible() {
        return bottomVisible;
    }

    public DuelBean getDuelBean() {
        return duelBean;
    }

    public void setTopVisible(boolean topVisible) {
        this.topVisible = topVisible;
    }

    public void setBottomVisible(boolean bottomVisible) {
        this.bottomVisible = bottomVisible;
    }
}
