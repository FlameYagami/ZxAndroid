package com.dab.zx.uitls;

import android.content.Context;
import android.os.Environment;

import com.dab.zx.R;

import java.io.File;

import static com.dab.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2017/3/6.
 */

public class PathManager {
    public static String appDir;
    public static String deckDir;
    public static String downloadDir;
    public static String pictureDir;

    public static String pictureZipPath;
    public static String databasePath;
    public static String restrictPath;

    public static void init(Context context) {
        appDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + File.separator;
        deckDir = appDir + "deck/";
        downloadDir = appDir + "download/";
        pictureDir = appDir + "picture/";

        pictureZipPath = appDir + "picture.zip";
        databasePath = getDatabasePath();
        restrictPath = appDir + "restrict";
    }

    private static String getDatabasePath() {
        if (SystemUtils.isInstallOnSDCard()) {
            return context.getFilesDir().getParent() + "/databases/data.db";
        } else {
            return "data/data/" + context.getPackageName() + "/databases/data.db";
        }
    }
}
