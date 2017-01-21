package com.zx.uitls;

import android.content.Context;
import android.content.SharedPreferences;

import com.zx.config.MyApp;

public class SpUtil
{
    public static final String IsNotFirst = "IsNotFirst";

    private static SpUtil instance;

    private SharedPreferences sp;

    private static final String CONFIG = "CONFIG";

    private SpUtil() {
        sp = MyApp.context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    }

    public static SpUtil getInstances() {
        if (null == instance) {
            instance = new SpUtil();
        }
        return instance;
    }

    /**
     * 存储一个float数据类型
     */
    public void putFloat(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * 获取一个float数据类型
     */
    public float getFloat(String key) {
        return sp.getFloat(key, 0);
    }

    /**
     * 存储一个boolean数据类型
     */
    public void putBoolean(String key, boolean value) {
        sp.edit().putString(key, String.valueOf(value)).apply();
    }

    /**
     * 获取一个boolean数据类型
     */
    public boolean getBoolean(String key) {
        return sp.getString(key, "").equals(String.valueOf(true));
    }

    /**
     * 存储一个String数据类型
     */
    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    /**
     * 获取一个String数据类型
     */
    public String getString(String key) {
        return sp.getString(key, "");
    }

    /**
     * 存储一个Int数据类型
     */
    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 存储一个Long数据类型
     */
    public void putLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 获取一个Int数据类型
     */
    public int getInt(String key) {
        return sp.getInt(key, -1);
    }

    /**
     * 获取一个Int数据类型
     */
    public long getLong(String key) {
        return sp.getLong(key, -1);
    }

}
