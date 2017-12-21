package com.dab.zx.event;

import com.dab.zx.game.Player;

/**
 * Created by 八神火焰 on 2017/2/20.
 */

public class LeaveGameEvent {
    Player player;
    byte   ownerType;

    public LeaveGameEvent(Player player, byte ownerType) {
        this.player = player;
        this.ownerType = ownerType;
    }

    public Player getPlayer() {
        return player;
    }

    public byte getOwnerType() {
        return ownerType;
    }
}
