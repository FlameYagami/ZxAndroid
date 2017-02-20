package com.zx.event;

/**
 * Created by 八神火焰 on 2017/2/20.
 */

public class LeaveRoomEvent
{
    private String playerName;
    private byte   playerType;

    public LeaveRoomEvent(byte playerType, String playerName) {
        this.playerType = playerType;
        this.playerName = playerName;
    }

    public byte getPlayerType() {
        return playerType;
    }

    public String getPlayerName() {
        return playerName;
    }
}
