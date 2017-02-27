package com.zx.event;

import com.zx.game.Player;

/**
 * Created by 八神火焰 on 2017/2/24.
 */

public class EnterRoomEvent
{
    Player player;

    public EnterRoomEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
