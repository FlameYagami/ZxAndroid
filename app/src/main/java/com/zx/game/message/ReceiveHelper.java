package com.zx.game.message;

import com.zx.event.EnterRoomEvent;
import com.zx.event.LeaveRoomEvent;
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
                case 41:
                    onCreateGame(servicePacket);
                    break;
                case 42:
                    onJoinGame(servicePacket);
                    break;
                case 43:
                    onLeaveGame(servicePacket);
                    break;
            }
            modbusQueue.remove();
        }
    }

    private void onCreateGame(ServicePacket servicePacket) {
        byte   playerType = servicePacket.readByte();
        int    roomId     = servicePacket.readCSharpInt();
        String playerName = servicePacket.readStringToEnd();
        RxBus.getInstance().post(new EnterRoomEvent(playerType, roomId, playerName));
    }

    private void onJoinGame(ServicePacket servicePacket) {
        byte   playerType = servicePacket.readByte();
        int    roomId     = servicePacket.readCSharpInt();
        String playerName = servicePacket.readStringToEnd();
        switch (playerType) {
            case 1:
            case 2:
                RxBus.getInstance().post(new EnterRoomEvent(playerType, roomId, playerName));
                break;
        }
    }

    private void onLeaveGame(ServicePacket servicePacket) {
        byte   playerType = servicePacket.readByte();
        String playerName = servicePacket.readStringToEnd();
        RxBus.getInstance().post(new LeaveRoomEvent(playerType, playerName));
    }
}
