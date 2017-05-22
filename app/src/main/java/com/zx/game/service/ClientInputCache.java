package com.zx.game.service;

import com.zx.game.message.ServicePacket;
import com.zx.uitls.Md5Utils;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 八神火焰 on 2017/2/25.
 */

public class ClientInputCache
{
    private static final String TAG = ClientInputCache.class.getSimpleName();

    private OnReadListener mOnReadListener;

    private ByteBuffer     mByteBuffer;
    private ReadSubscriber mReadSubscriber;

    public ClientInputCache(OnReadListener mOnReadListener) {
        this.mOnReadListener = mOnReadListener;
        if (null == mReadSubscriber) {
            mReadSubscriber = new ReadSubscriber();
        }
    }

    public interface OnReadListener
    {
        void read(byte[] bytes);
    }

    public void onDestroy() {
        if (null != mReadSubscriber) {
            mReadSubscriber.releaseSubscriber();
            mReadSubscriber = null;
        }
    }

    public void write(byte[] bytes) {
        byte[] remainingBytes;
        if (null != (remainingBytes = getRemaining())) {
            bytes = Md5Utils.combineByte(remainingBytes, bytes);
        }
        mByteBuffer = ByteBuffer.wrap(bytes);
    }

    private byte[] getRemaining() {
        if (null != mByteBuffer && mByteBuffer.hasRemaining()) {
            byte[] bytes = new byte[mByteBuffer.remaining()];
            mByteBuffer.get(bytes, 0, mByteBuffer.remaining());
            return bytes;
        }
        return null;
    }

    private synchronized byte[] read() {
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
            byte[] bytes    = new byte[4];
            int    position = mByteBuffer.array().length - mByteBuffer.remaining();
            System.arraycopy(mByteBuffer.array(), position, bytes, 0, 4);
            return new ServicePacket(bytes).readCSharpInt();
        }
        return -1;
    }

    private class ReadSubscriber
    {
        Disposable disposable;

        ReadSubscriber() {
            disposable = Observable.interval(500, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                byte[] readBytes;
                if (null != (readBytes = read())) {
                    mOnReadListener.read(readBytes);
                }
            });
        }

        void releaseSubscriber() {
            disposable.dispose();
            disposable = null;
        }
    }
}
