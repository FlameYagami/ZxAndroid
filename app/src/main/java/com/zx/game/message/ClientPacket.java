package com.zx.game.message;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by 八神火焰 on 2017/2/16.
 */

public class ClientPacket
{
    private ByteArrayOutputStream mByteArrayOutputStream;

    public ClientPacket() {
        mByteArrayOutputStream = new ByteArrayOutputStream();
        mByteArrayOutputStream.write((byte)4);
    }

    public void write(String string) {
        try {
            byte[] bytes = string.getBytes("UTF-8");
            write(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void write(byte b) {
        mByteArrayOutputStream.write(b);
    }

    public void write(byte[] bytes) {
        for (int i = 0; i != bytes.length; i++) {
            mByteArrayOutputStream.write(bytes[i]);
        }
    }

    public byte[] getBytes() {
        return mByteArrayOutputStream.toByteArray();
    }
}
