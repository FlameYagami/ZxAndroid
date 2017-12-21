package com.dab.zx.config;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.dab.zx.bean.UpdateBean;
import com.dab.zx.game.Client;
import com.dab.zx.uitls.PathManager;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by 八神火焰 on 2016/12/10.
 */

public class MyApp extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static UpdateBean mUpdateBean;
    public static Client     Client;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        Client = new Client();
        PathManager.init(context);
        FileDownloader.init(context);
    }
}
