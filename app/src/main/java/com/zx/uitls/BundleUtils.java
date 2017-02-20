package com.zx.uitls;

import android.os.Bundle;

public class BundleUtils
{
    /**
     * 返回一个Boolean类型的Bundle
     *
     * @param key   键
     * @param value 值
     * @return Bundle
     */
    public static Bundle putBoolean(String key, boolean value) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(key, value);
        return bundle;
    }

    /**
     * 返回一个Int类型的Bundle
     *
     * @param key   键
     * @param value 值
     * @return Bundle
     */
    public static Bundle putInt(String key, int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        return bundle;
    }

    /**
     * 返回一个String类型的Bundle
     *
     * @param key   键
     * @param value 值
     * @return Bundle
     */
    public static Bundle putString(String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static Bundle putByteArray(String key, byte[] value) {
        Bundle bundle = new Bundle();
        bundle.putByteArray(key, value);
        return bundle;
    }
}
