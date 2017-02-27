package com.zx.config;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.liulishuo.filedownloader.FileDownloader;
import com.zx.R;
import com.zx.bean.UpdateBean;
import com.zx.game.Client;

import java.io.File;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class MyApp extends Application
{
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static String appCache;
    public static String pictureCache;
    public static String pictureZipPath;
    public static String databasePath;
    public static String deckPath;
    public static String downloadPath;
    public static String banlistPath;

    public static UpdateBean mUpdateBean;
    public static Client     Client;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        Client = new Client();
        FileDownloader.init(context);
        initCache();
    }

    /**
     * 初始化路径
     */
    private void initCache() {
        appCache = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.app_name) + File.separator;
        pictureCache = appCache + context.getString(R.string.picture);
        pictureZipPath = pictureCache + context.getString(R.string.zip_extension);
        databasePath = "data/data/" + context.getPackageName() + "/databases/data.db";
        deckPath = appCache + "deck/";
        downloadPath = appCache + "download/";
        banlistPath = appCache + "banlist";
    }

}
