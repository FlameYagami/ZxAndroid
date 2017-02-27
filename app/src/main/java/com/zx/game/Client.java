package com.zx.game;

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
    public MessageManager mMessageManager;
    public Player         Player;
    public Room           Room;

    public void start() {
        IntentUtils.startService(ClientService.class);
        mMessageManager = new MessageManager();
        mMessageManager.start();
    }

    public void initPlayer(String playerName) {
        Player = new Player(playerName);
    }

    /**
     * 创建房间缓存
     */
    public void createRoom(){
        if (null == Room){
            Room = new Room();
        }
    }

    public void setRoom(String roomId){
        Room.setRoomId(roomId);
    }

    public void joinRoom(Player player){
        Room.addPlayer(player);
    }

    public void finish() {
        mMessageManager.finish();
        IntentUtils.sendBroadcast(context, ClientService.STOP_SERVICE);
    }

    public void send(ClientPacket clientPacket) {
        mMessageManager.sendMessage(clientPacket);
    }

    public void receive(byte[] bytes) {
        mMessageManager.receiveMessage(bytes);
    }
}
