package com.zx.bean;

/**
 * Created by 八神火焰 on 2017/1/16.
 */

public class PlayerBean
{
    private String nickname;
    private boolean isPrepare;
    private boolean isHost;

    public PlayerBean(String nickname, boolean isHost) {
        this.nickname = nickname;
        this.isHost = isHost;
        this.isPrepare = false;
    }

    public void setPrepare(boolean prepare) {
        isPrepare = prepare;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isHost() {
        return isHost;
    }

    public boolean isPrepare() {
        return isPrepare;
    }
}
