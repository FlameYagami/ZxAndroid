package com.zx.config;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;
import com.zx.bean.UpdateBean;
import com.zx.game.Client;
import com.zx.uitls.PathManager;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class MyApp extends Application
{
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static UpdateBean mUpdateBean;
    public static Client     Client;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        Client = new Client();
        PathManager.init(context);
        FileDownloader.init(context);
    }
}
