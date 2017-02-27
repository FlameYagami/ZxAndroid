package com.zx.game;

import com.zx.uitls.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

/**
 * Created by 八神火焰 on 2017/2/22.
 */

public class Room
{
    private static final String TAG = Room.class.getSimpleName();

    private String roomId;
    private Player       duelists[]   = new Player[2];
    private List<Player> observerList = new ArrayList<>();

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void addPlayer(Player playerBean) {
        if (2 != playerBean.getType()) {
            duelists[playerBean.getType()] = playerBean;
        } else {
            observerList.add(playerBean);
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

//    public boolean removePlayer(String playerName) {
//        if (stream(duelists).any(player -> player.getName().equals(playerName))) {
//            return duelists.remove(stream(duelists).first(player -> player.getName().equals(playerName)));
//        } else {
//            return duelists.remove(stream(observerList).first(player -> player.getName().equals(playerName)));
//        }
//    }

    public void setPlayerReady(int playerType, boolean isReady) {
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
