package com.zx.config;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.zx.R;
import com.zx.bean.UpdateBean;

import java.io.File;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class MyApp extends Application
{
    public static Context context;

    public static String  appCache;
    public static String  pictureCache;
    public static String  pictureZipPath;
    public static String  databasePath;
    public static String  deckPath;
    public static String  downloadPath;
    public static String  banlistPath;

    public static UpdateBean updateBean;

    @Override
    public void onCreate() {
        context = getApplicationContext();
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
