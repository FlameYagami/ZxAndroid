package com.zx.game.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.zx.config.MyApp;
import com.zx.game.message.MessageManager;
import com.zx.uitls.LogUtils;


/**
 * 网络传输TCP服务
 */
public class ClientService extends Service
{
    private static final String TAG = ClientService.class.getSimpleName();

    private static final String serviceIP   = "191.191.16.167";
    private static final int    servicePort = 8989;

    public static final String STOP_SERVICE   = "STOP_SERVICE";
    public static final String RESTART_SOCKET = "RESTART_SOCKET";

    private ClientSocket      mClientSocket;
    private BroadcastReceiver mReceiver;
    private MessageManager    mMessageManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initReceiver();
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        restartSocket();
        return START_REDELIVER_INTENT;
    }

    /**
     * 广播注册
     */
    private void initReceiver() {
        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case STOP_SERVICE:
                        stopSelf();
                        break;
                    case RESTART_SOCKET:
                        restartSocket();
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_SERVICE);
        filter.addAction(RESTART_SOCKET);
        registerReceiver(mReceiver, filter);
    }

    private void closeSocket() {
        if (mClientSocket != null) {
            mClientSocket.setIsStart(false);
            mClientSocket = null;
            // 关闭Socket休眠0.5s重启
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重启Socket
     */
    private void restartSocket() {
        closeSocket();
        //初始化
        mClientSocket = new ClientSocket(serviceIP, servicePort, isConnected -> {
            if (isConnected) {
                mMessageManager = new MessageManager(mClientSocket);
                mMessageManager.start();
                MyApp.Client.initMessageManager(mMessageManager);
            }
        });
        //正式启动线程
        mClientSocket.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
        unregisterReceiver(mReceiver);
        mMessageManager.finish();
        LogUtils.d(TAG, "onDestroy");
    }
}
