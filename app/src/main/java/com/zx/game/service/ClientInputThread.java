package com.zx.game.service;

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
    private boolean isStart = true;
    private InputStream      is;
    private ClientInputCache clientInputCache;

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
            clientInputCache = new ClientInputCache();
            while (isStart) {
                //读取信息,如果没信息将会阻塞线程
                byte[] data = new byte[1024];
                int    len  = is.read(data);
                if (-1 != len && isStart) {
                    byte[] bytes = new byte[len];
                    System.arraycopy(data, 0, bytes, 0, len);
                    clientInputCache.write(bytes);
                    LogUtils.e(TAG, "Read->可能存在粘包的数据包" + Arrays.toString(bytes));
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Read线程抛异常");
            e.printStackTrace();
        } finally {
            try {
                is.close();
                socket.close();
                clientInputCache.onDestroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}