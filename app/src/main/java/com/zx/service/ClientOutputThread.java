package com.zx.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/6/25.
 */
class ClientOutputThread extends Thread
{
    private static final String TAG = ClientOutputThread.class.getSimpleName();

    private Socket       socket;
    private OutputStream os;
    private boolean isStart = true;
    private byte[] msg;

    ClientOutputThread(Socket socket) {
        this.socket = socket;
        try {
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    // 这里处理跟服务器是一样的
    void sendMsg(byte[] msg) {
        this.msg = msg;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                if (msg != null) {
//                    LogUtils.e(TAG, "发送信息：" + StringUtils.changeByteToHexString(msg, 0, msg.length));
                    os.write(msg);
                    os.flush();
                    msg = null;
                    synchronized (this) {
                        wait();// 发送完消息后，线程进入等待状态
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        // 循环结束后，关闭输出流和socket
        finally {
            try {
                os.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
