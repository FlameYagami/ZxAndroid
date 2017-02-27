package com.zx.game.service;

import com.zx.game.message.ServicePacket;
import com.zx.uitls.Md5Utils;

import java.nio.ByteBuffer;

/**
 * Created by 八神火焰 on 2017/2/25.
 */

public class ClientInputCache
{
    private static final String TAG = ClientInputCache.class.getSimpleName();

    private ByteBuffer mByteBuffer;

    public void write(byte[] bytes) {
        byte[] remainingBytes;
        if (null != (remainingBytes = getRemaining())) {
            bytes = Md5Utils.combineByte(remainingBytes, bytes);
        }
        mByteBuffer = ByteBuffer.wrap(bytes);
    }

    public byte[] getRemaining() {
        if (null != mByteBuffer && mByteBuffer.hasRemaining()) {
            byte[] bytes = new byte[mByteBuffer.remaining()];
            mByteBuffer.get(bytes, 0, mByteBuffer.remaining());
            return bytes;
        }
        return null;
    }

    public byte[] read() {
        int length = readLength();
        if (-1 != length && length + 4 <= mByteBuffer.remaining()) {
            byte[] bytes = new byte[length];
            mByteBuffer.get(bytes, 0, 4);
            mByteBuffer.get(bytes, 0, length);
            return bytes;
        }
        return null;
    }

    private int readLength() {
        if (null != mByteBuffer && 4 <= mByteBuffer.remaining()) {
            byte[] bytes = new byte[4];
            System.arraycopy(mByteBuffer.array(), 0, bytes, 0, 4);
            return new ServicePacket(bytes).readCSharpInt();
        }
        return -1;
    }
}
