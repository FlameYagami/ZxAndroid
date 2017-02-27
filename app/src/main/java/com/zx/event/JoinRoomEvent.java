package com.zx.event;

/**
 * Created by 八神火焰 on 2017/2/22.
 */

public class JoinRoomEvent
{
    private byte    playerType;
    private boolean succeed;

    public JoinRoomEvent(byte playerType) {
        this.playerType = playerType;
        this.succeed = true;
    }

    public JoinRoomEvent(byte playerType, boolean succeed) {
        this.playerType = playerType;
        this.succeed = succeed;
    }

    public byte getPlayerType() {
        return playerType;
    }

    public boolean isSucceed() {
        return succeed;
    }
}
