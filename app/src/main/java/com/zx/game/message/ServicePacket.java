package com.zx.game.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by 八神火焰 on 2017/2/16.
 */

public class ServicePacket
{
    private DataInputStream mDataInputStream;

    ServicePacket(byte[] bytes) {
        InputStream mInputStream = new ByteArrayInputStream(bytes);
        mDataInputStream = new DataInputStream(mInputStream);
    }

    public int readCSharpInt() {
        byte[] bytes = new byte[4];
        try {
            for (int i = 3; i != 0; i--) {
                bytes[i] = (byte)mDataInputStream.read();
            }
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            return buffer.getInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public short readCSharpShort() {
        byte[] bytes = new byte[2];
        try {
            for (int i = 2; i != 0; --i) {
                bytes[i] = (byte)mDataInputStream.read();
            }
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            return buffer.getShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public byte readByte() {
        try {
            return mDataInputStream.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String readStringToEnd() {
        try {
            return new String(readBytesToEnd(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    public byte[] readBytesToEnd() {
        try {
            int    avi   = mDataInputStream.available();
            byte[] bytes = new byte[avi];
            for (int i = 0; i != avi; i++) {
                bytes[i] = readByte();
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{-1};
    }
}
