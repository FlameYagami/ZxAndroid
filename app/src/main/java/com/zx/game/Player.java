package com.zx.game;

/**
 * Created by 八神火焰 on 2017/2/21.
 */

public class Player
{
    private boolean ready;
    private String  name;
    private byte    type;

    public Player(String name) {
        this.name = name;
    }

    public Player(byte type, String name) {
        this.name = name;
        this.type = type;
        this.ready = false;
    }

    public Player(byte type, String name, boolean isReady) {
        this.name = name;
        this.type = type;
        this.ready = isReady;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public boolean isReady() {
        return ready;
    }

    public byte getType() {
        return type;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getName() {
        return name;
    }
}
