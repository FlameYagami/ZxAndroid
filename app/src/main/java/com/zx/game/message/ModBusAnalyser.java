package com.zx.game.message;

import com.michaelflisar.rxbus2.RxBus;
import com.zx.config.MyApp;
import com.zx.event.DuelistStateEvent;
import com.zx.event.EnterGameEvent;
import com.zx.event.JoinGameEvent;
import com.zx.event.LeaveGameEvent;
import com.zx.event.StartGameEvent;
import com.zx.game.GameConfig;
import com.zx.game.Player;
import com.zx.game.enums.PlayerChange;
import com.zx.game.enums.PlayerType;
import com.zx.game.enums.ServiceMessage;

/**
 * Created by 八神火焰 on 2017/2/11.
 */

class ModBusAnalyser
{
    void analysis(ServicePacket servicePacket) {
        // 读取设备类型
        servicePacket.readByte();
        // 读取指令
        switch (servicePacket.readByte()) {
            case ServiceMessage.PlayerEnter:
                onPlayerEnter(servicePacket);
                break;
            case ServiceMessage.JoinGame:
                onJoinGame(servicePacket);
                break;
            case ServiceMessage.GameConfig:
                onGameConfig(servicePacket);
                break;
            case ServiceMessage.DuelistInfo:
                onDuelistInfo(servicePacket);
                break;
            case ServiceMessage.DuelistState:
                onDuelistState(servicePacket);
                break;
            case ServiceMessage.LeaveGame:
                onLeaveGame(servicePacket);
                break;
            case ServiceMessage.StartGame:
                onStartGame(servicePacket);
                break;
        }
    }

    /**
     * 开始游戏
     */
    private void onStartGame(ServicePacket servicePacket) {
        RxBus.get().send(new StartGameEvent());
    }

    /**
     * 向本地玩家传递其他玩家进入房间的消息,并将信息数据存入缓存
     */
    private void onPlayerEnter(ServicePacket servicePacket) {
        byte playerType = servicePacket.readByte();
        // 成功创建房间或是找到对应房间
        if (playerType != PlayerType.Undefined) {
            String playerName = servicePacket.readStringToEnd();
            Player player     = new Player(playerType, playerName);
            // 对已经在房间的玩家发送其他玩家进入的消息
            RxBus.get().send(new EnterGameEvent(player));
            if (null != MyApp.Client.Game) {
                MyApp.Client.Game.updateDuelist(player);
            }
        }
    }

    /**
     * 向本地玩家传递进入房间的消息,并更新本地玩家类型
     */
    private void onJoinGame(ServicePacket servicePacket) {
        byte playerType = servicePacket.readByte();
        // 成功创建房间或是找到对应房间
        if (playerType != PlayerType.Undefined) {
            int roomId = servicePacket.readCSharpInt();
            // Game中的本地玩家取自索引
            MyApp.Client.Player.setType(playerType);
            MyApp.Client.createGame(String.valueOf(roomId), MyApp.Client.Player);
            RxBus.get().send(new JoinGameEvent(playerType));
        } else {
            RxBus.get().send(new JoinGameEvent(playerType, false));
        }
    }

    private void onGameConfig(ServicePacket servicePacket) {
        MyApp.Client.Game.GameConfig = new GameConfig(servicePacket);
    }

    private void onDuelistInfo(ServicePacket servicePacket) {
        byte    playerType = servicePacket.readByte();
        boolean isReady    = servicePacket.readByte() == PlayerChange.Ready;
        String  playerName = servicePacket.readStringToEnd();
        MyApp.Client.Game.updateDuelist(new Player(playerType, playerName, isReady));
    }

    private void onDuelistState(ServicePacket servicePacket) {
        byte    playerType = servicePacket.readByte();
        boolean isReady    = servicePacket.readByte() == PlayerChange.Ready;
        MyApp.Client.Game.setPlayerReady(playerType, isReady);
        // 向界面告知选手状态改变
        RxBus.get().send(new DuelistStateEvent());
    }

    private void onLeaveGame(ServicePacket servicePacket) {
        byte   playerType = servicePacket.readByte();
        String playerName = servicePacket.readStringToEnd();
        // 向界面告知选手离开
        RxBus.get().send(new LeaveGameEvent(new Player(playerType, playerName), MyApp.Client.Player.getType()));
        // 对应本地用户准备状态更新
        if (playerType == MyApp.Client.Player.getType()) {
            MyApp.Client.leaveGame();
        } else {
            MyApp.Client.Game.removePlayer(new Player(playerType, playerName));
        }
    }
}
