package com.zx.uitls;

import android.content.Context;
import android.content.SharedPreferences;

import com.zx.config.MyApp;

public class SpUtils
{
    private static SpUtils instance;

    private SharedPreferences sp;

    private static final String CONFIG = "CONFIG";

    private SpUtils() {
        sp = MyApp.context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    }

    public static SpUtils getInstance() {
        if (null == instance) {
            instance = new SpUtils();
        }
        return instance;
    }

    /**
     * 存储一个float数据类型
     */
    public static void putFloat(String key, float value) {
        getInstance().sp.edit().putFloat(key, value).apply();
    }

    /**
     * 获取一个float数据类型
     */
    public static float getFloat(String key) {
        return getInstance().sp.getFloat(key, 0);
    }

    /**
     * 存储一个boolean数据类型
     */
    public static void putBoolean(String key, boolean value) {
        getInstance().sp.edit().putString(key, String.valueOf(value)).apply();
    }

    /**
     * 获取一个boolean数据类型
     */
    public static boolean getBoolean(String key) {
        return getInstance().sp.getString(key, "").equals(String.valueOf(true));
    }

    /**
     * 存储一个String数据类型
     */
    public static void putString(String key, String value) {
        getInstance().sp.edit().putString(key, value).apply();
    }

    /**
     * 获取一个String数据类型
     */
    public static String getString(String key) {
        return getInstance().sp.getString(key, "");
    }

    /**
     * 存储一个Int数据类型
     */
    public static void putInt(String key, int value) {
        getInstance().sp.edit().putInt(key, value).apply();
    }

    /**
     * 存储一个Long数据类型
     */
    public static void putLong(String key, long value) {
        getInstance().sp.edit().putLong(key, value).apply();
    }

    /**
     * 获取一个Int数据类型
     */
    public static int getInt(String key) {
        return getInstance().sp.getInt(key, -1);
    }

    /**
     * 获取一个Int数据类型
     */
    public static long getLong(String key) {
        return getInstance().sp.getLong(key, -1);
    }

}
