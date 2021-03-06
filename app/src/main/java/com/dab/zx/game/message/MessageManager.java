package com.dab.zx.game.message;

import com.dab.zx.game.service.ClientSocket;
import com.dab.zx.uitls.LogUtils;
import com.dab.zx.uitls.Md5Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 八神火焰 on 2017/2/10.
 */
public class MessageManager {
    private static String TAG = MessageManager.class.getSimpleName();

    private ModBusAnalyser mModBusAnalyser;
    private ClientSocket   mClientSocket;
    private TickDisposable mTickDisposable;
    private Queue<byte[]>        sendQueue    = new LinkedList<>();
    private Queue<ServicePacket> receiveQueue = new LinkedList<>();

    public MessageManager(ClientSocket mClientSocket) {
        this.mClientSocket = mClientSocket;
        mModBusAnalyser = new ModBusAnalyser();
    }

    public void start() {
        mTickDisposable = new TickDisposable();
    }

    public void finish() {
        mTickDisposable.releaseSubscriber();
    }

    public void sendMessage(ClientPacket clientPacket) {
        // 添加Md5验证码
        clientPacket.write(Md5Utils.calculate(clientPacket.getBytes()));
        sendQueue.add(clientPacket.getBytes());
    }

    public void receiveMessage(byte[] bytes) {
        if (!Md5Utils.check(bytes)) {
            LogUtils.e(TAG, "Md5校验错误->", Arrays.toString(bytes));
            return;
        }
        // 移除Md5验证码
        byte[] removeMd5 = Md5Utils.removeMd5(bytes);
        receiveQueue.add(new ServicePacket(removeMd5));
    }

    private void tick() {
//        if (checkDisconnected()) {
//            networkSend();
//            networkReceive();
//        }
        networkSend();
        networkReceive();
    }

    private boolean checkDisconnected() {
        try {
            mClientSocket.getSocket().sendUrgentData(0xFF);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void networkSend() {
        if (sendQueue.size() > 0) {
            mClientSocket.sendMsg(sendQueue.peek());
            sendQueue.remove();
        }
    }

    private void networkReceive() {
        if (receiveQueue.size() > 0) {
            mModBusAnalyser.analysis(receiveQueue.peek());
            receiveQueue.remove();
        }
    }

    private class TickDisposable {
        Disposable disposable;

        TickDisposable() {
            disposable = Observable.interval(100, TimeUnit.MILLISECONDS).subscribe(aLong -> tick());
        }

        void releaseSubscriber() {
            disposable.dispose();
            disposable = null;
        }
    }
}
