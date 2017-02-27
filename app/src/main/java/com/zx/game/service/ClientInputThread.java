package com.zx.game.service;

import com.zx.config.MyApp;
import com.zx.uitls.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/6/25.
 */
class ClientInputThread extends Thread
{
    private static final String TAG = ClientInputThread.class.getSimpleName();

    private Socket socket;
    private String msg;
    private boolean isStart = true;
    private InputStream is;


    ClientInputThread(Socket socket) {
        this.socket = socket;
        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    @Override
    public void run() {
        try {
            ClientInputCache clientInputCache = new ClientInputCache();
            while (isStart) {
                byte[] readBytes;
                if (isStart && null != (readBytes = clientInputCache.read())) {
                    LogUtils.e(TAG,"read->" + Arrays.toString(readBytes));
                    MyApp.Client.receive(readBytes);
                }
                //读取信息,如果没信息将会阻塞线程
                byte[] data = new byte[1024];
                int    len  = is.read(data);
                if (-1 != len) {
                    byte[] bytes = new byte[len];
                    System.arraycopy(data, 0, bytes, 0, len);
                    LogUtils.e(TAG, "收到信息" + Arrays.toString(bytes));
                    clientInputCache.write(bytes);
                }
            }

        } catch (Exception e) {
            LogUtils.e(TAG, "read线程抛异常");
            e.printStackTrace();
        } finally {
            try {
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}