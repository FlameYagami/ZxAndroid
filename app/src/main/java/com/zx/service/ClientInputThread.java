package com.zx.service;

import com.zx.uitls.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
            while (isStart) {
                byte[] data = new byte[1024 * 1024];
                //读取信息,如果没信息将会阻塞线程
                int len = is.read(data);
                if (len != -1) {
                    byte[] bytes = new byte[len];
                    System.arraycopy(data, 0, bytes, 0, len);
//                    String modbus = StringUtils.changeByteToHexString(bytes, 0, bytes.length);
//                    LogUtils.e(TAG, "收到信息" + modbus);
                    if (isStart) {
//                        new AnalysisThread(MyApplication.context, bytes).start();
                    }
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