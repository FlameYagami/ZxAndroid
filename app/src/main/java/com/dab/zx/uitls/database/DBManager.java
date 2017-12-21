package com.dab.zx.uitls.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dab.zx.uitls.LogUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static com.dab.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2017/1/5.
 */

/**
 * 数据库管理工具
 */
class DBManager {
    private static String TAG = DBManager.class.getSimpleName();

    /**
     * DatabaseManager接口
     */
    private static DBManager instance;

    /**
     * 数据库打开计数
     */
    private AtomicInteger mOpenCounter = new AtomicInteger();

    /**
     * 数据库操作对象
     */
    private static SQLiteHelper sqLiteHelper;

    /**
     * 数据库对象
     */
    private SQLiteDatabase sqLiteDatabase;

    /**
     * 获取DatabaseManager接口
     *
     * @return instance
     */
    public static synchronized DBManager getInstance() {
        if (null == instance) {
            instance = new DBManager();
        }
        if (null == sqLiteHelper) {
            sqLiteHelper = new SQLiteHelper(context, "data.db", 1);
        }
        return instance;
    }

    /**
     * 释放DatabaseManager接口
     */
    public static synchronized void releaseInstance() {
        if (instance != null) {
            getInstance().closeDatabase();
            instance = null;
        }
    }

    /**
     * 获取数据库操作对象接口
     *
     * @return instance
     */
    SQLiteHelper getSQLiteHelper() {
        return sqLiteHelper;
    }

    /**
     * 打开数据库
     *
     * @return 数据库对象
     */
    synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            sqLiteDatabase = sqLiteHelper.getWritableDatabase();
            LogUtils.e(TAG, "openDatabase->" + mOpenCounter.intValue());
        }
        return sqLiteDatabase;
    }

    /**
     * 关闭数据库
     */
    synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            sqLiteDatabase.close();
            LogUtils.e(TAG, "closeDatabase->" + mOpenCounter.intValue());
        }
    }

    /**
     * 删除数据库
     *
     * @param context      上下文
     * @param dataBaseName 数据库名称
     * @return 返回是否删除成功
     */
    public static boolean deleteDatabase(Context context, String dataBaseName) {
        return context.deleteDatabase(dataBaseName);
    }
}