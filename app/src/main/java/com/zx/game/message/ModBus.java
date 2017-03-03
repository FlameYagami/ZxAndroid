package com.zx.game.message;

import android.text.TextUtils;

import com.zx.game.Player;
import com.zx.game.enums.ClientMessage;
import com.zx.game.enums.PlayerChange;
import com.zx.ui.deckeditor.DeckManager;
import com.zx.uitls.JsonUtils;

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
        return getModBus(ClientMessage.Response, "");
    }

    /**
     * 创建房间
     */
    public static ClientPacket onCreateRoom(Player player) {
        return getModBus(ClientMessage.CreateGame, player.getName());
    }

    /**
     * 进入房间
     *
     * @param roomId 8位房间编号
     */
    public static ClientPacket onJoinRoom(String roomId, Player player) {
        return getModBus(ClientMessage.JoinGame, player.getName() + "#" + roomId);
    }

    /**
     * 离开房间
     */
    public static ClientPacket onLeaveRoom(Player player) {
        ClientPacket packet = getModBus(ClientMessage.LeaveGame);
        packet.write(player.getType());
        return packet;
    }

    /**
     * 设置准备、取消准备
     */
    public static ClientPacket onPlayerState(Player player) {
        ClientPacket packet = getModBus(ClientMessage.DuelistState);
        packet.write(player.isReady() ? PlayerChange.NotReady : PlayerChange.Ready);
        return packet;
    }

    /**
     * 开始游戏、更新卡组信息到服务器
     */
    public static ClientPacket onStartGame(DeckManager deckManager) {
        return getModBus(ClientMessage.StartGame, JsonUtils.serializer(deckManager.getNumberExList()));
    }
}