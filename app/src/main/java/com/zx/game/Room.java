package com.zx.game;

/**
 * Created by 八神火焰 on 2017/2/22.
 */

public class Room
{
    private static final String TAG = Room.class.getSimpleName();

    private String roomId;

    public Room(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
