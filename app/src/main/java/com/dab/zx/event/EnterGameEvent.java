package com.dab.zx.event;

import com.dab.zx.game.Player;

/**
 * Created by 八神火焰 on 2017/2/24.
 */

public class EnterGameEvent {
    Player player;

    public EnterGameEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
