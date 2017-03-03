package com.zx.game;

import com.zx.game.message.ServicePacket;

/**
 * Created by 八神火焰 on 2017/2/28.
 */

public class GameConfig
{
    private int     StartLp;
    private int     StartHand;
    private int     StartResouce;
    private int     DrawCount;
    private int     GameTimer;
    private boolean ShuffleDeck;

    public GameConfig() {
        StartLp = 4;
        StartHand = 4;
        StartResouce = 2;
        DrawCount = 2;
        GameTimer = 120;
        ShuffleDeck = true;
    }

    public GameConfig(ServicePacket packet) {
        StartLp = packet.readByte();
        StartHand = packet.readByte();
        StartResouce = packet.readByte();
        DrawCount = packet.readByte();
        GameTimer = packet.readByte();
        ShuffleDeck = packet.readByte() == 1;
    }

    public int getStartLp() {
        return StartLp;
    }

    public int getStartHand() {
        return StartHand;
    }

    public int getDrawCount() {
        return DrawCount;
    }

    public int getStartResouce() {
        return StartResouce;
    }

    public boolean isShuffleDeck() {
        return ShuffleDeck;
    }

    public int getGameTimer() {
        return GameTimer;
    }
}
