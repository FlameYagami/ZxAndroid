package com.zx.game.message;

import com.zx.uitls.LogUtils;
import com.zx.uitls.Md5Utils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by 八神火焰 on 2017/2/10.
 */

public class MessageManager
{
    private static String TAG = MessageManager.class.getSimpleName();

    private SendHelper     mSendHelper;
    private ReceiveHelper  mReceiveHelper;
    private TickSubscriber mTickSubscriber;

    public MessageManager() {
        mSendHelper = new SendHelper();
        mReceiveHelper = new ReceiveHelper();
    }

    public void start() {
        mTickSubscriber = new TickSubscriber();
    }

    public void finish() {
        mTickSubscriber.releaseSubscriber();
    }

    public void sendMessage(ClientPacket clientPacket) {
        // 添加Md5验证码
        clientPacket.write(Md5Utils.calculate(clientPacket.getBytes()));
        mSendHelper.add(clientPacket);
    }

    public void receiveMessage(byte[] bytes) {
        if (!Md5Utils.check(bytes)) {
            LogUtils.e(TAG, "Md5校验错误->", Arrays.toString(bytes));
            return;
        }
        // 移除Md5验证码
        byte[] removeMd5 = Md5Utils.removeMd5(bytes);
        mReceiveHelper.add(new ServicePacket(removeMd5));
    }

    private void tick() {
        mSendHelper.send();
        mReceiveHelper.analysis();
    }

    private class TickSubscriber
    {
        Subscription stopMePlease;

        TickSubscriber() {
            stopMePlease = Observable.interval(100, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                tick();
            });
        }

        void releaseSubscriber() {
            stopMePlease.unsubscribe();
            stopMePlease = null;
        }
    }
}
