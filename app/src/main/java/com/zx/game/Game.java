package com.zx.game;

import com.zx.uitls.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

/**
 * Created by 八神火焰 on 2017/2/22.
 */

public class Game
{
    private static final String TAG = Game.class.getSimpleName();

    public GameConfig GameConfig;
    private Player       duelists[]   = new Player[2];
    private List<Player> observerList = new ArrayList<>();

    public Game(Player player) {
        if (2 != player.getType()) {
            duelists[player.getType()] = player;
        } else {
            observerList.add(player);
        }
    }

    public void updateDuelist(Player player) {
        if (0 == player.getType() || 1 == player.getType()) {
            if (null == duelists[player.getType()]) {
                duelists[player.getType()] = player;
            } else {
                duelists[player.getType()].setReady(player.isReady());
            }
        }
    }

    public int getDuelistCount() {
        return stream(duelists).where(duelist -> null != duelist).count();
    }

    public int getObserverCount() {
        return observerList.size();
    }

    public Player[] getDuelists() {
        return duelists;
    }

    public List<Player> getObserverList() {
        return observerList;
    }

    public void removePlayer(Player player) {
        if (1 == player.getType()) {
            duelists[1] = null;
        } else {
            observerList.remove(stream(observerList).first(observer -> observer.getName().equals(player.getName())));
        }
    }

    public void setPlayerReady(byte playerType, boolean isReady) {
        if (0 == playerType || 1 == playerType) {
            duelists[playerType].setReady(isReady);
        } else {
            LogUtils.e(TAG, "setPlayerReady->越界");
        }
    }

//    public boolean moveToDuelist(String playerName) {
//        Player tempPlayer = stream(duelists).first(player -> player.getName().equals(playerName));
//        return duelists.add(tempPlayer) && observerList.remove(tempPlayer);
//    }

//    public boolean moveToObserver(String playerName) {
//        Player tempPlayer = stream(observerList).first(player -> player.getName().equals(playerName));
//        return observerList.add(tempPlayer) && duelists.remove(tempPlayer);
//
//    }
}
