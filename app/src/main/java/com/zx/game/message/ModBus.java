package com.zx.game.message;

import android.text.TextUtils;

import com.zx.game.enums.CtosMessage;

public class ModBus
{
    public static final String TAG = ModBus.class.getSimpleName();

    private static ClientPacket getModBus(byte cmd) {
        return getModBus(cmd, "");
    }

    private static ClientPacket getModBus(byte cmd, String data) {
        ClientPacket packet = new ClientPacket();
        packet.write(cmd);
        if (!TextUtils.isEmpty(data)) {
            packet.write(data);
        }
        return packet;
    }

    public static ClientPacket onHeart() {
        return getModBus(CtosMessage.Response, "");
    }

    /**
     * 创建房间
     */
    public static ClientPacket onCreateRoom(String playerName) {
        return getModBus(CtosMessage.CreateGame, playerName);
    }

    /**
     * 进入房间
     *
     * @param roomId 8位房间编号
     */
    public static ClientPacket onJoinRoom(String roomId, String playerName) {
        return getModBus(CtosMessage.JoinGame, playerName + "#" + roomId);
    }

    /**
     * 离开房间
     */
    public static ClientPacket onLeaveRoom() {
        return getModBus(CtosMessage.LeaveGame);
    }

}