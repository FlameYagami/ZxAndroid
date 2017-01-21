package com.zx.service;

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

    public static final String STOP_SERVICE = "STOP_SERVICE";
    public static final String SEND_BYTES   = "SEND_BYTES";
    public static final String CLOSE_SOCKET = "CLOSE_SOCKET";

    public static boolean isRunning = false;

    private static ClientSocket clientSocket;

    private static final String serviceIP   = "191.191.16.167";
    private static final int    servicePort = 8911;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        initReceiver();
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (clientSocket == null) {
            //初始化
            clientSocket = new ClientSocket(serviceIP, servicePort);
            //正式启动线程
            clientSocket.start();
        } else {
//            clientSocket.setMainBoardId(tempMainBoardId);
        }
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
                    case STOP_SERVICE: {
                        isRunning = false;
                        stopSelf();
                        break;
                    }
                    case SEND_BYTES: {
                        byte[] bytes = intent.getByteArrayExtra(byte[].class.getSimpleName());
                        if (clientSocket != null) {
                            clientSocket.sendMsg(bytes);
                        }
                        break;
                    }
                    case CLOSE_SOCKET: {
                        closeSocket();
                        break;
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_SERVICE);
        filter.addAction(SEND_BYTES);
        filter.addAction(CLOSE_SOCKET);
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 关闭Socket
     */
    private void closeSocket() {
        if (clientSocket != null) {
            clientSocket.setIsStart(false);
            clientSocket = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
        unregisterReceiver(broadcastReceiver);
        LogUtils.d(TAG, "onDestroy");
    }
}
