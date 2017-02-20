package com.zx.game.message;

import com.zx.game.service.ClientService;
import com.zx.uitls.IntentUtils;
import com.zx.uitls.LogUtils;
import com.zx.uitls.Md5Utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

import static com.zx.config.MyApp.context;

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
        IntentUtils.startService(ClientService.class);
        mTickSubscriber = new TickSubscriber();
    }

    public void end() {
        IntentUtils.sendBroadcast(context, ClientService.STOP_SERVICE);
        mTickSubscriber.releaseSubscriber();
    }

    public void sendMessage(ClientPacket clientPacket) {
        // 添加Md5验证码
        clientPacket.write(Md5Utils.calculate(clientPacket.getBytes()));
        mSendHelper.add(clientPacket);
    }

    public void receiveMessage(byte[] bytes) {
        if (!Md5Utils.check(bytes)) {
            LogUtils.e(TAG, "Md5校验错误");
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
        TickSubscriber(){
            Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe(subscriber);
        }

        Subscriber<Long> subscriber = new Subscriber<Long>()
        {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long o) {
                tick();
            }
        };

        void releaseSubscriber() {
            subscriber.unsubscribe();
            subscriber = null;
        }
    }
}
