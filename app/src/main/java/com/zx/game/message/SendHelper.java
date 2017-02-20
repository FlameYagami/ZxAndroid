package com.zx.game.message;

import com.zx.config.MyApp;
import com.zx.game.service.ClientService;
import com.zx.uitls.BundleUtils;
import com.zx.uitls.IntentUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by 八神火焰 on 2017/2/10.
 */

class SendHelper
{
    private static String TAG = SendHelper.class.getSimpleName();

    private static Queue<byte[]> bytesQueue = new LinkedList<>();

    void add(ClientPacket clientPacket) {
        bytesQueue.add(clientPacket.getBytes());
    }

    void send() {
        if (bytesQueue.size() > 0) {
            IntentUtils.sendBroadcast(MyApp.context, ClientService.SEND_MESSAGE, BundleUtils.putByteArray(byte[].class.getSimpleName(), bytesQueue.peek()));
            bytesQueue.remove();
        }
    }
}
