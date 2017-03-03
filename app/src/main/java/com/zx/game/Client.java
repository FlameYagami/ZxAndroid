package com.zx.game;

import com.zx.game.enums.PlayerType;
import com.zx.game.message.ClientPacket;
import com.zx.game.message.MessageManager;
import com.zx.game.service.ClientService;
import com.zx.uitls.IntentUtils;

import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2017/2/22.
 */

public class Client
{
    public MessageManager MessageManager;
    public Player         Player;
    public Room           Room;
    public Game           Game;

    public void start() {
        IntentUtils.startService(ClientService.class);
        MessageManager = new MessageManager();
        MessageManager.start();
    }

    public void initPlayer(String playerName) {
        Player = new Player(playerName);
    }

    /**
     * 创建房间缓存
     */
    public void createGame(String roomId, Player player) {
        if (null == Room) {
            Room = new Room(roomId);
        }
        if (null == Game) {
            Game = new Game(player);
        }
    }

    /**
     * 销毁房间缓存
     */
    public void leaveGame() {
        Room = null;
        Game = null;
        Player.setType(PlayerType.Undefined);
    }

    public void finish() {
        MessageManager.finish();
        IntentUtils.sendBroadcast(context, ClientService.STOP_SERVICE);
    }

    public void send(ClientPacket clientPacket) {
        MessageManager.sendMessage(clientPacket);
    }

    public void receive(byte[] bytes) {
        MessageManager.receiveMessage(bytes);
    }
}
