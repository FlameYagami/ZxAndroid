package com.zx.game.service;

import com.zx.config.MyApp;
import com.zx.game.message.ModBus;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Administrator on 2016/6/25.
 */
class ClientSocket extends Thread
{
    private Socket socket;
    private Client client;
    private String ip;
    private int    port;
    private boolean isStart = false;

    private static ClientHeartThread socketHeartThread;


    /**
     * 使用TCP协议,连接访问
     *
     * @param ip   目标机器的IP
     * @param port 端口
     */
    ClientSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            //实例化一个Socket对象
            socket = new Socket();
            //与对应的ip、端口进行连接,先要把桥建好
            socket.connect(new InetSocketAddress(ip, port), 3000);
            setClient();
        } catch (Exception e) {
            e.printStackTrace();
            isStart = false;
        }

    }

    private void setClient() {
        if (socket.isConnected()) {
            System.out.println("打开TCP对应的输入/输出流监听");
            client = new Client(socket);
            client.start();
            isStart = true;
            if (socketHeartThread == null) {
                socketHeartThread = new ClientHeartThread();
                socketHeartThread.start();
            }
        }
    }

    // 直接通过client得到读线程
    public ClientInputThread getClientInputThread() {
        return client.getIn();
    }

    // 直接通过client得到写线程
    public ClientOutputThread getClientOutputThread() {
        return client.getOut();
    }

    //返回Socket状态
    public boolean isStart() {
        return isStart;
    }

    // 直接通过client停止读写消息
    void setIsStart(boolean isStart) {
        if (this.isStart != isStart) {
            this.isStart = isStart;
            if (client.getIn() != null) {
                client.getIn().setStart(isStart);
            }
            if (client.getOut() != null) {
                client.getOut().setStart(isStart);
            }
            if (!isStart) {
                socketHeartThread.Cancel();
                socketHeartThread = null;
            }
        }
    }

    void sendMsg(byte[] msg) {
        if (null != client && null != client.getOut()) {
            client.getOut().sendMsg(msg);
        }
    }

    private class ClientHeartThread extends Thread
    {
        private boolean isRun;

        @Override
        public void run() {
            super.run();
            isRun = true;
            while (isRun) {
                try {
                    MyApp.Client.send(ModBus.onHeart());
                    sleep(15000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        void Cancel() {
            isRun = false;
        }
    }

    private class Client
    {
        private ClientInputThread  in;
        private ClientOutputThread out;

        Client(Socket socket) {
            //用这个监听输入流线程来接收信息
            in = new ClientInputThread(socket);
            //以后就用这个监听输出流的线程来发送信息了
            out = new ClientOutputThread(socket);
        }

        public void start() {
            in.setStart(true);
            out.setStart(true);
            in.start();
            out.start();
        }

        // 得到读消息线程
        public ClientInputThread getIn() {
            return in;
        }

        // 得到写消息线程
        public ClientOutputThread getOut() {
            return out;
        }
    }
}
