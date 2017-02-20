package com.zx.event;

/**
 * Created by 八神火焰 on 2017/2/15.
 */

public class EnterRoomEvent
{
    private int    roomId;
    private String playerName;
    private byte   playerType;

    public EnterRoomEvent(byte playerType, int roomId, String playerName) {
        this.playerType = playerType;
        this.roomId = roomId;
        this.playerName = playerName;
    }

    public byte getPlayerType() {
        return playerType;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
