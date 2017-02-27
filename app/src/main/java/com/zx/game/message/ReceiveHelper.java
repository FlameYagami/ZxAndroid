package com.zx.game.message;

import com.zx.config.MyApp;
import com.zx.event.EnterRoomEvent;
import com.zx.event.JoinRoomEvent;
import com.zx.event.LeaveRoomEvent;
import com.zx.game.Player;
import com.zx.game.enums.PlayerChange;
import com.zx.game.enums.PlayerType;
import com.zx.game.enums.StocMessage;
import com.zx.uitls.RxBus;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by 八神火焰 on 2017/2/11.
 */

class ReceiveHelper
{
    private static Queue<ServicePacket> modbusQueue = new LinkedList<>();

    void add(ServicePacket servicePacket) {
        modbusQueue.add(servicePacket);
    }

    void analysis() {
        if (modbusQueue.size() > 0) {
            ServicePacket servicePacket = modbusQueue.peek();
            // 读取设备类型
            servicePacket.readByte();
            // 读取指令
            switch (servicePacket.readByte()) {
                case StocMessage.HsPlayerEnter:
                    onPlayerEnter(servicePacket);
                    break;
                case StocMessage.JoinGame:
                    onJoinGame(servicePacket);
                    break;
                case StocMessage.HsDuelistState:
                    onPlayerChange(servicePacket);
                    break;
                case StocMessage.LeaveGame:
                    onLeaveGame(servicePacket);
                    break;
            }
            modbusQueue.remove();
        }
    }

    /**
     * 通知有玩家进入房间,此时玩家信息数据存入缓存
     */
    private void onPlayerEnter(ServicePacket servicePacket) {
        byte playerType = servicePacket.readByte();
        if (playerType != PlayerType.Undefined) {
            String playerName = servicePacket.readStringToEnd();
            Player player     = new Player(playerType, playerName);
            MyApp.Client.createRoom();
            MyApp.Client.joinRoom(player);
            RxBus.getInstance().post(new EnterRoomEvent(player));
        }
    }

    private void onJoinGame(ServicePacket servicePacket) {
        byte playerType = servicePacket.readByte();
        if (playerType != PlayerType.Undefined) {
            int roomId = servicePacket.readCSharpInt();
            MyApp.Client.setRoom(String.valueOf(roomId));
            MyApp.Client.Player.update(playerType);
            RxBus.getInstance().post(new JoinRoomEvent(playerType));
        } else {
            RxBus.getInstance().post(new JoinRoomEvent(playerType, false));
        }
    }

    private void onPlayerChange(ServicePacket servicePacket) {
        int     playerType = servicePacket.readByte() == PlayerType.Host ? 0 : 1;
        boolean isReady    = servicePacket.readByte() == PlayerChange.Ready;
        MyApp.Client.Room.setPlayerReady(playerType, isReady);
    }

    private void onLeaveGame(ServicePacket servicePacket) {
        byte   playerType = servicePacket.readByte();
        String playerName = servicePacket.readStringToEnd();
        RxBus.getInstance().post(new LeaveRoomEvent(playerType, playerName));
    }
}
