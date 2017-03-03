package com.zx.game.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

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
    public static final String SEND_MESSAGE   = "SEND_MESSAGE";
    public static final String RESTART_SOCKET = "RESTART_SOCKET";
    public static final String CONNECT_ERROR  = "CONNECT_ERROR";

    private ClientSocket      clientSocket;
    private BroadcastReceiver broadcastReceiver;

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
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case STOP_SERVICE:
                        stopSelf();
                        break;
                    case SEND_MESSAGE:
                        byte[] bytes = intent.getByteArrayExtra(byte[].class.getSimpleName());
                        if (clientSocket != null) {
                            clientSocket.sendMsg(bytes);
                        }
                        break;
                    case RESTART_SOCKET:
                        restartSocket();
                        break;
                    case CONNECT_ERROR:
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_SERVICE);
        filter.addAction(RESTART_SOCKET);
        filter.addAction(SEND_MESSAGE);
        filter.addAction(CONNECT_ERROR);
        registerReceiver(broadcastReceiver, filter);
    }

    private void closeSocket() {
        if (clientSocket != null) {
            clientSocket.setIsStart(false);
            clientSocket = null;
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
        clientSocket = new ClientSocket(serviceIP, servicePort);
        //正式启动线程
        clientSocket.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
        unregisterReceiver(broadcastReceiver);
        LogUtils.d(TAG, "onDestroy");
    }
}
